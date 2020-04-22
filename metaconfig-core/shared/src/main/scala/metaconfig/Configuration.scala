package metaconfig

import metaconfig.generic.Surface
import metaconfig.internal.NoTyposDecoder
import metaconfig.generic.Settings

trait Configuration[T] extends ConfCodec[T] { self =>
  def surface: Surface[T]
  def withNoTypos: Configuration[T] = new Configuration[T] {
    private val decoder = NoTyposDecoder[T](self)(Settings(surface))
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
