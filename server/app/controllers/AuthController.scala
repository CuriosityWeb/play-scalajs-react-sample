package controllers

import dao.UserDao
import model.ReadsAndWrites._
import model.User
import org.webjars.play.WebJarsUtil
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import util.PasswordHelper

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
final class AuthController @Inject()(userDao: UserDao,
                                     controllerComponents: ControllerComponents)(
                                      implicit ec: ExecutionContext, webJarsUtil: WebJarsUtil)
  extends AbstractController(controllerComponents) {

  def index: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.auth())
  }

  def register: Action[AnyContent] = Action.async { implicit request =>
    request.body.asJson match {
      case Some(jsValue) => jsValue.validate[User].asOpt match {

        case Some(User(id, password)) =>
          val (salt, hash) = PasswordHelper.generateSaltAndHash(password)
          userDao.saveUser(id, salt, hash).transform {
            case Success(_) => Success(Ok)

            case Failure(_: java.sql.SQLIntegrityConstraintViolationException) => Success(Conflict)

            case Failure(exception) => Success(InternalServerError(exception.getMessage))
          }

        case None => Future.successful(BadRequest("Request body not present or corrupted"))
      }

      case None => Future.successful(BadRequest("Request body not present or corrupted"))
    }
  }

  def login: Action[AnyContent] = Action.async { implicit request =>
    request.body.asJson match {
      case Some(jsValue) => jsValue.validate[User].asOpt match {

        case Some(User(id, password)) => userDao.getUserSaltAndHash(id).transform {

          case Success(Some((salt, hash))) =>
            if (PasswordHelper.isValidPassword(password, salt, hash)) {
              Success(Ok.withSession("user-id" -> id))
            } else {
              Success(BadRequest(s"Password for id: $id is incorrect"))
            }

          case Success(None) => Success(NotFound)

          case Failure(exception) => Success(InternalServerError(exception.getMessage))
        }

        case None => Future.successful(BadRequest("Request body not present or corrupted"))
      }

      case None => Future.successful(BadRequest("Request body not present or corrupted"))
    }
  }

  def logout: Action[AnyContent] = Action {
    Ok.withNewSession
  }
}
