package controllers

import play.api.mvc.{Action, Controller}

object Application extends Controller with Secured {
  def index = Action {
    //Ok(views.html.index("Ticketing System"))
    Redirect(routes.Auth.login())
  }
  def home() = withUser { user => request =>
    Ok(views.html.home())
  }
}