<template>
    <div class="grid grid-cols-1 grid-rows-auto w-2/3 h-2/3">
        <div class="row flex border shadow-md" :data-row-index="rowIndex" v-for="(row,rowIndex) in board" :key="rowIndex">
            <div class="cell flex border shadow-md h-full w-full justify-center items-center" :data-col-index="cellIndex" v-for="(cell,cellIndex) in row " :key="cellIndex">
                <boardTile class="flex justify-center items-center text-center" v-if="!(rowIndex === 2 && cellIndex === 2)" @click="placePiece"/>
                <p class="flex justify-center items-center text-center text-2xl" v-else>FREE SPACE</p>
            </div>
        </div>
        <button class="mt-10 border shadow-md w-1/2 h-10 justify-self-center" v-if="this.checkWin(this.board,this.boardSpaces)">Declare Bingo</button>
    </div>
</template>


<script>
import boardTile from './BoardTile.vue';
export default {
    data() {
        return{
            name: 'Gameboard',
            board: this.generateBoard(5,5),
            boardSpaces: [22],
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

                for (let i = 0; i < this.boardSpaces.length; i++) {
                    if(this.boardSpaces[i] === cords){
                        this.boardSpaces.splice(i,1)
                    }
                    console.log(this.boardSpaces)                    
                }
            })

            e.target.parentNode.appendChild(piece);

            this.boardSpaces.push(cords)

            console.log(this.boardSpaces)
            e.stopImmediatePropagation();

        },
        checkWin(board, chosenSpots) {
            const n = board.length;

            // Helper function to check if all elements of an array are in chosen spots
            function allChosen(arr) {
                return arr.every(spot => chosenSpots.includes(spot));
            }

            // Check rows
            for (let row = 0; row < n; row++) {
                if (allChosen(board[row])) {
                    return true;
                }
            }

            // Check columns
            for (let col = 0; col < n; col++) {
                const column = board.map(row => row[col]);
                if (allChosen(column)) {
                    return true;
                }
            }

            // Check main diagonal
            const mainDiagonal = board.map((row, i) => row[i]);
            mainDiagonal.splice(Math.floor(n / 2), 1); 
            if (allChosen(mainDiagonal)) {
                return true;
            }

            // Check anti-diagonal
            const antiDiagonal = board.map((row, i) => row[n - 1 - i]);
            antiDiagonal.splice(Math.floor(n / 2), 1); 
            if (allChosen(antiDiagonal)) {
                return true;
    }

            // No win condition met
            return false;
        }
    }
}
</script>