<template>
    <div class="grid w-2/3 h-2/3 border shadow-md">
        <div class="row flex border shadow-md" :data-row-index="rowIndex" v-for="(row,rowIndex) in board" :key="rowIndex">
            <div class="cell flex border shadow-md h-full w-full justify-center items-center" :data-col-index="cellIndex" v-for="(cell,cellIndex) in row " :key="cellIndex">
                <boardTile class="flex justify-center items-center text-center" v-if="!(rowIndex === 2 && cellIndex === 2)" @click="placePiece"/>
                <p class="flex justify-center items-center text-center text-2xl" v-else>FREE SPACE</p>
            </div>
        </div>
    </div>
</template>


<script>
import boardTile from './BoardTile.vue';
export default {
    data() {
        return{
            name: 'Gameboard',
            board: this.generateBoard(5,5),
            boardSpaces: [],
        }
    },
    
    components: {
        boardTile,
    },

    methods: {
        generateBoard(rows, cols) {
            const board = [];
            for (let i = 0; i < rows; i++) {
                const row = [];
                for (let j = 0; j < cols; j++) {
                    row.push(`${i}${j}`); 
                }
                board.push(row);
            }
            return board;
        },

        placePiece(e){
            const x = e.target.parentNode.parentNode.dataset.rowIndex;
            const y = e.target.parentNode.dataset.colIndex;

            const cords = x + y
            const piece = document.createElement('button');

            piece.classList.add('bg-red-600','h-20','w-20','rounded-full','absolute', 'hover:opacity-20');
            piece.addEventListener('click',() => {
                piece.parentNode.removeChild(piece.parentNode.lastChild)
            })

            e.target.parentNode.appendChild(piece);
            e.stopImmediatePropagation();

            this.boardSpaces.push(cords)
            this.checkWin(cords)

        },
        checkWin(cords){
            console.log(cords);
            console.log(this.boardSpaces)
        }
    }
}
</script>