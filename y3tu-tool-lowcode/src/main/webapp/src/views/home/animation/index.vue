<template>
    <lottie :options="defaultOptions" :width="width" :height="height" v-on:animCreated="handleAnimation"/>
</template>

<script>
    /**
     * https://lottiefiles.com/ 在这上面查找动画素材
     */
    import lottie from 'vue-lottie'
    import animationJson1 from './animation1.json'
    import animationJson2 from './animation2.json'
    import animationJson3 from './animation3.json'
    import animationJson4 from './animation4.json'
    import animationJson5 from './animation5.json'

    export default {
        name: 'lottie-home',
        props: {
            width: {
                type: Number,
                default: 600,
            },
            height: {
                type: Number,
                default: 500,
            },
            type: {
                type: Number,
                default: 5,
            },
        },
        components: {lottie},
        data() {
            return {
                defaultOptions: {
                    animationData: ''
                },
                animationSpeed: 1,
                anim: {}
            }
        },
        created() {
            let animationData;
            switch (this.type) {
                case 1:
                    animationData = animationJson1;
                    break
                case 2:
                    animationData = animationJson2;
                    break
                case 3:
                    animationData = animationJson3;
                    break
                case 4:
                    animationData = animationJson4
                    break
                case 5:
                    animationData = animationJson5;
                    break
                default:
                    animationData = animationJson5;
                    break
            }
            this.defaultOptions = {
                animationData: animationData
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