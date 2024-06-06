import { createRouter, createWebHistory } from "vue-router";
import HomePage from "@/views/HomePage.vue";
import WarcraftBingoBoard from "@/views/WarcraftBingoBoard.vue";

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            name: 'home',
            component: HomePage,
            props: true
        },
        {
            path: '/board',
            name: 'WarcraftBingoboard',
            component: WarcraftBingoBoard,
            props: route => ({roomCode: route.query.roomCode})
        },
    ]
});

export default router;
