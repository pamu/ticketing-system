function comment(ticketId) {
    var comment = $("#comment_box").val().trim();
    if (comment.length > 0) {
        var comment = {
                'ticketId' : ticketId,
                'comment' : comment
             }
             jsRoutes.controllers.Application.comment(commenterId, ticketId).ajax({
                data: JSON.stringify(comment),
                contentType: "application/json",
                success: function(data) {
                    console.log(data);
                    if (data.success) {
                        console.log(data);
                    }

                    if (data.failure) {
                        console.log(data);
                    }
                }
             });
    } else {
        $("#msg_center").html('<span class="alert alert-error">Empty Comment</span>');
    }
}