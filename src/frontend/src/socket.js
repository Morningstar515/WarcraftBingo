import { reactive } from 'vue';
import { createApp } from 'vue';
import WarningModal from './components/WarningModal.vue';
import WinModal from './components/WinModal.vue' ;
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

                if(Array.isArray(JSON.parse(message))){
                    this.updateMembers(JSON.parse(message))
                }

                else if(message.slice(message.length-20,message.length-17) === "has"){
                    let content = message.slice(4,message.length)
                    this.currentMessage = content


                    const container = document.getElementById('modalContainer')
                    if (container.hasChildNodes()) {
                        container.removeChild(container.firstChild)
                    }
                    const app = createApp(WinModal, { username, message })
                    app.mount(container)
                    this.currentMessage = message
                }

                //Warning logic
                else{
                    console.log(typeof(message))

                    let username = this.username
                    const container = document.getElementById('modalContainer')
                    if (container.hasChildNodes()) {
                        container.removeChild(container.firstChild)
                    }
                    const app = createApp(WarningModal, { username, message })
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
