<template lang="">
    <div>
        <div v-if="isVisible" class="absolute bg-green-500 text-white p-4 rounded-md shadow-md top-20">
            {{username}} {{message}}
          </div>
    </div>
</template>
<script>
export default {
    name: "WinModal",
    data(){
        return{
            isVisible: true
        }
    },
    props: {
        username: {
            type: String,
            required: true,
        },
        message: {
            type: String,
            required: true,
        }
    },
}
</script>
