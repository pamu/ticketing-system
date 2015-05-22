package models

import java.sql.Timestamp

import scala.slick.driver.PostgresDriver.simple._
import models.DTO.{Comment, Customer, User, Ticket}

/**
 * Created by pnagarjuna on 22/05/15.
 */
object Tables {

  val usersTable = "users"

  class Users(tag: Tag) extends Table[User](tag, usersTable) {
    def email = column[String]("email", O.NotNull)

    def password = column[String]("password", O.NotNull)

    def timestamp = column[Timestamp]("timestamp", O.NotNull)

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def * = (email, password, id.?) <>(User.tupled, User.unapply)

  }

  val customerTable = "customers"

  class Customers(tag: Tag) extends Table[Customer](tag, customerTable) {

    def username = column[String]("username", O.NotNull)

    def email = column[String]("email", O.NotNull)

    def address = column[String]("address", O.NotNull)

    def timestamp = column[Timestamp]("timestamp", O.NotNull)

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def * = (username, email, address, timestamp, id.?) <> (Customer.tupled, Customer.unapply)

  }

  val ticketTable = "tickets"

  class Tickets(tag: Tag) extends Table[Ticket](tag, ticketTable) {

    def authorId = column[Long]("authorId", O.NotNull)

    def customerId = column[Long]("customerId", O.NotNull)

    def assignedId = column[Long]("assignedId", O.NotNull)

    def desc = column[String]("desc", O.NotNull)

    def status = column[Int]("status", O.NotNull)

    def timestamp = column[Timestamp]("timestamp", O.NotNull)

    def id = column[Long]("id", O.PrimaryKey, O.NotNull)

    def * = (customerId, desc, authorId, assignedId, status, timestamp, id.?) <> (Ticket.tupled, Ticket.unapply)

    //now foreign keys
  }

  val commentTable = "comments"

  class Comments(tag: Tag) extends Table[Comment](tag, commentTable) {

    def commenterId = column[Long]("commenterId", O.NotNull)

    def ticketId = column[Long]("ticketId", O.NotNull)

    def comment = column[String]("comment", O.NotNull)

    def timestamp = column[Timestamp]("timestamp", O.NotNull)

    def id = column[Long]("id", O.PrimaryKey, O.NotNull)

    def * = (commenterId, ticketId, comment, timestamp, id.?) <> (Comment.tupled, Comment.unapply)

    //now foriegn keys
  }
}