<template>
    <div class="flex h-full w-full items-center align-middle">
        <div class="flex flex-col mr-10 ml-10 shadow-md rounded-xl w-1/6 border h-2/3 float-start">
            <p class="flex shadow-md rounded-xl w-full justify-center mb-3 border">Room Members:</p>
            <p v-for="item in members" :key="item" class="flex shadow-md rounded-xl w-full justify-center mb-3 border">
                {{ item }}
            </p>
        </div>
        <div class="grid grid-cols-1 grid-rows-auto w-2/3 h-2/3">
            <div class="row flex border shadow-md" :data-row-index="rowIndex" v-for="(row, rowIndex) in board"
                :key="rowIndex">
                <div class="cell flex border shadow-md h-full w-full justify-center items-center"
                    :data-col-index="cellIndex" v-for="(cell, cellIndex) in row " :key="cellIndex">
                    <boardTile class="flex justify-center items-center text-center"
                        v-if="!(rowIndex === 2 && cellIndex === 2)" @click="placePiece" />
                    <p class="flex justify-center items-center text-center text-2xl" v-else>FREE SPACE</p>
                </div>
            </div>
            <button class="mt-10 border shadow-md w-1/2 h-10 justify-self-center"
                v-if="this.checkWin(this.board, this.boardSpaces)">Declare Bingo</button>
        </div>
    </div>
</template>

<script>
import boardTile from '../components/BoardTile.vue';
import {ref} from 'vue'

export default {
    name: 'WarcraftBingoboard',
    
    setup() {
        const members = ref([]);

        // Fetch room members when the component is mounted
        const fetchRoomMembers = async (code) => {
            try {
                const response = await fetch('http://localhost:8080/members', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ roomCode: code })
                });
                const data = await response.json();
                members.value = data;
                console.log(data)
            } catch (error) {
                console.error('Error fetching room members:', error);
            }

        };

        return {
            members,
            fetchRoomMembers,
        };
    },

    data() {
        return {
            board: this.generateBoard(5, 5),
            boardSpaces: [22],
            code: this.roomCode,
        };
    },
    props: {
        roomCode: {
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
            console.log(this.boardSpaces);
            e.stopImmediatePropagation();
        },
        winWarning(roomCode) {
            fetch("http://localhost:8080/warning", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    roomCode: roomCode,
                    message: "I have a bingo!",
                })
            })
            .then((res) => res.text())
            .then(() => {
                //
            })
        },
        checkWin(board, chosenSpots) {
            const n = board.length;
            const center = Math.floor(n / 2);

            const allChosen = (arr) => {
                if(arr.every(spot => chosenSpots.includes(spot))){
                    this.winWarning(this.code)
                    return true;
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
    mounted() {
        console.log('mount')
        // Fetch room members when the component is mounted
        this.fetchRoomMembers(this.roomCode);
    },
    addMember(member){
            this.members.value.push(member);
        }
    
};
</script>
