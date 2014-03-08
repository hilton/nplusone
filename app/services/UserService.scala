package services

import models.User
import play.api.Application
import securesocial.core.{SocialUser, Identity, IdentityId, UserServicePlugin}
import securesocial.core.providers.Token


class UserService(application: Application) extends UserServicePlugin(application) {

  /**
   * Finds a user that matches the specified ID.
   */
  def find(id: IdentityId): Option[Identity] = {
    User.findByUserId(id)
  }

  /**
   * Saves the authenticated user.  This method gets called when a user logs in.
   */
  def save(identity: Identity): Identity = {
    val user = identity match {
      case socialUser: SocialUser ⇒ User.forSocialUser(socialUser)
      case user: User ⇒ user
    }
    User.save(user)
  }

	// Not needed without user name and password authentication.
  def deleteExpiredTokens() { }
  def deleteToken(uuid: String) { }
  def findByEmailAndProvider(email: String, providerId: String): Option[Identity] = None
  def findToken(token: String): Option[Token] = None
  def save(token: Token) = { }
}