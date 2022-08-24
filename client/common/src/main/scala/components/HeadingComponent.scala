package components

import com.github.ahnfelt.react4s._

final case class HeadingComponent() extends Component[NoEmit] {

  override def render(get: Get): Node = E.div(Text("Curiosity Web"), A.id("heading"))
}
