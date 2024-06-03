<template>
    <button class="board-tile h-full w-full text-center mt-2" @click="$emit('click', $event)">
    {{ tileContent }}
    </button>
</template>

<script>
export default {
    data(){
        return{
            tileContent: ""
        }
    },
    mounted(){
    fetch("http://localhost:8080/test")
    .then((res) => res.text())
    .then((data) => {
      this.tileContent = data;
    })
  }
}
</script>