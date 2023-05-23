package model

final case class UserSecret(id: String, salt: String, hash: String)
