<template>
  <div>
    <animation/>
    <el-button type="primary" icon="el-icon-refresh-left" class="return-button"><a href="/#" style="text-decoration:none;">返回</a></el-button>
  </div>
</template>

<script>
import animation from './animation'

export default {
  name: 'Page404',
  components: {animation},
  computed: {
    message() {
      return '您访问的页面没有找到！'
    }
  }
}
</script>

<style lang="scss" scoped>
  .return-button{
    position: absolute;
    left: 47%;
  }
</style>
