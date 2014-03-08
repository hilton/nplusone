package models.db

import models.User
import play.api.db.slick.Config.driver.simple._
import securesocial.core.{OAuth2Info, IdentityId}

class Users(tag: Tag) extends Table[User](tag, "USER") {

  implicit def tuple2IdentityId(tuple: (String, String)): IdentityId = tuple match {
    case (userId, providerId) ⇒ IdentityId(userId, providerId)
  }

  implicit def tuple2OAuth2Info(tuple: (Option[String], Option[String], Option[Int], Option[String])) = tuple match {
    case (Some(token), tokenType, expiresIn, refreshToken) ⇒ Some(OAuth2Info(token, tokenType, expiresIn, refreshToken))
    case _ ⇒ None
  }

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def userId = column[String]("USER_ID")
  def providerId = column[String]("PROVIDER_ID")

  def accessToken = column[Option[String]]("ACCESS_TOKEN")
  def tokenType = column[Option[String]]("TOKEN_TYPE")
  def expiresIn = column[Option[Int]]("EXPIRES_IN")
  def refreshToken = column[Option[String]]("REFRESH_TOKEN")

  def fullName = column[String]("FULL_NAME")

  def * = (id.?, userId, providerId, accessToken, tokenType, expiresIn, refreshToken, fullName) <> (mapRow _, unMapRow _)

  private def mapRow(tuple: (Option[Int], String, String, Option[String], Option[String], Option[Int], Option[String], String)): User = tuple match {
    case (id, userId, providerId, accessToken, tokenType, expiresIn, refreshToken, fullName) =>
      User(id, userId -> providerId, (accessToken, tokenType, expiresIn, refreshToken), fullName)
  }

  private def unMapRow(user: User) = {
    Some( (user.id, user.identityId.userId, user.identityId.providerId, user.oAuth2Info.map(_.accessToken),
      user.oAuth2Info.flatMap(_.tokenType), user.oAuth2Info.flatMap(_.expiresIn),
      user.oAuth2Info.flatMap(_.refreshToken), user.fullName) )
  }

}
