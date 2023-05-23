package modules

import com.google.inject.AbstractModule
import dao.UserDao
import dao.impl.UserDaoImpl

final class DaoModule extends AbstractModule {

  override def configure(): Unit = {

    bind(classOf[UserDao]).to(classOf[UserDaoImpl])
  }
}
