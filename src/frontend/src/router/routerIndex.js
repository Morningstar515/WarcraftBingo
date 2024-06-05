import { createRouter, createWebHistory } from "vue-router";
import HomePage from "@/views/HomePage.vue";
import WarcraftBingoBoard from "@/views/WarcraftBingoBoard.vue";

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            name: 'home',
            component: HomePage
        },
        {
            path: '/board',
            name: 'board',
            component: WarcraftBingoBoard
        },
    ]
})

export default router