package models

import models.db.ChipCases
import org.joda.time.DateTime
import play.api.Play.current
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Logger
import scala.beans.BeanProperty
import scala.slick.lifted.TableQuery
import org.joda.time.format.DateTimeFormat

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

  def view: ChipCaseView = {
    val createdDate = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss").print(created)
    implicit def boolean2String(boolean: Boolean) = if (boolean) "yes" else "no"
    implicit def optionString2String(option: Option[String]) = option.getOrElse("")
    implicit def optionInt2String(option: Option[Int]) = option.map(_.toString).getOrElse("")
    ChipCaseView(patientNumber, fracture, contusion, vomit, loc, seizure, age, act, mechanism, gcs0, gcs1, pta,
      memory, deficit, createdDate, createdBy, uuid)
  }

  override def toString = s"ChipCase($uuid)"
}

/**
 * JavaBean view for jXLS export.
 */
case class ChipCaseView(
  @BeanProperty patientNumber: String,

  @BeanProperty fracture: String,
  @BeanProperty contusion: String,
  @BeanProperty vomit: String,
  @BeanProperty loc: String,
  @BeanProperty seizure: String,

  @BeanProperty age: Int,
  @BeanProperty act: String,
  @BeanProperty mechanism: String,

  @BeanProperty gcs0: String,
  @BeanProperty gcs1: String,
  @BeanProperty pta: String,
  @BeanProperty memory: String,
  @BeanProperty deficit: String,

  @BeanProperty created: String,
  @BeanProperty createdBy: String,
  @BeanProperty uuid: String)

/**
 * Data Access Object.
 */
object ChipCase extends ((Option[Int], String, Boolean, Boolean, Boolean, Boolean, Boolean, Int, Boolean, Option[String],
  Option[Int], Option[Int], Option[Int], Boolean, Boolean, DateTime, String, String) => ChipCase) {

  val cases = TableQuery[ChipCases]

  /**
   * Returns the number of cases.
   */
  def count: Int = DB.withSession { implicit session: Session ⇒
    Query(cases.length).first
  }

  /**
   * Returns a list of export view objects, ordered by date created.
   */
  def find: List[ChipCaseView] = DB.withSession { implicit session: Session ⇒
    cases.sortBy(_.created).list.map(_.view)
  }

  /**
   * Saves the given case.
   */
  def save(chipCase: ChipCase): Unit = DB.withSession { implicit session: Session ⇒
    Logger.debug(s"save $chipCase")
    cases += chipCase
  }
}