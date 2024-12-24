var ws;

function connect() {
    var username = document.getElementById("username").value.trim();
    var wsserver = document.getElementById("wsserver").value.trim();

    if (!username || !wsserver) {
        alert("Please enter both WebSocket URL and Username.");
        return;
    }

    var url = wsserver + username;
    ws = new WebSocket(url);

    ws.onopen = function(event) {
        logMessage("Connected to " + event.currentTarget.url);
    };

    ws.onmessage = function(event) {
        logMessage("Server: " + event.data);
    };

    ws.onclose = function() {
        logMessage("Disconnected from server.");
    };

    ws.onerror = function(error) {
        logMessage("Error: " + error.message);
    };
}

function send() {
    var content = document.getElementById("msg").value.trim();
    if (ws && content) {
        ws.send(content);
        logMessage("You: " + content);
        document.getElementById("msg").value = '';
    } else {
        logMessage("Message not sent. Make sure you're connected and message is not empty.");
    }
}

function logMessage(message) {
    var log = document.getElementById("log");
    log.value += message + "\n";
    log.scrollTop = log.scrollHeight;  // Auto-scroll to the bottom
}