<template>
  <div :style="{ padding: '0 0 32px 32px' }">
    <h4 :style="{ marginBottom: '20px' }">{{ title }}</h4>
    <div :id="id" style="height: 254px; width: 100%">
      <!--      <v-tooltip />-->
      <!--      <v-axis />-->
      <!--      <v-bar position="x*y"/>-->
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'Bar',
  props: {
    title: {
      type: String,
      default: '',
    },
    data: {
      type: Object,
    },
    id:{
      type:String,
    },
    scale: {
      type: Array,
      default: () => {
        return [
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
      },
    },
    tooltip: {
      type: Array,
      default: () => {
        return [
          'x*y',
          (x, y) => ({
            name: x,
            value: y,
          }),
        ]
      },
    },
  },
  data() {
    return {}
  },
  mounted() {


    let mychart = echarts.init(document.getElementById(this.id))
    mychart.setOption({
      color: ['#38a0ff'],
      // legend: {
      //   data: ['销售额'],
      // },
      tooltip: {
        show: true,
        trigger: 'axis',
        axisPointer: {
          type: 'shadow',
        },
      },
      grid: {
        top: '36px',
        left: '40px',
        right: 'auto',
        bottom: '18px',
      },
      xAxis: {
        type: 'category',

        data: this.data.x,
      },
      yAxis: [{ show: true, name: this.title, type: 'value' }],
      series: [
        {
          name:this.title,
          type: 'bar',
          smooth: true,
          symbol: 'circle',
          barWidth: 40,
          data: this.data.y,
          areaStyle: {},
        },
      ],
    })
  },
}
</script>
