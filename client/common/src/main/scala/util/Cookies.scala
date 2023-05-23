package util

import org.scalajs.dom.document

object Cookies {

  def get: Map[String, String] = document.cookie.split(";").map(_.trim).map(_.split("=")).map {
    case Array(key, value) => key -> value
  }.toMap
}
