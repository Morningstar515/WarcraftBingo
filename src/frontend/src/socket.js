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
        this.boardType = ""
    }

    connect(action, roomCode, username, boardType) {
        return new Promise((resolve, reject) => {
            this.socket = new WebSocket("ws://localhost:8080/websocket");

            this.socket.onopen = () => {
                const joinMessage = JSON.stringify({ type: action, roomCode: roomCode, username: username, boardType: boardType });
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
                console.log(message)

                console.log(typeof(message))
                let json = "";
                try{
                    json = JSON.parse(message)


                    if('Room' in json){
                    
                        this.boardType = json.BoardType.string
                        console.log(this.boardType)
                        this.updateMembers(JSON.parse(json.Usernames.string))
                    }
    
                    else if('Win' in json && json.Win.valueType === 'TRUE'){
                        let username = json.Username.string
                        let message = json.Message.string
    
                        const container = document.getElementById('modalContainer')
                        if (container.hasChildNodes()) {
                            container.removeChild(container.firstChild)
                        }
                        const app = createApp(WinModal, { username,  message})
                        app.mount(container)
                    }
    
                    //Warning logic
                    else{
                        console.log(json.Win.valueType)
                        console.log(json.Win.valueType === 'TRUE')
    
                        let username = json.Username.string
                        let message = json.Message.string
    
                        const container = document.getElementById('modalContainer')
                        if (container.hasChildNodes()) {
                            container.removeChild(container.firstChild)
                        }
    
                        const app = createApp(WarningModal, { username, message })
                        app.mount(container)
                        this.currentMessage = json.Message.string
                    }


                }
                catch(error){
                    console.log('Not json string')
                }
                resolve()
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
