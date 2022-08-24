package util

object Session {

  private var userId: Option[String] = None

  def setUserId(id: String): Unit = {
    userId = Option(id).filter(_.nonEmpty)
  }

  def clearUserId(): Unit = userId = None

  def getUserId: Option[String] = userId
}
