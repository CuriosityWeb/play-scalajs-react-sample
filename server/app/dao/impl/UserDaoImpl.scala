package dao.impl

import dao.UserDao
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import java.sql.SQLException
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

final class UserDaoImpl @Inject()(override val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] with UserDao {

  import profile.api._

  override def saveUser(id: String, salt: String, hash: String): Future[Unit] =
    db.run(sqlu"""INSERT INTO USERS(ID, SALT, HASH) VALUES($id, $salt, $hash)""").map {
      case 1 => ()
      case x => throw new SQLException(s"User insertion failed, expected 1 but received $x")
    }

  override def getUserSaltAndHash(id: String): Future[Option[(String, String)]] =
    db.run(sql"""SELECT SALT, HASH FROM USERS WHERE ID = $id""".as[(String, String)].headOption)
}
