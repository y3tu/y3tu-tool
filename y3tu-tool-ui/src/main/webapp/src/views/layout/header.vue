<template>
    <el-menu
            default-active="/report/list"
            mode="horizontal"
            background-color="#545c64"
            text-color="#fff"
            :router="true"
            active-text-color="#ffd04b">
        <el-menu-item v-for="item in menuList" :index="item.route" v-bind:key="item.route">
            {{ item.title }}
        </el-menu-item>
    </el-menu>
</template>

<script>

    import logo from '../../assets/logo.png'

    export default {
        name: 'Header',
        computed: {},
        data() {
            return {
                menuList: [
                    {
                        route: '/',
                        title: '首页'
                    },
                    {
                        route: '/report/home',
                        title: '报表'
                    },
                    {
                        route: '/report/dataSource',
                        title: '数据源'
                    },
                    {
                        route: '/report/dict',
                        title: '字典'
                    },
                    {
                        route: '/report/generationLog',
                        title: '生成记录'
                    },
                ],
                logo,
                searchContent: ''
            }
        },
        methods: {}
    }
</script>

<style lang="scss" scoped>

</style>
