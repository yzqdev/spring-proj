<template>
  <div id='radar' style='height: 400px;width: 100%'  :padding="[20, 20, 95, 20]" :scale="scale">
<!--    <v-tooltip></v-tooltip>-->
<!--    <v-axis :dataKey="axis1Opts.dataKey" :line="axis1Opts.line" :tickLine="axis1Opts.tickLine" :grid="axis1Opts.grid" />-->
<!--    <v-axis :dataKey="axis2Opts.dataKey" :line="axis2Opts.line" :tickLine="axis2Opts.tickLine" :grid="axis2Opts.grid" />-->
<!--    <v-legend dataKey="user" marker="circle" :offset="30" />-->
<!--    <v-coord type="polar" radius="0.8" />-->
<!--    <v-line position="item*score" color="user" :size="2" />-->
<!--    <v-point position="item*score" color="user" :size="4" shape="circle" />-->
  </div>
</template>

<script>
import * as echarts from 'echarts'

const axis1Opts = {
  dataKey: 'item',
  line: null,
  tickLine: null,
  grid: {
    lineStyle: {
      lineDash: null
    },
    hideFirstLine: false
  }
}
const axis2Opts = {
  dataKey: 'score',
  line: null,
  tickLine: null,
  grid: {
    type: 'polygon',
    lineStyle: {
      lineDash: null
    }
  }
}

const scale = [
  {
    dataKey: 'score',
    min: 0,
    max: 80
  }, {
    dataKey: 'user',
    alias: '类型'
  }
]

export default {
  name: 'Radar',
  props: {
    data: {
      type: Array,
      default: null
    }
  },
  data () {
    return {
      axis1Opts,
      axis2Opts,
      scale
    }
  },mounted() {
    let mychart = echarts.init(document.getElementById('radar'))
    mychart.setOption({

      tooltip: { show: true,trigger:'item' },
      legend: {
        x:'center',
        y:'bottom',
        data: ['预算分配', '实际开销','贡献']
      },
      radar: {
        axisLabel: {show: true, textStyle: {  color: '#333'}},
        indicator: [
          { name: '引用', max: 65},
          { name: '口碑', max: 160},
          { name: '热度', max: 300},
          { name: '产量', max: 380},
          { name: '贡献', max: 520},

        ]
      },
      series: [
        {
          type: 'radar',
          smooth: true,
          symbol:'circle',
          showSymbol:false,
          data: [
            {
              value: [42, 30, 2, 350, 5, 180],
              name: '预算分配'
            },
            {
              value: [50, 140, 280, 260, 420, 210],
              name: '实际开销'
            },  {
              value: [55, 60, 140, 350, 120, 110],
              name: '贡献'
            }
          ],

        },
      ],
    })
  }
}
</script>

<style scoped>

</style>
