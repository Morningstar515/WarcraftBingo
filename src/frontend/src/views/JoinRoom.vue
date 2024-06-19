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
                <button class="bg-blue-500 w-36 h-10 text-white rounded-md shadow-md hover:bg-blue-400" @click="connect('JOIN', roomCode, username, this.boardType)">Join Room</button>
            </td>
        </tr>
    </table>
</template>


<script>
import socket from '../socket.js'
export default {
    name:'JoinRoom',
    data() {
        return {
            roomCode: "",
            username: "",
            boardType: "",
        }
    },
    methods: {
        async connect(action, roomCode, username, boardType) {
            await this.getBoard(roomCode)
            socket.connect(action,roomCode,username,boardType);
            console.log(this.boardType)
            this.$router.push({ name: 'WarcraftBingoboard', query: { roomCode: this.roomCode, username: this.username, boardType: this.boardType} });
        },
        async getBoard(roomCode) {
            return new Promise((resolve) => {
                console.log(roomCode)
                fetch("http://localhost:8080/getBoardType", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        roomCode: roomCode,
                    })
                })
                    .then((res) => res.text())
                    .then((data) => {
                        console.log(data)
                        this.boardType = data
                        resolve()
                    })
            })

        }

    }

}
</script>
