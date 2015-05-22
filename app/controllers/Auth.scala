package controllers

import play.api.data.Form
import play.api.mvc.{Action, Controller}

import play.api.data.Forms._

/**
 * Created by pnagarjuna on 22/05/15.
 */
object Auth extends Controller {

  val loginForm = Form(
    tuple(
      "email" -> email,
      "password" -> nonEmptyText(minLength = 6, maxLength = 25)
    ).verifying("error.login_failed", data => true)
  )

  def login = Action { implicit request =>
    Ok(views.html.login(loginForm)(request.flash))
  }

  def loginSubmit = Action { implicit request =>
    loginForm.bindFromRequest().fold(
      hasErrors => BadRequest(views.html.login(hasErrors)(request.flash)),
      success => {
        Redirect(routes.Application.home()).withSession("email" -> success._1)
        //Ok(s"${success._1} ${success._2}")
      }
    )
  }

  val signupForm = Form(
    tuple(
      "email" -> email,
      "passwords" -> tuple(
        "first_time" -> nonEmptyText(minLength = 6, maxLength = 25),
        "second_time" -> nonEmptyText
      )
    ).verifying("error.password_match", data => {
      val pws = data._2
      pws._1 == pws._2
    }).verifying("error.signup_failed", data => true)
  )

  def signup = Action { implicit request =>
    Ok(views.html.signup(signupForm)(request.flash))
  }

  def signupSubmit = Action { implicit request =>
    signupForm.bindFromRequest().fold(
      hasErrors => BadRequest(views.html.signup(hasErrors)(request.flash)),
      success => Ok(s"${success._1} ${success._2}")
    )
  }
}
