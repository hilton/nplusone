package models.db

import com.github.tototoshi.slick.MySQLJodaSupport._
import models.{ChipCase, User}
import org.joda.time.DateTime
import play.api.db.slick.Config.driver.simple._
import securesocial.core.IdentityId

/**
 * Database table mapping for `ChipCase`.
 */
class ChipCases(tag: Tag) extends Table[ChipCase](tag, "chip") {

  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def patientNumber = column[String]("patient")

  def fracture = column[Boolean]("fracture")
  def contusion = column[Boolean]("contusion")
  def vomit = column[Boolean]("vomit")
  def loc = column[Boolean]("loc")
  def seizure = column[Boolean]("SEIZURE")

  def age = column[Int]("age")
  def act = column[Boolean]("act")
  def mechanism = column[Option[String]]("mechanism")

  def gcs0 = column[Option[Int]]("gcs0")
  def gcs1 = column[Option[Int]]("gcs1")
  def pta = column[Option[Int]]("pta")
  def memory = column[Boolean]("memory")
  def deficit = column[Boolean]("deficit")

  def created = column[DateTime]("created")
  def createdBy = column[String]("created_by")
  def uuid = column[String]("uuid")

  def * = (id.?, patientNumber, fracture, contusion, vomit, loc, seizure, age, act, mechanism, gcs0, gcs1, pta,
    memory, deficit, created, createdBy, uuid).shaped <> (ChipCase.tupled, ChipCase.unapply)

}
