var ws;

function connect() {
    var username = document.getElementById("username").value;
    var wsserver = document.getElementById("wsEndpoint").value;

    if (!username) {
        alert("Please enter a username!");
        return;
    }

    var url = wsserver + username;
    ws = new WebSocket(url);

    ws.onopen = function(event) {
        var log = document.getElementById("log");
        log.innerHTML += "Connected to " + event.currentTarget.url + "\n";
        document.getElementById("msg").focus();
    };

    ws.onmessage = function(event) {
        var log = document.getElementById("log");
        log.innerHTML += "Message from server: " + event.data + "\n";
        log.scrollTop = log.scrollHeight; // Auto-scroll to bottom
    };

    ws.onclose = function(event) {
        var log = document.getElementById("log");
        log.innerHTML += "Disconnected from server\n";
        alert("Disconnected from the server.");
    };

    ws.onerror = function(event) {
        var log = document.getElementById("log");
        log.innerHTML += "Error: " + event.message + "\n";
        alert("WebSocket Error: Check console for details.");
    };
}

function send() {
    var content = document.getElementById("msg").value;
    if (ws && ws.readyState === WebSocket.OPEN) {
        ws.send(content);
        document.getElementById("msg").value = ""; // Clear the message input
    } else {
        alert("WebSocket is not connected!");
    }
}

function sendCommand(command) {
    if (ws && ws.readyState === WebSocket.OPEN) {
        ws.send(command);
    } else {
        alert("WebSocket is not connected!");
    }
}