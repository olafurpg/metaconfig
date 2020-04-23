package metaconfig

import metaconfig.generic.Surface
import metaconfig.internal.NoTyposDecoder
import metaconfig.generic.Settings
import metaconfig.internal.CliParser

trait Configuration[T] extends ConfCodec[T] { self =>
  def surface: Surface[T]

  final def withNoTypos: Configuration[T] =
    withDecoder(NoTyposDecoder[T](self)(Settings(surface)))

  final def parseCommandLine(
      arguments: List[String]
  ): Configured[T] =
    CliParser
      .parseArgs[T](arguments)(Settings(surface))
      .andThen(conf => this.read(conf))
  final def parseString(
      text: String,
      parser: MetaconfigParser
  ): Configured[T] = parser.parseString[T](text)(self)
  final def parseFilename(
      filename: String,
      text: String,
      parser: MetaconfigParser
  ): Configured[T] = parser.parseFilename[T](filename, text)(this)
  final def parseInput(
      input: Input,
      parser: MetaconfigParser
  ): Configured[T] =
    parser.parseInput[T](input)(self)
  final def withDecoder(decoder: ConfDecoder[T]): Configuration[T] =
    new Configuration[T] {
      def read(conf: Conf): Configured[T] = decoder.read(conf)
      def write(value: T): Conf = self.write(value)
      def surface: Surface[T] = self.surface
    }
}

object Configuration {
  def apply[A](implicit ev: Configuration[A]): Configuration[A] = ev
  def fromSurfaceCodec[T](
      s: Surface[T],
      c: ConfCodec[T]
  ): Configuration[T] = new Configuration[T] {
    def read(conf: Conf): Configured[T] = c.read(conf)
    def write(value: T): Conf = c.write(value)
    def surface: Surface[T] = s
  }
}
