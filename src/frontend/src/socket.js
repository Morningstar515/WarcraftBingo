class WebSocketController{
    /* eslint-disable vue/no-unused-components */

    constructor(){
        this.socket = null;
        this.listeners = [];
    }

    connect(action, roomCode, username) {
        if (this.socket) {
            return;
        }

        this.socket = new WebSocket("ws://localhost:8080/websocket");

        socket.onopen = () => {
            const joinMessage = JSON.stringify({ type: action, roomCode: roomCode, username: username });
            console.log('opened connection')
            socket.send(joinMessage);
            this.$router.push({ name: 'WarcraftBingoboard', query: { roomCode: roomCode, username: username } });
        };

        this.socket.onmessage = (event) => {
            const message = JSON.parse(event.data);
            this.notifyListeners(message);
        };

        this.socket.onerror = (error) => {
            console.error('WebSocket error:', error);
        };

        this.socket.onclose = () => {
            console.log('WebSocket connection closed');
            this.socket = null;
        };
    }

    addListener(listener) {
        this.listeners.push(listener);
    }

    removeListener(listener) {
        this.listeners = this.listeners.filter((l) => l !== listener);
    }

    notifyListeners(message) {
        this.listeners.forEach((listener) => listener(message));
    }
}
const socket = new WebSocketController();
export default socket;
