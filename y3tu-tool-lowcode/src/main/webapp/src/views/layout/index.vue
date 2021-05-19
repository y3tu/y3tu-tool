<template>
    <el-container>
        <el-header id="header">
            <Header/>
        </el-header>
        <el-main style="margin-top: 40px;">
            <div v-bind:style="{minHeight: Height+'px'}">
                <router-view v-slot="{ Component }" v-if="$route.meta.keepAlive">
                    <keep-alive>
                        <component :is="Component"/>
                    </keep-alive>
                </router-view>
                <router-view v-slot="{ Component }" v-if="!$route.meta.keepAlive">
                    <keep-alive>
                        <component :is="Component"/>
                    </keep-alive>
                </router-view>
            </div>
        </el-main>

        <message-web-socket></message-web-socket>
    </el-container>
</template>

<script>
    import messageWebSocket from "../websocket/messageWebSocket";
    import Header from './header'
    import Headroom from 'headroom.js'

    export default {
        name: 'Home',
        components: {
            Header,
            messageWebSocket
        },
        created() {
            this.$nextTick(() => {
                let headerElement = document.querySelector('#header');
                let headroom = new Headroom(headerElement, {
                    // 在元素没有固定之前，垂直方向的偏移量（以px为单位）
                    offset: 100,
                    // scroll tolerance in px before state changes
                    tolerance: 5,
                    // 对于每个状态都可以自定义css classes
                    classes: {
                        // 当元素初始化后所设置的class
                        initial: "headroom",
                        // 向上滚动时设置的class
                        pinned: "headroom--pinned",
                        // 向下滚动时所设置的class
                        unpinned: "headroom--unpinned"
                    }
                })

                headroom.init();

                //动态设置内容高度 让footer始终居底   header+footer的高度是100
                this.Height = document.documentElement.clientHeight - 100;
                //监听浏览器窗口变化
                window.onresize = () => {
                    this.Height = document.documentElement.clientHeight - 100
                }
            })
        },
        data() {
            return {
                Height: 0
            }
        },

    }
</script>

<style lang="scss">

    html,
    body {
        margin: 0;
    }

    #header {
        padding: 0;
        height: 60px;
        position: fixed;
        z-index: 10;
        right: 0;
        left: 0;
        top: 0
    }

    .headroom {
        transition: transform .25s ease-in-out;
        will-change: transform
    }

    .headroom--pinned {
        transform: translateY(0)
    }

    .headroom--unpinned {
        transform: translateY(-100%)
    }

</style>