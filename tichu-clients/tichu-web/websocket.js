var ws;

function connect() {
    var username = document.getElementById("username").value;
    var host = document.location.host;
    ws = new WebSocket("ws://" +host + "/chat/" + username);

    ws.onmessage = function(event) {
    var log = document.getElementById("log");
        console.log(event.data);
        var message = JSON.parse(event.data);
        log.innerHTML += message.from + " : " + message.content + "\n";
    };
}

function send() {
    var content = document.getElementById("msg").value;
    var json = JSON.stringify(
    {
        "type":"chat",
        "content":content
    }
    );

    ws.send(json);
}

function other() {
    var content = document.getElementById("msg").value;
    var json = JSON.stringify(
    {
        "type":"other",
        "thing":content
    }
    );

    ws.send(json);
}