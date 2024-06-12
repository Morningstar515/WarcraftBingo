<template>
	<div class="flex flex-col">
		<h1 class="float-left m-2 pl-2">Warcraft Bingo</h1>
		<h1 id="userHeader" class="pl-4"></h1>
	</div>
	<div class="flex w-full h-screen shadow-md justify-center items-center">
		<router-view v-slot="{ Component }" >
			<transition enter-from-class="translate-x-[80%] opacity-0" enter-active-class="transition duration-500">
				<component :is="Component"/>
			</transition>
		</router-view>
	</div>
</template>

<script>
import Gameboard from './views/WarcraftBingoBoard.vue'
import Home from './views/HomePage.vue'
import JoinRoom from './views/JoinRoom.vue';
import NewGameUserScreen from './views/NewGameUserScreen.vue';

/* eslint-disable vue/no-unused-components */

export default {
	name: 'App',
	transitionName: "slide",
	component: "",


	components: {
		Home,
		JoinRoom,
		Gameboard,
		NewGameUserScreen
	},
	methods: {
		toggleJoin() {

		},
	}

}
</script>
