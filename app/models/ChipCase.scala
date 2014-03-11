package models

import models.db.ChipCases
import org.joda.time.DateTime
import play.api.Play.current
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Logger
import scala.slick.lifted.TableQuery

/**
 * One CHIP study patient.
 */
case class ChipCase(
  id: Option[Int],
  patientNumber: String,

  fracture: Boolean,
  contusion: Boolean,
  vomit: Boolean,
  loc: Boolean,
  seizure: Boolean,

  age: Int,
  act: Boolean,
  mechanism: Option[String],

  gcs0: Option[Int],
  gcs1: Option[Int],
  pta: Option[Int],
  memory: Boolean,
  deficit: Boolean,

  created: DateTime,
  createdBy: String,
  uuid: String) {

  override def toString = s"ChipCase($uuid)"
}

/**
 * Data Access Object.
 */
object ChipCase extends ((Option[Int], String, Boolean, Boolean, Boolean, Boolean, Boolean, Int, Boolean, Option[String],
  Option[Int], Option[Int], Option[Int], Boolean, Boolean, DateTime, String, String) => ChipCase) {

  val cases = TableQuery[ChipCases]

  def count: Int = DB.withSession { implicit session: Session ⇒
    Query(cases.length).first
  }

  /**
   * Saves the given case.
   */
  def save(chipCase: ChipCase): Unit = DB.withSession { implicit session: Session ⇒
    Logger.debug(s"save $chipCase")
    cases += chipCase
  }
}