package util

import io.circe._
import io.circe.syntax._
import org.scalajs.dom._

import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSConverters._

object Ajax {

  def request[T](method: HttpMethod,
                 uri: String,
                 body: T,
                 headers: Map[String, String])(implicit encoder: Encoder[T]): Future[Response] =
    fetch(uri, requestInit(method, body, headers)).toFuture

  def request(method: HttpMethod, uri: String, headers: Map[String, String]): Future[Response] =
    fetch(uri, requestInit(method, headers)).toFuture

  def requestInit[T](httpMethod: HttpMethod,
                     httpBody: T,
                     httpHeaders: Map[String, String])(implicit encoder: Encoder[T]): RequestInit = new RequestInit {
    method = httpMethod
    headers = (httpHeaders ++ Map("Content-Type" -> "application/json", "X-CSRF-Token" -> Cookies.get("CSRF-Token")))
      .map { case (key, value) => js.Array(key, value) }.toJSArray
    body = httpBody.asJson.noSpaces
  }

  def requestInit(httpMethod: HttpMethod,
                  httpHeaders: Map[String, String]): RequestInit = new RequestInit {
    method = httpMethod
    headers = (httpHeaders + ("X-CSRF-Token" -> Cookies.get("CSRF-Token")))
      .map { case (key, value) => js.Array(key, value) }.toJSArray
  }
}
