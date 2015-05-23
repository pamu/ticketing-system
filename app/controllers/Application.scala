package controllers

import play.api.Routes
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

import play.api.libs.concurrent.Execution.Implicits.defaultContext

object Application extends Controller with Secured {

  def javascriptRoutes() = Action { implicit request =>
    import routes.javascript._
    Ok(Routes.javascriptRouter("jsRoutes")(

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

  def newticket() = withUser { user => implicit request =>
    Ok(views.html.newticket())
  }

  def newticketSubmit() = withAsyncUser(parse.json) { user => implicit request =>
    Future(Ok(""))
  }

}