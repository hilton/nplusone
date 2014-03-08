package models

import play.api.Play.current
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import securesocial.core._
import models.db.Users
import securesocial.core.IdentityId
import securesocial.core.PasswordInfo

case class User(id: Option[Int], identityId: IdentityId, oAuth2Info: Option[OAuth2Info], fullName: String) extends Identity {

  def email: Option[String] = None
  def firstName = ""
  def lastName = ""
  def avatarUrl: Option[String] = None
  def authMethod: AuthenticationMethod = AuthenticationMethod.OAuth2
  def oAuth1Info: Option[OAuth1Info] = None
  def passwordInfo: Option[PasswordInfo] = None
}

object User {

  val users = TableQuery[Users]

  def findByUserId(identityId: IdentityId): Option[User] = DB.withSession { implicit session: Session ⇒
    users.filter(_.userId === identityId.userId).filter(_.providerId === identityId.providerId).firstOption
  }

  def forSocialUser(user: SocialUser): User = {
    User(None, user.identityId, user.oAuth2Info, user.fullName)
  }

  def save(user: User): User = DB.withSession { implicit session: Session ⇒
    findByUserId(user.identityId) match {
      case None ⇒ {
        val id = (users returning users.map(_.id)).insert(user)
        user.copy(id = Some(id))
      }
      case Some(existingUser) ⇒ {
        users.filter(_.id === existingUser.id).update(user.copy(id = existingUser.id))
        user.copy(id = existingUser.id)
      }
    }
  }
}