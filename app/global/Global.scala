package global

import play.api.{Logger, Application, GlobalSettings}

/**
 * Created by pnagarjuna on 22/05/15.
 */
object Global extends GlobalSettings {
  override def onStart(app: Application): Unit = {
    super.onStart(app)
    Logger.info("Ticket System started ...")
  }
  override def onStop(app: Application): Unit = {
    super.onStop(app)
    Logger.info("Ticket System Stopped.")
  }
}
