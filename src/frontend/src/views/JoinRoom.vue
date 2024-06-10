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
import socket from '../socket.js'
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
            socket.connect(action,roomCode,username);
            this.$router.push({ name: 'WarcraftBingoboard', query: { roomCode: this.roomCode, username: this.username } });
        },

    }

}
</script>
