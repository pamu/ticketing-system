function commentLoader(ticketId, page) {
   jsRoutes.controllers.Application.listComments(ticketId, page).ajax({
      success: function(data) {
         if (data.success) {
            var list = data.success;
            for(var i = 0; i < list.length; i++) {
                $("#comments").append('<div class="well"><label>Commented by ' + list[i].commenter + '</label><div>' + list[i].comment + '</div></div>')
            }
         }
         if (data.failure) {
            $("#msg_center").html('<span class="alert alert-error">' + data.failure + '</span>');
         }
      }
   });
}