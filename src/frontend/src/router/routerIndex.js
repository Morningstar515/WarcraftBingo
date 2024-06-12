import { createRouter, createWebHistory } from "vue-router";
import HomePage from "@/views/HomePage.vue";
import WarcraftBingoBoard from "@/views/WarcraftBingoBoard.vue";
import JoinRoom from '@/views/JoinRoom.vue'
import NewGameUserScreen from "@/views/NewGameUserScreen.vue";

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
            props: route => ({roomCode: route.query.roomCode, username: route.query.username})
        },
        {
            path: '/joinroom',
            name: 'JoinRoom',
            component: JoinRoom,
            

        },
        {
            path: '/newroom',
            name: 'NewGameUsername',
            component: NewGameUserScreen
        },
    ]
});

export default router;
