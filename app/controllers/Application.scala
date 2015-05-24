package controllers

import java.sql.Timestamp

import models.DAO
import models.DTO.Ticket
import play.api.{Logger, Routes}
import play.api.libs.json._
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

import play.api.libs.concurrent.Execution.Implicits.defaultContext

import play.api.libs.functional.syntax._

object Application extends Controller with Secured {

  def javascriptRoutes() = Action { implicit request =>
    Ok(Routes.javascriptRouter("jsRoutes")(
      controllers.routes.javascript.Application.newticketSubmit,
      controllers.routes.javascript.Application.editableTicket,
      controllers.routes.javascript.Application.editticketSubmit,
      controllers.routes.javascript.Application.listTickets,
      controllers.routes.javascript.Application.closeTicket
    )).as(JAVASCRIPT)
  }

  def index = Action { implicit request =>
    //Ok(views.html.index("Ticketing System"))
    Redirect(routes.Application.home)
  }

  def home() = withUser { user => implicit request =>
    Ok(views.html.home())
  }

  def profile(email: String) = withUser { user => implicit request =>
    Ok(views.html.profile())
  }

  case class TicketInfo(customerId: Long, heading: String, desc: String, assignedToId: Option[Long])

  implicit val reads: Reads[TicketInfo] = (
    (JsPath \ "customerId").read[Long] and
      (JsPath \ "heading").read[String] and
      (JsPath \ "desc").read[String] and
      (JsPath \ "assignedToId").read[Option[Long]]
    )(TicketInfo.apply _)


  def newticket() = withUser { user => implicit request =>
    Ok(views.html.newticket())
  }

  def newticketSubmit() = withAsyncUser(parse.json) { user => implicit request =>
    request.body.validate[TicketInfo] match {
      case success: JsSuccess[TicketInfo] => {
        val data = success.get
        Logger.info(data.toString)
        scala.concurrent.blocking {
          if (DAO.customerExists(data.customerId)) {
            data.assignedToId match {
              case Some(id) => {
                if (DAO.userExists(id)) {
                    //store
                  import java.util.Date
                  val ticket = Ticket(user.id.get, data.customerId, Some(data.assignedToId.get), data.heading, data.desc,
                    2, new Timestamp(new Date().getTime))
                  DAO.saveTicket(ticket)
                  Future(Ok(Json.obj("success" -> "ticket opened")))
                } else {
                  Future(Ok(Json.obj("failure" -> "User id(assignedToId) does not exists")))
                }
              }
              case None => {
                  //store
                import java.util.Date
                val ticket = Ticket(user.id.get, data.customerId, None, data.heading, data.desc,
                  1, new Timestamp(new Date().getTime))
                DAO.saveTicket(ticket)
                Future(Ok(Json.obj("success" -> "new ticket")))
              }
            }
          } else {
            Future(Ok(Json.obj("failure" -> "Customer id does not exist")))
          }
        }
        //Future(Ok(Json.obj("success" -> "submitted")))
      }
      case error: JsError => Future(Ok(Json.obj("errors" -> JsError.toFlatJson(error))))
    }
  }

  def editticket(ticketId: Long) = withUser { user => implicit request =>
    Ok(views.html.editticket(ticketId))
  }

  implicit val writes: Writes[TicketInfo] = (
    (JsPath \ "customerId").write[Long] and
      (JsPath \ "heading").write[String] and
      (JsPath \ "desc").write[String] and
      (JsPath \ "assignedToId").write[Option[Long]]
    )(unlift(TicketInfo.unapply))

  def editableTicket(ticketId: Long) = withUser{user => implicit request =>
    scala.concurrent.blocking {
      DAO.getMyTicket(user.id.get, ticketId) match {
        case Some(ticket) => {
          if (ticket.status < 3) {
            val ticketInfo = TicketInfo(ticket.customerId, ticket.name, ticket.desc, ticket.assignedTo)
            Ok(Json.obj("success" -> Json.toJson(ticketInfo)))
          } else {
            Ok(Json.obj("failure" -> "Item Not editable(Ticket Closed)."))
          }
        }
        case None => Ok(Json.obj("failure" -> "ticket not found.(One can only edit one's tickets)"))
      }
    }
  }

  case class TicketInfoWithId(ticketId: Long, customerId: Long, heading: String, desc: String, assignedToId: Option[Long])

  implicit val ticketInfoWithIdReads: Reads[TicketInfoWithId] = (
    (JsPath \ "ticketId").read[Long] and
    (JsPath \ "customerId").read[Long] and
    (JsPath \ "heading").read[String] and
    (JsPath \ "desc").read[String] and
    (JsPath \ "assignedToId").read[Option[Long]]
    )(TicketInfoWithId.apply _)

  def editticketSubmit() = withAsyncUser(parse.json) { user => implicit request =>
    Future {
      request.body.validate[TicketInfoWithId] match {
        case success: JsSuccess[TicketInfoWithId] => {
          val data = success.get
          scala.concurrent.blocking {
            if (DAO.customerExists(data.customerId)) {
              data.assignedToId match {
                case Some(id) => {
                  if (DAO.userExists(id)) {
                    DAO.updateTicket(user.id.get, data)
                    Ok(Json.obj("success" -> "successfully updated."))
                  } else {
                    Ok(Json.obj("failure" -> "User does not exist."))
                  }
                }
                case None => {
                  DAO.updateTicket(user.id.get, data)
                  Ok(Json.obj("success" -> "successfully updated."))
                }
              }
            } else {
              Ok(Json.obj("failure" -> "Customer Id does not exist"))
            }
          }
        }
        case error: JsError => {
          Ok(Json.obj("errors" -> JsError.toFlatJson(error)))
        }
      }
    }
  }

  def listTickets(page: Int) = withUser {user => implicit request => {
    implicit val listWrites: Writes[(String, String, String, Int, Long , String)] =
      new Writes[(String, String, String, Int, Long, String)] {
      override def writes(o: (String, String, String, Int, Long, String)): JsValue = {
        Json.obj("userEmail" -> o._1, "heading" -> o._2, "desc" -> o._3, "status" -> o._4,
          "id" -> o._5, "customerEmail" -> o._6)
      }
    }
    if (page < 1) Ok(Json.obj("failure" -> "In valid page Id.")) else {
      scala.concurrent.blocking {
        val list = DAO.listTickets(page)
        if (list.isEmpty) Ok(Json.obj("failure" -> "end of results.")) else Ok(Json.obj("success" -> Json.toJson(list)))
      }
    }
  }}

  def closeTicket(id: Long) = withUser { user => implicit request => {
    scala.concurrent.blocking {
      val status = DAO.closeTicket(id)
      if (status > 0) Ok(Json.obj("success" -> "closed")) else Ok(Json.obj("failure" -> "operation failed.(ticket cannot be closed until it is assigned to someone)"))
    }
  }}

  def ticket(ticketId: Long) = withUser { user => implicit request => {
    Ok(views.html.ticket())
  }}
}