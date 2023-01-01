<template>
  <div class="antv-chart-mini">
    <div class="chart-wrapper" :style="{ height: 46 }">
      <div id="miniArea" style="height: 100px; width: 100%">
        <!--        <v-tooltip />-->
        <!--        <v-smooth-area position="x*y" />-->
      </div>
    </div>
  </div>
</template>

<script>
import moment from 'moment'

const data = []
const beginDay = new Date().getTime()
let xAxis = []
for (let i = 0; i < 10; i++) {
  data.push(Math.round(Math.random() * 20))
  xAxis.push(moment(new Date(beginDay + 1000 * 60 * 60 * 24 * i)).format('YYYY-MM-DD'))
}

const tooltip = [
  'x*y',
  (x, y) => ({
    name: x,
    value: y,
  }),
]
const scale = [
  {
    dataKey: 'x',
    min: 2,
  },
  {
    dataKey: 'y',
    title: '时间',
    min: 1,
    max: 22,
  },
]
import * as echarts from 'echarts'
export default {
  name: 'MiniArea',
  data() {
    return {
      data,
      tooltip,
      scale,
      height: 100,
    }
  },
  mounted() {
    let mychart = echarts.init(document.getElementById('miniArea'))
    mychart.setOption({
      color:['#74bcff'],
      tooltip: { show: true,trigger:'axis' },
      grid: {
        top: '40px',
        left: '0px',
        right: '0px',
        bottom: '18px',
      },
      xAxis: { show: false, type: 'category', data: xAxis },
      yAxis: { show: false, type: 'value', max: 'dataMax', min: 1 },
      series: [
        {
          type: 'line',
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
@import 'chart';
</style>
