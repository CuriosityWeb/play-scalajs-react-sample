import com.github.ahnfelt.react4s._
import components.MainComponent
import org.scalajs.dom._
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits.global
import util.{Ajax, Session}

import scala.util.{Failure, Success}

object App extends scala.App {

  Ajax.request(HttpMethod.POST, "/main/getUserInfo", None, Map.empty)
    .flatMap(resp => resp.text().toFuture.map(body => (resp.status, body))).onComplete {
    case Success((200, userId)) =>
      Session.setUserId(userId)
      Session.getUserId match {
        case Some(_) => ReactBridge.renderToDomById(Component(MainComponent), "main")

        case None => window.alert(s"Something bad happened bad userid")
      }

    case Success((404, _)) => window.location.replace("/auth")

    case Success((status, msg)) =>
      window.alert(s"Something bad happened with status: $status & msg: $msg")

    case Failure(exception) =>
      exception.printStackTrace()
      window.alert(s"Something bad happened with exception msg: $exception")
  }
}
