<template>
  <div class="antv-chart-mini">
    <div class="chart-wrapper" :style="{ height: 46 }">
      <div id='container1' style='height: 100px;width: 100%'>
<!--        <v-tooltip />-->
<!--        <v-bar position="x*y" />-->
      </div>
    </div>
  </div>
</template>

<script>
import moment from 'moment'

import * as echarts from 'echarts'
const data = []
const beginDay = new Date().getTime()
let xAxis = []
for (let i = 0; i < 10; i++) {
  data.push(Math.round(Math.random() * 20+1))
  xAxis.push(moment(new Date(beginDay + 1000 * 60 * 60 * 24 * i)).format('YYYY-MM-DD'))
}




export default {
  name: 'MiniBar',
  data () {
    return {
      data,


      height: 100
    }
  },mounted() {

    let mychart = echarts.init(document.getElementById('container1'))
    mychart.setOption({
      color:['#74bcff'],
      tooltip: { show: true,trigger:'axis' },
      grid: {
        top: '36px',
        left: '5px',
        right: '5px',
        bottom: '18px',
      },
      xAxis: { show: false, type: 'category', data: xAxis },
      yAxis: { show: false, type: 'value', max: 'dataMax', min: 1 },
      series: [
        {
          type: 'bar',
          smooth: true,
          symbol:'circle',
          showSymbol:false,
          data: this.data,
          areaStyle: {},
        },
      ],
    })
  },
}
</script>

<style lang="less" scoped>
  @import "chart";
</style>
