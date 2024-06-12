import { reactive } from 'vue';
import { createApp } from 'vue';
import WinnerModal from './components/WinnerModal.vue';
class WebSocketController{
    /* eslint-disable vue/no-unused-components */

    constructor(){
        this.socket = null;
        this.listeners = [];
        this.members = reactive([]);
        this.currentMessage = ""
    }

    connect(action, roomCode, username) {
        return new Promise((resolve, reject) => {
            this.socket = new WebSocket("ws://localhost:8080/websocket");

            this.socket.onopen = () => {
                const joinMessage = JSON.stringify({ type: action, roomCode: roomCode, username: username });
                console.log('WebSocket connection opened');
                this.socket.send(joinMessage);
                resolve(); 
            };

            this.socket.onerror = (error) => {
                console.error('WebSocket error:', error);
                reject(error); 
            };

            this.socket.onclose = () => {
                console.log('WebSocket connection closed');
                this.socket = null;
            };

            this.socket.onmessage = (event) => {
                const message = event.data;
                console.log(typeof(message))
                console.log(JSON.parse(message))

                if(Array.isArray(JSON.parse(message))){
                    this.updateMembers(JSON.parse(message))
                }

                else if(message.slice(0,2) === "Win"){
                    let content = message.slice(4,message.length)
                    this.currentMessage = content
                    console.log('win con')
                    console.log(content)
                    // Win logic alert here
                }

                //Warning logic
                else{

                    let username = this.username
                    const container = document.getElementById('modalContainer')
                    if (container.hasChildNodes()) {
                        container.removeChild(container.firstChild)
                    }
                    const app = createApp(WinnerModal, { username, message })
                    app.mount(container)
                    this.currentMessage = message
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
