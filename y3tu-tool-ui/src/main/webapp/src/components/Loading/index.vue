<style>
</style>
<template>
    <div>
        <vue-element-loading :active="show" spinner="bar-fade-scale" :is-full-screen="fullscreen" color="#FF6700">
            <img src="../../assets/images/pikaqiu.gif" width="55px" height="55px">
        </vue-element-loading>
        <span>
             加载中...
        </span>
    </div>
</template>
<script>
    import VueElementLoading from 'vue-element-loading'


    // 部分样式代码冗长，未作展示
    export default {
        name: "loading",
        props: {
            //是否全屏
            fullscreen: {
                type: Boolean,
                default: function () {
                    return false
                }
            },
            // 编辑器中呈现的内容
            codes: {
                type: String,
                default: function () {
                    return ''
                }
            },
            readOnly: {
                type: Boolean,
                default: function () {
                    return false
                }
            }
        },
        data() {
            return {
                show: true
            }

        },
        mounted() {

        },
        components: {
            VueElementLoading
        }
    }
</script>
