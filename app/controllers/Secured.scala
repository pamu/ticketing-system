package controllers

import models.DAO
import models.DTO.User
import play.api.mvc._

import scala.concurrent.Future

import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by pnagarjuna on 23/05/15.
 */
trait Secured {
  private def email(request: RequestHeader) = request.session.get("email")
  private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Auth.login())
  def withAuth(f: String => Request[AnyContent] => Result) = Security.Authenticated(email, onUnauthorized) { email =>
    Action.async(request => Future(f(email)(request)))
  }
  def withAsyncAuth[A](p: BodyParser[A])(f: String => Request[A] => Future[Result]) = Security.Authenticated(email, onUnauthorized) { email =>
    Action.async(p)(request => f(email)(request))
  }

  def withUser(f: User => Request[AnyContent] => Result) = withAuth { email => request =>
    scala.concurrent.blocking {
      DAO.getUser(email).map(user => f(user)(request)).getOrElse(onUnauthorized(request))
    }
  }
  def withAsyncUser[A](p: BodyParser[A])(f: User => Request[A] => Future[Result]) = withAsyncAuth(p) { email => request =>
    scala.concurrent.blocking {
      DAO.getUser(email).map(user => f(user)(request)).getOrElse(Future(onUnauthorized(request)))
    }
  }
}
