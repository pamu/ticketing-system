$(function() {
    if (!!window.EventSource) {
             var source = new EventSource("/notifications");
             source.addEventListener('message', function(e) {
                var data = JSON.parse(e.data);
                $("#notifications").html('<center><span class="alert alert-success"> ' + data.msg + ' </span></center>')
             });
           } else {
             $("#notifications").html("Sorry. This browser doesn't seem to support Server sent event. Check <a href='http://html5test.com/compare/feature/communication-eventSource.html'>html5test</a> for browser compatibility.");
           }
});