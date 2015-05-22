package controllers

import play.api.mvc.{Action, Controller}

object Application extends Controller {
  def index = Action {
    //Ok(views.html.index("Ticketing System"))
    Redirect(routes.Auth.login())
  }
}