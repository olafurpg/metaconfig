package metaconfig.internal

import java.nio.file.Paths
import scala.meta.testkit.DiffAssertions
import org.scalatest.FunSuite
import metaconfig.annotation._
import metaconfig._
import metaconfig.generic.Settings

case class Options(
    @Description("The input directory to generate the fox site.")
    @ExtraName("i")
    in: String = Paths.get("docs").toString,
    @Description("The output directory to generate the fox site.")
    @ExtraName("o")
    out: String = Paths.get("target").resolve("fox").toString,
    cwd: String = Paths.get(".").toAbsolutePath.toString,
    repoName: String = "olafurpg/fox",
    repoUrl: String = "https://github.com/olafurpg/fox",
    title: String = "Fox",
    description: String = "My Description",
    googleAnalytics: List[String] = Nil,
    classpath: List[String] = Nil,
    cleanTarget: Boolean = false,
    baseUrl: String = "",
    encoding: String = "UTF-8",
    configPath: String = Paths.get("fox.conf").toString,
    remainingArgs: List[String] = Nil
)
object Options {
  implicit val surface = generic.deriveSurface[Options]
  implicit val decoder: ConfDecoder[Options] =
    generic.deriveDecoder[Options](Options())
}

class CliParserSuite extends FunSuite with DiffAssertions {
  val settings = Settings[Options]
  def toString(options: Options): String = {
    settings.settings
      .zip(options.productIterator.toList)
      .map {
        case (s, v) =>
          s"${s.name} = $v"
      }
      .mkString("\n")
  }
  def check(name: String, args: List[String], expectedOptions: Options): Unit = {
    test(name) {
      val conf = Conf.parseCliArgs[Options](args).get
      val obtainedOptions = ConfDecoder[Options].read(conf).get
      val obtained = toString(obtainedOptions)
      val expected = toString(expectedOptions)
      assertNoDiff(obtained, expected)
    }
  }

  check(
    "empty",
    Nil,
    Options()
  )

  check(
    "boolean",
    "--clean-target" :: Nil,
    Options(cleanTarget = true)
  )

  check(
    "string",
    "--in" :: "in" :: Nil,
    Options(in = "in")
  )

  check(
    "string2",
    "--in" :: "in" :: "--out" :: "out" :: Nil,
    Options(in = "in", out = "out")
  )

  check(
    "kebab",
    "--base-url" :: "base-url" :: Nil,
    Options(baseUrl = "base-url")
  )

  check(
    "remainingArgs",
    "arg1" :: Nil,
    Options(remainingArgs = "arg1" :: Nil)
  )

  check(
    "flag+remainingArgs",
    "--clean-target" :: "arg1" :: Nil,
    Options(cleanTarget = true, remainingArgs = "arg1" :: Nil)
  )

  check(
    "arg+value+remainingArgs",
    "--in" :: "in" :: "arg1" :: Nil,
    Options(in = "in", remainingArgs = "arg1" :: Nil)
  )

  check(
    "extraName",
    "-i" :: "in" :: "arg1" :: Nil,
    Options(in = "in", remainingArgs = "arg1" :: Nil)
  )

}
