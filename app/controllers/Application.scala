package controllers

import java.util.UUID
import models.ChipCase
import org.joda.time.DateTime
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import securesocial.core.SecuredRequest
import securesocial.core.SecureSocial
import net.sf.jxls.transformer.XLSTransformer
import java.io.{ByteArrayInputStream, ByteArrayOutputStream, FileInputStream, File}
import scala.collection.mutable
import org.joda.time.format.DateTimeFormat
import play.api.libs.ws.WS
import scala.concurrent.Await
import play.api.Play

object Application extends Controller with SecureSocial {

  val exportTemplate = "/assets/chip.xls"

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

  /**
   * Input form.
   */
  def chip = SecuredAction { implicit request ⇒
    Ok(views.html.chip(request.user, form))
  }

  /**
   * Exports all cases as a spreadsheet.
   */
  def export = SecuredAction { implicit request ⇒
    // Load the XLS template from a URL so it works without file system access on Cloudbees.
    import play.api.Play.current
    import scala.concurrent.duration._
    val url = Play.configuration.getString("application.url").getOrElse("http://localhost:9000") + exportTemplate
    val templateBytes = Await.result(WS.url(url).get, 2.seconds).ahcResponse.getResponseBodyAsBytes
    play.api.Logger.debug(s"template length ${templateBytes.length}")
    val template = new ByteArrayInputStream(templateBytes)

    // Generate a spreadsheet.
    import scala.collection.JavaConverters._
    val beans = mutable.HashMap("cases" -> ChipCase.find.asJava)
    val workbook = new XLSTransformer().transformXLS(template, beans.asJava)
    template.close()
    val outputStream = new ByteArrayOutputStream()
    workbook.write(outputStream)
    outputStream.close()

    // Serve a binary response.
    val contentType = "application/vnd.ms-excel"
    val timestamp = DateTimeFormat.forPattern("yyyy-MM-dd-HHmm").print(DateTime.now())
    val headers = "Content-disposition" -> s"""inline;filename="chip-results-$timestamp.xls""""
    Ok(outputStream.toByteArray).withHeaders(headers).as(contentType)
  }

  /**
   * Handles form submission.
   */
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