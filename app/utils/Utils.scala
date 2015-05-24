package utils

import actors.NHandler
import global.Global
import models.DAO

/**
 * Created by pnagarjuna on 25/05/15.
 */
object Utils {
  def notify(userId: Long, userEmail: String, ticketId: Long): Unit = {
    DAO.getTicketAuthorIdAndHeading(ticketId).map(tuple => {
      val (id, heading) = tuple
      if (userId != id) {
        Global.nhandler ! NHandler.Message(id, s"""${userEmail} commented on ticket "${heading}" started by you.""")
      }
    })
  }
}
