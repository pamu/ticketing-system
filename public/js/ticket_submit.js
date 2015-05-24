function ticketPost() {
        var customerId = parseInt($("#customer_id").val().trim());
                if (!isNaN(customerId)) {
                    var heading = $("#heading").val().trim();
                    console.log(heading)
                    if (heading.length > 0) {
                        var desc = $("#desc").val().trim();
                        if (desc.length > 0) {
                            var assignTo = parseInt($("#assignedToId").val().trim());
                            if (! isNaN(assignTo)) {
                                var json = {
                                    'customerId' : customerId,
                                    'heading' : heading,
                                    'desc' : desc,
                                    'assignedToId' : assignTo
                                }
                                post(json);
                            } else {
                                var json = {
                                    'customerId' : customerId,
                                    'heading' : heading,
                                    'desc' : desc,
                                    'assignedToId' : NaN
                                }
                                post(json);
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
    }

    function post(json) {
                                    console.log(json);
                                    jsRoutes.controllers.Application.newticketSubmit().ajax({
                                        data: JSON.stringify(json),
                                        contentType: "application/json",
                                        success: function(data) {
                                            if (data.success) {
                                                $("#new_ticket").html('<span class="alert alert-info">Ticket Submitted Successfully</span>')
                                                $("#msg_center").html('<center><span class="alert alert-success">' + data.success + ' added</span></center>')
                                            }
                                            if (data.failure) {
                                                $("#msg_center").html('<span class="alert alert-error">' + JSON.stringify(data.failure) + '</span>')
                                            }
                                            if (data.errors) {
                                                $("#msg_center").html('<span class="alert alert-error">' + JSON.stringify(data.errors)+ '</span>')
                                            }
                                        }
                                    });
                                }