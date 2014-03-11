package controllers

import java.util.UUID
import models.ChipCase
import org.joda.time.DateTime
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import securesocial.core.SecuredRequest
import securesocial.core.SecureSocial

object Application extends Controller with SecureSocial {

  def form(implicit request: SecuredRequest[_]) = Form(mapping(
    "id" -> ignored(Option.empty[Int]),
    "patient" -> nonEmptyText,
    "fracture" -> boolean,
    "contusion" -> boolean,
    "vomit" -> boolean,
    "loc" -> boolean,
    "seizure" -> boolean,
    "age" -> number(16, 120),
    "act" -> boolean,
    "mechanism" -> optional(text),
    "gcs0" -> optional(number(13, 15)),
    "gcs1" -> optional(number(3, 15)),
    "pta" -> optional(number(0, 4)),
    "memory" -> boolean,
    "deficit" -> boolean,
    "created" -> ignored(DateTime.now()),
    "createdBy" -> ignored(request.user.fullName),
    "uuid" -> ignored(UUID.randomUUID().toString))(ChipCase.apply)(ChipCase.unapply))

  /**
   * Start page for authenticated users.
   */
  def dashboard = SecuredAction { implicit request ⇒
    Ok(views.html.dashboard(request.user, ChipCase.count))
  }

  def chip = SecuredAction { implicit request ⇒
    Ok(views.html.chip(request.user, form))
  }

  def save = SecuredAction { implicit request ⇒
    val boundForm: Form[ChipCase] = form.bindFromRequest
    boundForm.fold(
      formWithErrors ⇒ {
        play.api.Logger.debug("errors = " + formWithErrors.errorsAsJson)
        BadRequest(views.html.chip(request.user, formWithErrors))
      },
      chipCase ⇒ {
        ChipCase.save(chipCase)
        Redirect(routes.Application.chip()).flashing("success" -> s"Saved case ${chipCase.patientNumber}")
      })
  }
}