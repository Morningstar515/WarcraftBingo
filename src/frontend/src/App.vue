

<template>
  <div class="flex w-full h-screen shadow-md justify-center items-center">
    <Gameboard/>
  </div>
</template>

<script>
import Gameboard from './components/WarcraftBingoBoard.vue'
export default {
  name: 'App',
  data() {
    return {
      Container: 'Container',
    }
  },
  components: {
    Gameboard,
  }
}
</script>

