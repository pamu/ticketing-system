$(function() {
    $("#new_ticket_submit").click(function() {
        var userId = $("#user_id").val();
        if (userId.length > 0) {
            var heading = $("#heading").val();
            if (heading.length > 0) {
                var desc = $("#desc").val();
                if (desc.length > 0) {
                    var assignTo = $("#assignTo").val();
                    if (assignTo.length > 0) {

                    } else {

                    }
                    jsRoutes.controllers.Application.newticketSubmit().ajax({
                                                data: ,
                                                contentType: "application/json",
                                                success: function(data) {

                                                }
                                            });
                } else {
                    $("#desc_msg").html('<span class="alert alert-error">Enter valid Desc<span>')
                }
            } else {
                $("#heading_msg").html('<span class="alert alert-error">Enter valid Heading<span>')
            }
        } else {
            $("#user_id_msg").html('<span class="alert alert-error">Enter valid user id<span>')
        }
    });
});