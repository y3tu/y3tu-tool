<template>
  <div>
    <animation :type="4" :width="300" :height="300"/>
    <div class="about" id="main" :style="{ width: '500px', height: '400px' }"></div>
  </div>
</template>

<script>
import animation from './animation'
import * as echarts from "echarts";


export default {
  components: {animation},
  mounted() {
    var myChart = echarts.init(document.getElementById("main"));
    // 绘制图表
    myChart.setOption({
      title: {
        text: "cpu使用率",
      },
      tooltip: {},
      xAxis: {
        type: 'category',
        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
      },
      yAxis: {
        type: 'value'
      },
      series: [{
        data: [820, 932, 901, 934, 1290, 1330, 1320],
        type: 'line',
        smooth: true
      }]
    });
  },
  setup: () => {
  }
}
</script>

<style>

</style>