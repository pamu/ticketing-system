package controllers

import models.DAO
import play.api.data.Form
import play.api.mvc.{Action, Controller}

import play.api.data.Forms._

import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

/**
 * Created by pnagarjuna on 22/05/15.
 */
object Auth extends Controller {

  val loginForm = Form(
    tuple(
      "email" -> email,
      "password" -> nonEmptyText(minLength = 6, maxLength = 25)
    ).verifying("error.login_failed", data => {
      scala.concurrent.blocking(DAO.auth(data._1, data._2))
    })
  )

  def login = Action { implicit request =>
    request.session.get("email").map(email => Redirect(routes.Application.home)).getOrElse(Ok(views.html.login(loginForm)(request.flash)))
  }

  def loginSubmit = Action { implicit request =>
    loginForm.bindFromRequest().fold(
      hasErrors => BadRequest(views.html.login(hasErrors)(request.flash)),
      success => {
        Redirect(routes.Application.home()).withSession("email" -> success._1)
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
    }).verifying("error.signup_failed", data => ! DAO.exists(data._1))
  )

  def signup = Action { implicit request =>
    request.session.get("email").map(email => Redirect(routes.Application.home)).getOrElse(Ok(views.html.signup(signupForm)(request.flash)))
  }

  def signupSubmit = Action.async { implicit request =>
    signupForm.bindFromRequest().fold(
      hasErrors => Future(BadRequest(views.html.signup(hasErrors)(request.flash))),
      success => {
        val email = success._1
        val password = success._2._1
        scala.concurrent.blocking {
          DAO.createUser(email, password)
        }
        Future(Redirect(routes.Auth.login).flashing("success" -> "Signup successful."))
      }
    )
  }

  def logout() = Action { implicit request =>
    Redirect(routes.Auth.login).flashing("success" -> "logged out.")
  }

}
