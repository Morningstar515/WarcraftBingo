import { reactive } from 'vue';
class WebSocketController{
    /* eslint-disable vue/no-unused-components */

    constructor(){
        this.socket = null;
        this.listeners = [];
        this.members = reactive([]);
    }

    connect(action, roomCode, username) {
        return new Promise((resolve, reject) => {
            this.socket = new WebSocket("ws://localhost:8080/websocket");

            this.socket.onopen = () => {
                const joinMessage = JSON.stringify({ type: action, roomCode: roomCode, username: username });
                console.log('WebSocket connection opened');
                this.socket.send(joinMessage);
                resolve();  // Resolve the promise when connection is open
            };

            this.socket.onerror = (error) => {
                console.error('WebSocket error:', error);
                reject(error);  // Reject the promise on error
            };

            this.socket.onclose = () => {
                console.log('WebSocket connection closed');
                this.socket = null;
            };

            this.socket.onmessage = (event) => {
                const message = event.data;
                console.log(JSON.parse(message))
                if(Array.isArray(JSON.parse(message))){
                    this.updateMembers(JSON.parse(message))
                }
                else{
                    console.log(JSON.parse(message))
                    // Win logic alert here
                }
                this.notifyListeners(message);
            };
        });
        
    }
    updateMembers(members) {
        this.members.splice(0, this.members.length, ...members);
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
