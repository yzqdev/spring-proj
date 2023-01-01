<template>
  <div :class="prefixCls">
    <div class="chart-wrapper" :style="{ height: 46 }">
      <div :id='id'  style='height: 100px;width: 100%' :scale="scale"  >
<!--        <v-tooltip />-->
<!--        <v-smooth-line position="x*y" :size="2" />-->
<!--        <v-smooth-area position="x*y" />-->
      </div>
    </div>
  </div>
</template>

<script>

import * as echarts from 'echarts'

export default {
  name: 'MiniSmoothArea',
  props: {
    prefixCls: {
      type: String,
      default: 'ant-pro-smooth-area'
    },
    scale: {
      type: [Object, Array],
      required: true
    },
    dataSource: {
      type: Object,
      required: true
    },id:{
      type:String
    }
  },
  data () {
    return {
      height: 100
    }
  },mounted() {


    let mychart = echarts.init(document.getElementById(this.id))
    mychart.setOption({
      color:['#74bcff'],
      tooltip: { show: true,trigger:'axis' },
      grid: {
        top: '36px',
        left: '0px',
        right: '0px',
        bottom: '18px',
      },
      xAxis: { show: false, type: 'category', data: this.dataSource.x },
      yAxis: { show: false, type: 'value', max: 'dataMax', min: 1 },
      series: [
        {
          name:'次数',
          type: 'line',
          smooth: true,
          symbol:'circle',
          showSymbol:false,
          data: this.dataSource.y,
          areaStyle: {},
        },
      ],
    })
  },
}
</script>

<style lang="less" scoped>
  @import "smooth.area.less";
</style>
