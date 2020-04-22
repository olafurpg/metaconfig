package metaconfig.cli

import metaconfig.annotation._
import metaconfig.Configuration

@BinaryName("echo")
@Usage("echo [OPTIONS] [ARGUMENTS ...]")
@ExampleUsage(
  """|$ echo Hello world!
     |Hello world!
     |$ echo --uppercase Hello world!
     |HELLO WORLD!
     |""".stripMargin
)
final case class EchoOptions(
    @Description("Print out additional information.")
    verbose: Boolean = false,
    @Description("Print the message in all uppercase.")
    uppercase: Boolean = false
)

object EchoOptions {

  val default = EchoOptions()
  implicit val config: Configuration[EchoOptions] =
    metaconfig.generic.deriveConfiguration[EchoOptions](default)
  def main(args: Array[String]): Unit = {
    val app = CliApp(
      "1.0.0",
      "echo",
      commands = List(
        HelpCommand,
        EchoCommand
      )
    )
    app.run(args.toList)
  }
}
