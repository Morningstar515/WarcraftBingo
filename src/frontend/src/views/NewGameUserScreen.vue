<template lang="">
    <table id='table' class="flex flex-col w-1/2 h-1/2 shadow-lg border justify-center gap-4 items-center rounded-3xl">
        <tr class="flex w-1/2">
            <td>
                <p class="">Enter Username:</p>
                <input v-model="username" class="border rounded-md h-10 shadow-md" placeholder="Username">
            </td>
            <td class="pl-4">
                <br>
                <button class="bg-blue-500 w-48 h-10 text-white rounded-md shadow-md hover:bg-blue-400" @click="generateNewCode(); checkBoardType()" >Start New Room</button>
            </td>
        </tr>
        <br>
        <p>Select Raid Board:</p>

        <tr class="flex w-1/2 h-1/3 gap-6">
 
            <div class="flex flex-col w-1/3 rounded-md items-center transform transition-all hover:scale-125">
                <button class="flex h-full w-full shadow-md border rounded-md focus:ring-green-500 focus:ring-4 focus:outline-2 focus:border-transparent" @click="this.boardType ='getMC'">
                    <img src="../assets/MC.jpeg" alt="" class="rounded-md">
                </button>
                <br>
                <p>Molten Core</p>   
            </div>
            <div class="flex flex-col w-1/3 rounded-md items-center transform transition-all hover:scale-125">
                <button class="flex h-full w-full shadow-md border rounded-md focus:ring-green-500 focus:ring-4 focus:outline-2 focus:border-transparent" @click="this.boardType ='getBWL'">
                    <img src="../assets/BWL.jpg" alt="" class="rounded-md ">
                </button>
                <br>
                <p>Blackwing Lair</p>   
            </div>
            <div class="flex flex-col w-1/3 rounded-md items-center transform transition-all hover:scale-125">
                <button class="flex h-full w-full shadow-md border rounded-md focus:ring-green-500 focus:ring-4 focus:outline-2 focus:border-transparent" @click="this.boardType ='getZG' ">
                    <img src="../assets/ZG.webp" alt="" class="rounded-md">
                </button>
                <br>
                <p>Zul'Gurub</p>   
            </div>          
        </tr>
    </table>

</template>
<script>
import socket from '../socket.js'
export default {
    name: "NewGameUsername",
    data() {
        return {
            username: "",
            boardType: ""
        }
    },

    methods: {
        async generateNewCode() {
            const res = await fetch("http://localhost:8080/generateRoomCode");
            const data = await res.text();
            this.code = data;
            await socket.connect("START", this.code, this.username,this.boardType)
            if(this.boardType != ""){
                this.$router.push({ name: 'WarcraftBingoboard', query: { roomCode: this.code, username: this.username, boardType: this.boardType} });
            }
        },

        checkBoardType(){
            const alert = document.getElementById('table')
            let ele = document.createElement('p')
            ele.classList.add('text-red-500')
            ele.innerText = 'Must choose board!'
            alert.appendChild(ele)
        }

    },
    
}
</script>
