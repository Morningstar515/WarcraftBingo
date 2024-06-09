<template>
    <div class="flex flex-col w-1/2 h-1/2 shadow-lg border justify-evenly items-center rounded-3xl">
        <div class="flex justify-evenly w-full">
            <router-link class="bg-blue-500 w-48 h-10 text-white rounded-md shadow-md hover:bg-blue-400 flex justify-center items-center" to="/newroom">Start New Room</router-link>
            <router-link class="bg-blue-500 w-48 h-10 text-white rounded-md shadow-md hover:bg-blue-400 flex justify-center items-center" to="/JoinRoom" >Join Room</router-link>
        </div>
    </div>
</template>

<script>
export default {
    name: 'HomePage',
    data() {
        return {
            code: "",
            currentValue: "",
        };
    },

};
</script>


