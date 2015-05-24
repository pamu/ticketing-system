var current = 1;
function load(page) {
    console.log("load called " +page)
    jsRoutes.controllers.Application.listTickets(page).ajax({
        success: function(data) {
            console.log(data)
            if (data.success) {
                var list = data.success;
                var tags = ''
                for(var i = 0; i < list.length; i ++) {
                    var status = "new";
                    var action = "close";
                    if (list[i].status == 2) {
                        status = "open";
                    }
                    if (list[i].status == 3) {
                        status = "close";
                        action = "closed";
                    }
                    tags = tags + '<tr id="'+ list[i].id +'"> <th id="id"> ' + list[i].id+ '</th> <th id="heading">'+ list[i].heading+'</th> <th id="desc"> ' + list[i].desc + '</th> <th id="customerEmail"> ' + list[i].customerEmail + ' </th> <th id="userEmail"> ' + list[i].userEmail + '</th> <th id="status"> ' + status + '</th> <th id="edit"><a id="editButton" class="btn" href="/editticket/' + list[i].id + '">Edit</a></th> <th id="action" onclick="closeTicket(' + list[i].id + ');">' + action +'</th></tr>';
                }
                var tb = $("#table_body").html(tags);
            }
            if (data.failure) {
                counter = 1;
                $("#msg_center").html('<span class="alert alert-error">' + data.failure + '</span>');
            }
        }
    });
}

function closeTicket(ticketId) {
    jsRoutes.controllers.Application.closeTicket(ticketId).ajax({
        success: function(data) {
            if (data.success) {
                $('#table_body #' + ticketId + ' #action').html('closed');
                $('#table_body #' + ticketId + ' #status').html('close')
            }
            if (data.failure) {
                $("#msg_center").html('<span class="alert alert-error"> ' + data.failure + '</span>')
            }
        }
    });
}