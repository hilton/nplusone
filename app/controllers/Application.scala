package controllers

import play.api.mvc._
import securesocial.core.SecureSocial

object Application extends Controller with SecureSocial {

  /**
   * Start page for authenticated users.
   */
  def dashboard = SecuredAction { implicit request ⇒
    Ok(views.html.dashboard(request.user))
  }

  def chip = SecuredAction { implicit request ⇒
    Ok(views.html.chip(request.user))
  }

}