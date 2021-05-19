<template>
    <lottie :options="defaultOptions" :width="800" :height="600" v-on:animCreated="handleAnimation"/>
</template>

<script>
    /**
     * https://lottiefiles.com/ 在这上面查找动画素材
     */
    import lottie from 'vue-lottie'
    import animationJson from './404.json'

    export default {
        name: 'lottie-home',
        components: {lottie},
        data() {
            return {
                defaultOptions: {animationData: animationJson},
                animationSpeed: 1,
                anim: {}
            }
        },
        methods: {
            handleAnimation: function (anim) {
                this.anim = anim;
                //console.log(anim); //这里可以看到 lottie 对象的全部属性
            },
        }

    }

</script>

<style scoped>

</style>