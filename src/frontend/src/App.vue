<template>
  <h1 class="float-left m-4">Warcraft Bingo</h1>
  <div class="flex w-full h-screen shadow-md justify-center items-center">
    <router-view/>
  </div>
</template>

<script>
import Gameboard from './views/WarcraftBingoBoard.vue'
import Home from './views/HomePage.vue'

/* eslint-disable vue/no-unused-components */

export default {
  name: 'App',

  components: {
    Home,
    Gameboard,
  }
}
</script>

