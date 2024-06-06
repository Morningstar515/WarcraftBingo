<template>
    <div class="flex flex-col w-1/2 h-1/2 shadow-lg border justify-evenly items-center rounded-3xl">
        <div>
            <button class="bg-blue-500 w-48 h-10 text-white rounded-md shadow-md hover:bg-blue-400" @click="generateNewCode">Start New Room</button>
        </div>
        <div class="flex w-full justify-evenly">
            <input v-model="currentValue" class="border w-1/2 rounded-md" placeholder="Room Code Here">
            <button class="bg-blue-500 w-48 h-10 text-white rounded-md shadow-md hover:bg-blue-400" @click="connect('JOIN', currentValue)">Join Room</button>
        </div>
    </div>
</template>

<script>
export default {
    name: 'HomePage',
    data() {
        return {
            code: "",
            currentValue: ""
        };
    },
    methods: {
        async generateNewCode() {
            const res = await fetch("http://localhost:8080/generateRoomCode");
            const data = await res.text();
            this.code = data;
            this.connect("START", this.code);
            
        },
        connect(action, roomCode) {
            let socket = new WebSocket("ws://localhost:8080/websocket");

            socket.onopen = () => {
                const joinMessage = JSON.stringify({ type: action, roomCode: roomCode });
                socket.send(joinMessage);
                this.$router.push({ name: 'WarcraftBingoboard', query: { roomCode: roomCode } });
            };

            socket.onmessage = (event) => {
                console.log("Received message:", event.data);
            };

            socket.onclose = () => {
                console.log("WebSocket connection closed");
            };

            socket.onerror = (error) => {
                console.log("WebSocket error:", error.message);
            };
        },
        created() {
        this.connect = this.connect.bind(this);
    }
    }
};
</script>
