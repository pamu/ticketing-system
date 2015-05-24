package actors

import akka.actor.{ActorLogging, Actor}
import play.api.libs.iteratee.{Enumeratee, Concurrent}
import play.api.libs.json.{Json, JsValue}

import play.api.libs.concurrent.Execution.Implicits.defaultContext
/**
 * Created by pnagarjuna on 24/05/15.
 */
object NHandler {
  case class Message(to: Long, msg: String)
  case object Stream
  case object Stop
  def filter(userId: Long) = Enumeratee.filter[JsValue]{  json: JsValue =>
    userId == (json\ "to").as[Long]
  }
}

class NHandler extends Actor with ActorLogging {
  import NHandler._
  val (en, out) = Concurrent.broadcast[JsValue]
  override def receive = {
    case Message(to, msg) =>
      log.info("{}", Message(to, msg))
      out.push(Json.obj("to" -> to, "msg" -> msg))
    case Stream => sender ! en
    case Stop =>
      out end()
      context stop self
  }
}
