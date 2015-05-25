package models

import java.sql.Timestamp

//import scala.slick.driver.PostgresDriver.simple._
//import scala.slick.driver.MySQLDriver.simple._
import scala.slick.driver.H2Driver.simple._

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

    def * = (email, password, timestamp,  id.?) <>(User.tupled, User.unapply)

  }

  val customerTable = "customers"

  class Customers(tag: Tag) extends Table[Customer](tag, customerTable) {

    def email = column[String]("email", O.NotNull)

    def timestamp = column[Timestamp]("timestamp", O.NotNull)

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def * = (email, timestamp, id.?) <> (Customer.tupled, Customer.unapply)

  }

  val ticketTable = "tickets"

  class Tickets(tag: Tag) extends Table[Ticket](tag, ticketTable) {

    def authorId = column[Long]("authorId", O.NotNull)

    def customerId = column[Long]("customerId", O.NotNull)

    def assignedToId = column[Long]("assignedId", O.Nullable)

    def name = column[String]("name", O.NotNull)

    def desc = column[String]("desc", O.NotNull)

    def status = column[Int]("status", O.NotNull)

    def timestamp = column[Timestamp]("timestamp", O.NotNull)

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def * = (authorId, customerId, assignedToId.?, name, desc, status, timestamp, id.?) <> ((Ticket.apply _).tupled, Ticket.unapply)

    def authorIdFK = foreignKey("tickets_author_id_fk", authorId, TableQuery[Users])(_.id, ForeignKeyAction.Cascade)

    def customerIdFK = foreignKey("tickets_customer_id_fk", customerId, TableQuery[Customers])(_.id, ForeignKeyAction.Cascade, ForeignKeyAction.Cascade)

    def assignedToIdFK = foreignKey("tickets_assigned_to_id_fk", assignedToId, TableQuery[Users])(_.id, ForeignKeyAction.Cascade, ForeignKeyAction.Cascade)

  }

  val commentTable = "comments"

  class Comments(tag: Tag) extends Table[Comment](tag, commentTable) {

    def commenterId = column[Long]("commenterId", O.NotNull)

    def ticketId = column[Long]("ticketId", O.NotNull)

    def comment = column[String]("comment", O.NotNull)

    def timestamp = column[Timestamp]("timestamp", O.NotNull)

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def * = (commenterId, ticketId, comment, timestamp, id.?) <> (Comment.tupled, Comment.unapply)

    def commenterIdFK = foreignKey("comments_commenter_id_fk", commenterId, TableQuery[Users])(_.id, ForeignKeyAction.Cascade, ForeignKeyAction.Cascade)

    def ticketIdFK = foreignKey("comments_ticket_id_fk", ticketId, TableQuery[Tickets])(_.id, ForeignKeyAction.Cascade, ForeignKeyAction.Cascade)
  }

  val users = TableQuery[Users]

  val customers = TableQuery[Customers]

  val tickets = TableQuery[Tickets]

  val comments = TableQuery[Comments]

}