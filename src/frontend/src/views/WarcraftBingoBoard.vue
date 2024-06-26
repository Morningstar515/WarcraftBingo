<template>
    <div id="modalContainer" class="absolute flex justify-center top-10 w-1/2"></div>
    <div class="flex h-full w-full items-center align-middle">
        <div class="flex flex-col mr-10 ml-10 shadow-md rounded-xl w-1/6 border h-2/3 float-start">
            <p class="flex  rounded-xl w-full h-10 items-center justify-center mb-3 border">Room Members:</p>
            <ul v-for="item in members" :key="item" class="flex shadow-md rounded-xl w-full justify-center mb-1 border">
                <li :id="item" class="">{{ item }}</li>
            </ul>

        </div>
        <div class="grid grid-cols-1 grid-rows-auto w-2/3 h-2/3">
            <div class="row flex border shadow-md" :data-row-index="rowIndex" v-for="(row, rowIndex) in board"
                :key="rowIndex">
                <div class="cell flex border shadow-md h-full w-full justify-center items-center"
                :data-col-index="cellIndex" v-for="(cell, cellIndex) in row " :key="cellIndex">
                    <boardTile class="flex justify-center items-center text-center" :content="this.tilesContent[5*rowIndex + cellIndex]"
                        v-if="!(rowIndex === 2 && cellIndex === 2)" @click="placePiece"/>
                    <p class="flex justify-center items-center text-center text-2xl" v-else>FREE SPACE</p>
                </div>
            </div>
            <button class="mt-10 border shadow-md w-1/2 h-10 justify-self-center"
                v-if="this.checkWin(this.board, this.boardSpaces)" @click="this.winWarning(' has declared bingo!')">Declare Bingo</button>
        </div>
    </div>
</template>

<script>
import boardTile from '../components/BoardTile.vue';
import socket from '@/socket';

export default {
    name: 'WarcraftBingoboard',
    data() {
        return {
            board: this.generateBoard(5, 5),
            boardSpaces: [],
            code: this.roomCode,
            members: socket.members,
            tempIndex: 0,
            oneSpaceToWin: false,
            tilesContent: [],
            bingo: false
        };
    },
    props: {
        roomCode: {
            type: String,
            required: true
        },
        username: {
            type: String,
            required: true
        },
        boardType: {
            type: String,
            required: true
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


        placePiece(e) {
            const x = e.target.parentNode.parentNode.dataset.rowIndex;
            const y = e.target.parentNode.dataset.colIndex;
            const cords = x + y;

            const piece = document.createElement('button');
            piece.classList.add('bg-red-600', 'h-20', 'w-20', 'rounded-full', 'absolute', 'hover:opacity-20');

            piece.addEventListener('click', () => {
                piece.parentNode.removeChild(piece.parentNode.lastChild);

                for (let i = 0; i < this.boardSpaces.length; i++) {
                    if (this.boardSpaces[i] === cords) {
                        this.boardSpaces.splice(i, 1);
                    }
                }
            });

            e.target.parentNode.appendChild(piece);
            this.boardSpaces.push(cords);
            e.stopImmediatePropagation();
        },


        winWarning(message) {
            console.log(this.bingo)
                fetch("http://localhost:8080/warning", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        roomCode: this.roomCode,
                        username: this.username,
                        message: message,
                        bingo: this.bingo
                    })
                })
                    .then((res) => res.text())
                    .then(() => {

                        //To avoid spamming
                    })
            

        },



        checkWin(board, chosenSpots) {

            const n = board.length;
            const center = Math.floor(n / 2);
            let oneSpot = false;
            
            const oneSpotAway = (arr) => {
                const countChosen = arr.filter(spot => chosenSpots.includes(spot)).length;      /* Come back to this  */
                if(countChosen === arr.length - 1){
                    this.oneSpaceToWin = true;
                    this.winWarning(" is 1 space away from bingo!")
                    return true;
                }
                else if(!countChosen === arr.length - 1 && this.oneSpaceToWin === true){
                    return true;
                }
                else{
                    return false
                }
            }

            const allChosen = (arr) => {
                const countChosen = arr.filter(spot => chosenSpots.includes(spot)).length; 
                if (countChosen === arr.length) {
                    this.bingo = true;
                    // All spots are chosen, indicating a win
                    return true;
                } 
                else if (countChosen === arr.length - 1) {

                    // One spot away from winning
                    oneSpot = oneSpotAway(arr);
                    this.oneSpaceToWin = oneSpot
                    console.log(oneSpot)
           
                }
            }


            for (let row = 0; row < n; row++) {
                if (row === center) {
                    const rowWithoutCenter = board[row].filter((_, col) => col !== center);
                    if (allChosen(rowWithoutCenter)) {
                        return true;
                    }
                } else {
                    if (allChosen(board[row])) {
                        return true;
                    }
                }
            }

            for (let col = 0; col < n; col++) {
                const column = board.map(row => row[col]);
                if (col === center) {
                    const columnWithoutCenter = column.filter((_, row) => row !== center);
                    if (allChosen(columnWithoutCenter)) {
                        return true;
                    }
                } else {
                    if (allChosen(column)) {
                        return true;
                    }
                }
            }

            const mainDiagonal = board.map((row, i) => row[i]);
            mainDiagonal.splice(center, 1);
            if (allChosen(mainDiagonal)) {
                return true;
            }

            const antiDiagonal = board.map((row, i) => row[n - 1 - i]);
            antiDiagonal.splice(center, 1);
            if (allChosen(antiDiagonal)) {
                return true;
            }

            return false;
        },

    },

    // Mount username when entering board && pull board data
    mounted(){
        document.getElementById('userHeader').innerText = "Playing as: " + this.username;
        console.log(this.username + " "+this.boardType)
        fetch("http://localhost:8080/" + this.boardType)
            .then((res) => res.text())
            .then((data) => {
                data = JSON.parse(data);

                for (let i = 0; i < 25; i++) {
                    let j = Math.floor(Math.random() * 25)
                    let temp = data[i];
                    data[i] = data[j];
                    data[j] = temp; 
                }
                this.tilesContent = data

            })
    }
}
</script>
 