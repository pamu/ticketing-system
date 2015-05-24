package global

import actors.NHandler
import akka.actor.{Props, ActorSystem}
import models.DAO
import play.api.{Logger, Application, GlobalSettings}

/**
 * Created by pnagarjuna on 22/05/15.
 */
object Global extends GlobalSettings {
  lazy val system = ActorSystem("AkkaSystem")
  lazy val nhandler = system.actorOf(Props[NHandler], "nhandler")

  override def onStart(app: Application): Unit = {
    super.onStart(app)
    Logger.info("Ticket System started ...")
    //DAO.clean
    DAO.init
    //DAO.addCustomers
  }
  override def onStop(app: Application): Unit = {
    super.onStop(app)
    nhandler ! NHandler.Stop
    Logger.info("Ticket System Stopped.")
  }
}
