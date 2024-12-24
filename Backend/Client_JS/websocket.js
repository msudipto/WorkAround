var ws;

function connect() {
    var username = document.getElementById("username").value.trim();
    var wsserver = document.getElementById("wsserver").value.trim();
    var chatType = document.getElementById("chatType").value;

    if (!username || !wsserver) {
        alert("Please enter both WebSocket URL and Username.");
        return;
    }

    var url = wsserver + chatType + "/" + username;
    ws = new WebSocket(url);

    ws.onopen = function(event) {
        logMessage("Connected to " + event.currentTarget.url);
    };

    ws.onmessage = function(event) {
        logMessage("Server: " + event.data);
    };

    ws.onclose = function() {
        //logMessage("Disconnected from server.");
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

function sendImage() {
    var imageInput = document.getElementById("imageUpload");
    if (ws && imageInput.files.length > 0) {
        var file = imageInput.files[0];
        if (file.size > 52428800) { // Check if file size is greater than 50MB
            logMessage("Image not sent. File size exceeds 50MB.");
            return;
        }
        var reader = new FileReader();
        reader.onload = function() {
            ws.send("/image " + reader.result);
            logMessage("You sent an image.");
        };
        reader.readAsDataURL(file);
    } else {
        logMessage("Image not sent. Make sure you're connected and an image is selected.");
    }
}

function logMessage(message) {
    var log = document.getElementById("log");
    if (message.startsWith("/image ")) {
        var img = document.createElement("img");
        img.src = message.substring(7); // Remove '/image ' prefix
        img.style.maxWidth = "100%";
        log.appendChild(img);
        log.appendChild(document.createElement("br"));
    } else {
        log.value += message + "\n";
        log.scrollTop = log.scrollHeight; // Auto-scroll to the bottom
    }
}