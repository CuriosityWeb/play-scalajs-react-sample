package controllers

import org.webjars.play.WebJarsUtil
import play.api.mvc._

import javax.inject.{Inject, Singleton}

@Singleton
final class MainController @Inject()(controllerComponents: ControllerComponents)(implicit webJarsUtil: WebJarsUtil)
  extends AbstractController(controllerComponents) {

  def index: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.main())
  }

  def getUserInfo: Action[AnyContent] = Action { implicit request =>
    request.session.get("user-id") match {
      case Some(userId) => Ok(userId)
      case None => NotFound
    }
  }
}
