<template>
    <div class="sorry">
        <img src="@/assets/images/sorry.png">
        <span class="text">抱歉，开发中...</span>
    </div>
</template>

<script>
    export default {
        name: 'developing'
    }

</script>

<style scoped>
    .sorry {
        height: 500px;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
    }

    .text {
        font-size: 20px;
        margin: 20px 0;
    }

</style>