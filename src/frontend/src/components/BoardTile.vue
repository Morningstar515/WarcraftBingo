<template>
    <button class="board-tile h-full w-full text-center mt-2" @click="$emit('click', $event)" >
    {{ this.content }}
    </button>
</template>

<script>
export default {
	name: 'boardTile',
    data(){
        return{
            tileContent: this.content
        }
    },
    props: {
		content: {
			type: String,
			required: true,
		}
    },
	mounted(){
		console.log(this.content)
	}

}
</script>