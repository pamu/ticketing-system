package models

import java.sql.Timestamp
import java.util.Date

import models.DTO.{Customer, Ticket, User}
import play.api.Logger

import scala.slick.jdbc.meta.MTable

//import scala.slick.driver.PostgresDriver.simple._

import scala.slick.driver.MySQLDriver.simple._

/**
 * Created by pnagarjuna on 22/05/15.
 */
object DAO {
  def init = DB.db.withSession {implicit sx => {
    if (MTable.getTables(Tables.usersTable).list.isEmpty) {
      Tables.users.ddl.create
      if (MTable.getTables(Tables.customerTable).list.isEmpty) {
        Tables.customers.ddl.create
        if (MTable.getTables(Tables.ticketTable).list.isEmpty) {
          Tables.tickets.ddl.create
          if (MTable.getTables(Tables.commentTable).list.isEmpty) {
            Tables.comments.ddl.create
          }
        }
      }
    }
  }}

  def clean = DB.db.withSession{implicit sx => {
    try {
      Tables.comments.ddl.drop
      Tables.tickets.ddl.drop
      Tables.customers.ddl.drop
      Tables.users.ddl.drop
    } catch {case ex: Exception => Logger.info(s"reason: ${ex.getMessage}")}
  }}

  def getUser(email: String): Option[User] = DB.db.withSession {implicit sx => {
    import Tables._
    val q = for(user <- users.filter(_.email === email)) yield user
    q.firstOption
  }}

  def exists(email: String): Boolean = DB.db.withSession {implicit sx => {
    import Tables._
    val q = for(user <- users.filter(_.email === email)) yield user
    q.exists.run
  }}

  def auth(email: String, password: String): Boolean = DB.db.withSession {implicit sx => {
    import Tables._
    val q = for(user <- users.filter(_.email === email).filter(_.password === password)) yield user
    q.exists.run
  }}

  def createUser(email: String, password: String) = DB.db.withSession {implicit sx => {
    import Tables._
    users += User(email, password, new Timestamp(new Date().getTime))
  }}

  //customer exists
  def customerExists(id: Long): Boolean = DB.db.withSession {implicit sx => {
    import Tables._
    val q = for(customer <- customers.filter(_.id === id)) yield customer
    q.exists.run
  }}

  //user with id exists
  def userExists(id: Long): Boolean = DB.db.withSession {implicit sx => {
    import Tables._
    val q = for(user <- users.filter(_.id === id)) yield user
    q.exists.run
  }}


  //ticket
  def saveTicket(ticket: Ticket) = DB.db.withSession {implicit sx => {
    Tables.tickets += ticket
  }}

  def addCustomers = DB.db.withSession {implicit sx => {
    for(i <- 1 to 10) {
      val customer = Customer(s"person$i@gmail.com", new Timestamp(new Date().getTime))
      Tables.customers += customer
    }
  }}
}
