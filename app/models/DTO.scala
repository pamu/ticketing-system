package models

import java.sql.Timestamp

/**
 * Created by pnagarjuna on 22/05/15.
 */
object DTO {

  case class User(email: String, password: String, timestamp: Timestamp, id: Option[Long] = None)

  case class Customer(email: String, timestamp: Timestamp, id: Option[Long] = None)

  case class Ticket(authorId: Long, customerId: Long, assignedTo: Option[Long], name: String, desc: String, status: Int,
                    timestamp: Timestamp, id: Option[Long] = None)

  case class Comment(commenterId: Long, ticketId: Long, comment: String, timestamp: Timestamp, id: Option[Long] = None)

}