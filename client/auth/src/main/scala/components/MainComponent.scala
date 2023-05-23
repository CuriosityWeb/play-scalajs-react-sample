package components

import com.github.ahnfelt.react4s._

final case class MainComponent() extends Component[NoEmit] {

  override def render(get: Get): Node = {
    E.div(Component(HeadingComponent), Component(InfoComponent), Component(OverlayComponent), Component(FormComponent))
  }
}
