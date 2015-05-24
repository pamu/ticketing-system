function comment(ticketId) {
    var comment = $("#comment_box").val().trim();
    if (comment.length > 0) {
        var commentObj = {
                'ticketId' : ticketId,
                'comment' : comment
             }
             jsRoutes.controllers.Application.comment().ajax({
                data: JSON.stringify(commentObj),
                contentType: "application/json",
                success: function(data) {
                    console.log(data);
                    if (data.success) {
                        $("#comment_box").val('');
                        $('#comments').append('<div class="well"><label>Commented by you </label><div>' + comment + '</div></div>');
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