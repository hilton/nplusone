package models.db

import models.User
import play.api.db.slick.Config.driver.simple._
import securesocial.core.{OAuth2Info, IdentityId}

/**
 * Database table mapping for `User`.
 */
class Users(tag: Tag) extends Table[User](tag, "USER") {

  implicit def tuple2IdentityId(tuple: (String, String)): IdentityId = tuple match {
    case (userId, providerId) ⇒ IdentityId(userId, providerId)
  }

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def userId = column[String]("USER_ID")
  def providerId = column[String]("PROVIDER_ID")
  def fullName = column[String]("FULL_NAME")

  def * = (id.?, userId, providerId, fullName) <> (mapRow _, unMapRow _)

  private def mapRow(tuple: (Option[Int], String, String, String)): User = tuple match {
    case (id, userId, providerId, fullName) => User(id, userId -> providerId, fullName)
  }

  private def unMapRow(user: User) = {
    Some( (user.id, user.identityId.userId, user.identityId.providerId, user.fullName) )
  }

}
