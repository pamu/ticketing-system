package models

import java.sql.Timestamp
import java.util.Date

import controllers.Application.{CommentInfo, TicketInfoWithId, TicketInfo}
import models.DTO.{Comment, Customer, Ticket, User}
import play.api.Logger

import scala.slick.jdbc.meta.MTable

import scala.slick.driver.PostgresDriver.simple._

//import scala.slick.driver.MySQLDriver.simple._

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


  def updateTicket(authorId: Long, ticketInfoWithId: TicketInfoWithId) = DB.db.withSession {implicit sx => {
    import Tables._
    val q = for(ticket <- tickets.filter(_.authorId === authorId).filter(_.id === ticketInfoWithId.ticketId).filter(_.status < 3)) yield ticket
    q.firstOption.map(ticket => {
      val status = ticketInfoWithId.assignedToId match {
        case Some(id) => 2
        case None => 1
      }
      val newTicket = Ticket(ticket.authorId, ticketInfoWithId.customerId, ticketInfoWithId.assignedToId,
        ticketInfoWithId.heading, ticketInfoWithId.desc, status, ticket.timestamp, ticket.id)
      q.update(newTicket)
    })
  }}

  def addCustomers = DB.db.withSession {implicit sx => {
    for(i <- 1 to 10) {
      val customer = Customer(s"person$i@gmail.com", new Timestamp(new Date().getTime))
      Tables.customers += customer
    }
  }}

  def listTickets(page: Int = 1, pageSize: Int = 2): Seq[(String, String, String, Int, Long, String)] = {
    DB.db.withSession {implicit sx => {
      import Tables._
      val q = for(((user, ticket), customer) <- users.innerJoin(tickets).on(_.id === _.authorId)
        .innerJoin(customers).on(_._2.customerId === _.id)) yield (user.email, ticket.name, ticket.desc, ticket.status,
        ticket.id, customer.email)
      val paged = Compiled((d: ConstColumn[Long], t: ConstColumn[Long]) => q.drop(d).take(t))
      //val p = q.drop((page - 1)* pageSize).take(pageSize)
      //val query = p.selectStatement
      //println(query)
      //p.list
      paged((page - 1) * pageSize, pageSize).run
    }}
  }

  def getMyTicket(authorId: Long, ticketId: Long) = DB.db.withSession {implicit sx => {
    import Tables._
    val q = for(ticket <- tickets.filter(_.id === ticketId).filter(_.authorId === authorId)) yield ticket
    q.firstOption
  }}

  def getAnyTicket(ticketId: Long): Option[(String, String, String, Int, Long, String)] = DB.db.withSession {implicit sx => {
    import Tables._
    val q = for(((ticket, user), customer) <- tickets.filter(_.id === ticketId).innerJoin(users).on(_.authorId === _.id)
      .innerJoin(customers).on(_._1.customerId === _.id)) yield (user.email, ticket.name, ticket.desc, ticket.status,
      ticket.id, customer.email)
    q.firstOption
  }}

  def closeTicket(id: Long): Int = DB.db.withSession{ implicit sx => {
    val q = for(ticket <- Tables.tickets.filter(_.id === id).filter(_.status > 1)) yield ticket.status
    q.update(3)
  }}

  def ticketExists(ticketId: Long): Boolean = DB.db.withSession {implicit sx => {
    val q = for(ticket <- Tables.tickets.filter(_.id === ticketId).filter(_.status < 3)) yield ticket
    q.exists.run
  }}

  def saveComment(commenterId: Long, data: CommentInfo) = DB.db.withSession {implicit sx => {
    Tables.comments += Comment(commenterId, data.ticketId, data.comment, new Timestamp(new Date().getTime))
  }}

  def listComments(ticketId: Long, page: Int, pageSize: Int = 2): Seq[(String, String)] = DB.db.withSession {implicit sx => {
    import Tables._
    val q = for((comment, user) <- comments.filter(_.ticketId === ticketId).innerJoin(users)
      .on(_.commenterId === _.id)) yield (user.email, comment.comment)
    val paged = Compiled((d: ConstColumn[Long], t: ConstColumn[Long]) => q.drop(d).take(t))
    paged((page - 1) * pageSize, pageSize).run
  }}
}
