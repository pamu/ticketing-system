$(function() {
    $("#new_ticket_submit").click(function() {
        var userId = parseInt($("#user_id").val().trim());
        if (!isNaN(userId)) {
            var heading = $("#heading").val().trim();
            console.log(heading)
            if (heading.length > 0) {
                var desc = $("#desc").val().trim();
                if (desc.length > 0) {
                    var assignTo = parseInt($("#assignToId").val().trim());
                    if (! isNaN(assignTo)) {
                        var json = {
                            'userId' : userId,
                            'heading' : heading,
                            'desc' : desc,
                            'assignedToId' : assignTo
                        }
                        post(json);
                    } else {
                        var json = {
                            'userId' : userId,
                            'heading' : heading,
                            'desc' : desc,
                            'assignedToId' : NaN
                        }
                        post(json);
                    }
                    function post(json) {
                        console.log(json);
                        jsRoutes.controllers.Application.newticketSubmit().ajax({
                            data: JSON.stringify(json),
                            contentType: "application/json",
                            success: function(data) {
                                if (data.success) {
                                    alert(data.success);
                                    console.log(JSON.stringify(data));
                                } else {
                                    alert(data.failure);
                                    console.log(JSON.stringify(data));
                                }
                            }
                        });
                    }

                } else {
                    $("#msg_center").html('<span class="alert alert-error">Enter valid Desc<span>')
                    return;
                }
            } else {
                $("#msg_center").html('<span class="alert alert-error">Enter valid Heading<span>')
                return;
            }
        } else {
            $("#msg_center").html('<span class="alert alert-error">Enter valid user id<span>')
            return;
        }
    });
});