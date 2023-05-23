package components

import com.github.ahnfelt.react4s._
import org.scalajs.dom.{HttpMethod, window}
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits.global
import util.{Ajax, Session}

import scala.util.{Failure, Success}

final case class MainComponent() extends Component[NoEmit] {

  override def render(get: Get): Node =
    E.div(Component(HeadingComponent),
      E.div(
        A.className("flex-column"),
        A.id("info"),
        E.h1(Text(s"Hello, you are now logged in")),
        E.p(S.fontSize("x-large"), S.textAlign("center"), Text(s"User with user-id:"), E.span(S.color("red"), S.marginLeft("5px"), Text(s"${Session.getUserId.get}"))),
        E.p(Text("You can try closing this tab and reopening it via"),
          E.a(S.marginLeft("5px"), A.href("http://localhost:9000/"), A.target("_blank"), Text("localhost:9000")),
          Text(", but you will not see any login screen this time.")),
        E.p(Text("You can also try clicking on"),
          E.a(S.marginLeft("5px"), A.href("http://localhost:9000/auth"), A.target("_blank"), Text("localhost:9000/auth")),
          Text(",which will take you to the same page.")),
        E.p(Text("To logout, either click the logout button or restart the browser, which will log you out automatically.")),
        E.button(Text("Logout!"), A.onClick(_ => logout()))))

  def logout(): Unit = Ajax.request(HttpMethod.POST, "/auth/logout", None, Map.empty).map(_.status).onComplete {
    case Success(200) => window.location.replace("/auth")

    case Success(status) => window.alert(s"Something bad happened, status code: $status")

    case Failure(exception) =>
      exception.printStackTrace()
      window.alert(s"Something bad happened with error message as: ${exception.getMessage}")
  }
}
