<template>
  <el-menu
      default-active="/report/list"
      mode="horizontal"
      background-color="#545c64"
      text-color="#fff"
      :router="true"
      active-text-color="#ffd04b">
    <template v-for="item in menuList" v-bind:key="item.title">
      <el-menu-item v-if="!item.sub" :index="item.route" v-bind:key="item.route">
        {{ item.title }}
      </el-menu-item>
      <el-submenu v-if="item.sub" :index="item.title">
        <template #title>{{ item.title }}</template>
        <el-menu-item v-for="sub in item.sub" :index="sub.route" v-bind:key="sub.route">
          {{ sub.title }}
        </el-menu-item>
      </el-submenu>
    </template>

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
          title: '报表',
          sub: [
            {
              route: '/report/home',
              title: '总览',
            },
            {
              route: '/report/dict',
              title: '字典'
            },
            {
              route: '/report/dataSource',
              title: '数据源'
            },
            {
              route: '/report/reportDownload',
              title: '报表下载'
            },
           ]
        },
        {
          route: '/cache',
          title: '缓存'
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
