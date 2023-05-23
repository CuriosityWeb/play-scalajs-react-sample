package dao

import scala.concurrent.Future

trait UserDao {

  def saveUser(id: String, salt: String, hash: String): Future[Unit]

  def getUserSaltAndHash(id: String): Future[Option[(String, String)]]
}
