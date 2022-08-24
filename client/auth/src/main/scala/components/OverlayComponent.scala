package components

import com.github.ahnfelt.react4s._

final case class OverlayComponent() extends Component[NoEmit] {

  override def render(get: Get): Node = E.div(A.id("overlay"), E.div())
}
