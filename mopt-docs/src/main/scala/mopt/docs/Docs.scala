package mopt.docs

import mopt.ConfEncoder
import scalatags.Text.all._
import mopt.generic.Setting
import mopt.generic.Settings
import scalatags.Text

object Docs {
  def htmlSetting(setting: Setting, defaultValue: Any): Text.TypedTag[String] =
    tr(
      td(code(setting.name)),
      td(code(setting.field.tpe)),
      td(setting.description),
      td(defaultValue.toString)
    )

  def html[T](
      default: T
  )(implicit settings: Settings[T], ev: ConfEncoder[T]): String = {
    val fields = settings.flat(ConfEncoder[T].writeObj(default)).map {
      case (setting, defaultValue) =>
        htmlSetting(setting, defaultValue)
    }
    table(
      thead(
        tr(
          th("Name"),
          th("Type"),
          th("Description"),
          th("Default value")
        )
      ),
      tbody(fields)
    ).toString()
  }
}
