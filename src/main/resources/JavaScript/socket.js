function openWebSocketConnection() {
    let socket = new WebSocket("ws://localhost:8080/websocket");

    socket.onopen = function(event) {
        console.log("Connection opened");
        socket.send("Hello, WebSocket!");
    };

    socket.onmessage = function(event) {
        console.log("Received: " + event.data);
    };

    socket.onclose = function(event) {
        console.log("Connection closed");
    };

    socket.onerror = function(error) {
        console.log("Error: " + error.message);
    };

    return socket;
}

// Example usage
let mySocket = openWebSocketConnection();

