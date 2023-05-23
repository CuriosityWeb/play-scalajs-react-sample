import com.github.ahnfelt.react4s._
import components._
import org.scalajs.dom.{HttpMethod, window}
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits.global
import util.Ajax

import scala.util.{Failure, Success}

object App extends scala.App {

  Ajax.request(HttpMethod.POST, "/main/getUserInfo", None, Map.empty).map(_.status).onComplete {
    case Success(200) => window.location.replace("/")

    case Success(404) => ReactBridge.renderToDomById(Component(MainComponent), "main")

    case Success(status) =>
      window.alert(s"Something bad happened with status: $status")

    case Failure(exception) =>
      exception.printStackTrace()
      window.alert(s"Something bad happened with exception msg: $exception")
  }
}
