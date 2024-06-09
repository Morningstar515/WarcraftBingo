<template lang="">

    <table class="flex flex-col w-1/2 h-1/2 shadow-lg border justify-center gap-4 items-center rounded-3xl">
        <tr class="flex w-1/2">
            <td>
                <p class="">Enter Username:</p>
                <input v-model="username" class="border rounded-md h-10 shadow-md" placeholder="Username">
            </td>

        </tr>
        <tr></tr>
        <tr class="flex w-1/2">
            <td>
                <p>Enter Room Code:</p>
                <input v-model="roomCode" class="border rounded-md h-10 shadow-md" placeholder="Room Code Here">
            </td>
            <td class="pl-4">
                <br>
                <button class="bg-blue-500 w-36 h-10 text-white rounded-md shadow-md hover:bg-blue-400" @click="connect('JOIN', roomCode, username)">Join Room</button>
            </td>
        </tr>
    </table>
</template>


<script>
export default {
    name:'JoinRoom',
    data() {
        return {
            roomCode: "",
            username: "",
        }
    },
    methods: {
        connect(action, roomCode, username) {
            let socket = new WebSocket("ws://localhost:8080/websocket");

            socket.onopen = () => {
                const joinMessage = JSON.stringify({ type: action, roomCode: roomCode, username:username });
                socket.send(joinMessage);
                this.$router.push({ name: 'WarcraftBingoboard', query: { roomCode: roomCode, username:username } });
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
        },
    }

}
</script>
