<template lang="">

    <table class="flex flex-col w-1/2 h-1/2 shadow-lg border justify-center gap-4 items-center rounded-3xl">
        <tr class="flex w-1/2">
            <td>
                <p class="">Enter Username:</p>
                <input v-model="username" class="border rounded-md h-10 shadow-md" placeholder="Username">
            </td>
            <td class="pl-4">
                <br>
                <button class="bg-blue-500 w-48 h-10 text-white rounded-md shadow-md hover:bg-blue-400" @click="generateNewCode()">Start New Room</button>
            </td>

        </tr>
    </table>

</template>
<script>
import socket from '../socket.js'
export default {
    name: "NewGameUsername",
    data() {
        return {
            username: ""
        }
    },

    methods: {
        async generateNewCode() {
            const res = await fetch("http://localhost:8080/generateRoomCode");
            const data = await res.text();
            this.code = data;
            await socket.connect("START", this.code, this.username)
            this.$router.push({ name: 'WarcraftBingoboard', query: { roomCode: this.code, username: this.username } });
        },

    },
    
}
</script>
