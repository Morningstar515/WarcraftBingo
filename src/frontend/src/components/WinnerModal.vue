<template lang="">
    <transition name='fade'>
        <div v-if="isVisible" class="absolute bg-green-500 text-white p-4 rounded-md shadow-md top-20">
            {{username}} {{message}}
          </div>
    </transition>
</template>
<script>
export default {
    name: "WinnerModal",

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

    methods: {
        fadeOutDiv() {
            this.isVisible = false;
            setTimeout(() => {
            }, 2000);
        }
    },
    mounted() {
        setTimeout(() => {
            this.fadeOutDiv()
            }, 2000);

    },
    
}
</script>
<style scoped>
    .fade-enter-active, .fade-leave-active {
      transition: opacity 2s;
    }
    .fade-enter, .fade-leave-to /* .fade-leave-active in <2.1.8 */ {
      opacity: 0;
    }
    </style>
