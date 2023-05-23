package components

import com.github.ahnfelt.react4s._
import io.circe.generic.auto._
import model.User
import org.scalajs.dom.html.Input
import org.scalajs.dom.{HttpMethod, document, window}
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits.global
import util.Ajax

import scala.util.{Failure, Success}

final case class FormComponent() extends Component[NoEmit] {

  override def render(get: Get): Node =
    E.div(
      A.id("auth"),
      A.className("flex-column"),
      E.label(Text("User Id:")),
      E.input(A.`type`("text")),
      E.label(Text("Password:")),
      E.input(A.`type`("password")),
      E.div(
        A.className("flex-row"),
        E.button(S.marginRight("2vw"), A.onClick(_ => register()), Text("Register")),
        E.button(S.marginLeft("2vw"), A.id("login"), A.onClick(_ => login()), Text("Login"))))

  private def register(): Unit = executeOperation("/auth/register") {
    case (_, 200, _) =>
      stopLoading()
      window.alert("User registered, login to continue")
      clearInput()

    case (User(id, _), 409, _) =>
      stopLoading()
      window.alert(s"User with same user id already present, try again with different user id: $id")
      clearInput()

    case (User(id, _), status, msg) =>
      stopLoading()
      window.alert(s"Something bad happened for user-id: $id with status code: $status & error message from server: $msg")
      clearInput()
  }

  private def login(): Unit = executeOperation("/auth/login") {
    case (user, 404, _) =>
      stopLoading()
      window.alert(s"User Id is not registered: ${user.id}")
      clearInput()

    case (_, 200, _) =>
      stopLoading()
      clearInput()
      window.location.replace("/")

    case (User(id, _), status, msg) =>
      stopLoading()
      window.alert(s"Something bad happened for user-id: $id with status code: $status & error message from server: $msg")
      clearInput()
  }

  private def executeOperation(uri: String)(fxn: (User, Int, String) => Unit): Unit = {
    val userOpt = for {
      userId <- Option(textInputElement).map(_.value.trim).filter(_.nonEmpty)

      password <- Option(passwordInputElement).map(_.value.trim).filter(_.nonEmpty)
    } yield User(userId, password)

    userOpt match {
      case Some(user) =>
        startLoading()
        Ajax.request(HttpMethod.POST, uri, user, Map.empty)
          .flatMap(resp => resp.text().toFuture.map(body => (resp.status, body))).onComplete {
          case Success((status, body)) => fxn(user, status, body)

          case Failure(exception) =>
            stopLoading()
            exception.printStackTrace()
            window.alert(s"Something bad happened with error message as: ${exception.getMessage}")
            clearInput()
        }

      case None => window.alert("UserId/Password can not be empty")
    }
  }

  private def clearInput(): Unit = {
    textInputElement.value = ""
    passwordInputElement.value = ""
  }

  private def textInputElement = document.querySelector(" #auth > input[type=text]").asInstanceOf[Input]

  private def passwordInputElement = document.querySelector(" #auth > input[type=password]").asInstanceOf[Input]

  private def startLoading(): Unit = document.getElementById("overlay").setAttribute("style", "display:flex")

  private def stopLoading(): Unit = document.getElementById("overlay").setAttribute("style", "display:none")
}
