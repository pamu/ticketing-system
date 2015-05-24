function commentLoader(ticketId) {
   jsRoutes.controllers.Application.listComments(ticketId).ajax({
   });
}