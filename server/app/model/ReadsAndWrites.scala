package model

import play.api.libs.json.{Json, Reads}

object ReadsAndWrites {

  implicit val userReads: Reads[User] = Json.reads[User]
}
