package components

import com.github.ahnfelt.react4s._

final case class InfoComponent() extends Component[NoEmit] {

  override def render(get: Get): Node =
    E.div(
      A.id("info"),
      A.className("flex-column"),
      E.h1(Text("> PlayFramework + Scala.js + React")),
      E.div(
        E.p(Text(
          """This is an example of Play framework integration with Scala.js; you can
            |register and then log in; the only functionalities supported by
            |this project are register, login, and logout.""".stripMargin)),
        E.p(Text("Main components of this application are :-")),
        E.div(mainComponents: _*)))

  private def mainComponents: Seq[Tag] =
    Map("Scala" -> "https://www.scala-lang.org/",
      "Play Framework (Backend)" -> "https://www.playframework.com/",
      "Scala.js (Frontend)" -> "https://www.scala-js.org/",
      "React4s (Frontend)" -> "http://www.react4s.org/").zipWithIndex.map {
      case ((name, link), index) => E.a(
        A.href(link), A.target("_blank"),
        Text(s"${index + 1}. $name")
      )
    }.toSeq :+ A.className("flex-column")
}
