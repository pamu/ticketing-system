package controllers

import play.api.{Logger, Routes}
import play.api.libs.json._
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

import play.api.libs.concurrent.Execution.Implicits.defaultContext

import play.api.libs.functional.syntax._

object Application extends Controller with Secured {

  def javascriptRoutes() = Action { implicit request =>
    import routes.javascript._
    Ok(Routes.javascriptRouter("jsRoutes")(
      controllers.routes.javascript.Application.newticketSubmit
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

  case class TicketInfo(userId: Long, heading: String, desc: String, assignedToId: Option[Long])

  implicit val reads: Reads[TicketInfo] = (
    (JsPath \ "userId").read[Long] and
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
        Future(Ok(Json.obj("success" -> "submitted")))
      }
      case error: JsError => Future(Ok(Json.obj("failure" -> JsError.toFlatJson(error))))
    }
  }

}