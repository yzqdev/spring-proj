<template>
  <div class="page-header-index-wide">
    <el-row :gutter="24">
      <el-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="总销售额" total="￥126,560">
          <el-tooltip content="指标说明"  >
             <info-circle-outlined />
          </el-tooltip>
          <div>
            <trend flag="up" style="margin-right: 16px">
              <template #term>
                <span>周同比</span>
              </template>
              12%
            </trend>
            <trend flag="down">
              <span slot="term">日同比</span>
              11%
            </trend>
          </div>
          <template #footer>日均销售额<span>￥ 234.56</span></template>
        </chart-card>
      </el-col>
      <el-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="访问量" :total="8846 | NumberFormat">
          <el-tooltip content="指标说明"  >
            <info-circle-outlined />
          </el-tooltip>
          <div>
            <mini-area />
          </div>
          <template slot="footer"
          >日访问量<span> {{ '1234' | NumberFormat }}</span></template
          >
        </chart-card>
      </el-col>
      <el-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="支付笔数" :total="6560 | NumberFormat">
          <el-tooltip content="指标说明"  >
             <info-circle-outlined/>
          </el-tooltip>
          <div>
            <mini-bar />
          </div>
          <template slot="footer">转化率 <span>60%</span></template>
        </chart-card>
      </el-col>
      <el-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="运营活动效果" total="78%">
          <el-tooltip content="指标说明"  >
            <info-circle-outlined />
          </el-tooltip>
          <div>
            <mini-progress color="rgb(19, 194, 194)" :target="80" :percentage="78" height="8px" />
          </div>
          <template slot="footer">
            <trend flag="down" style="margin-right: 16px">
              <span slot="term">同周比</span>
              12%
            </trend>
            <trend flag="up">
              <span slot="term">日环比</span>
              80%
            </trend>
          </template>
        </chart-card>
      </el-col>
    </el-row>

    <el-card :loading="loading" :bordered="false" :body-style="{ padding: '0' }">
      <div class="salesCard">
        <el-tabs default-active-key="1" size="large" :tab-bar-style="{ marginBottom: '24px', paddingLeft: '16px' }">
          <div class="extra-wrapper" slot="tabBarExtraContent">
            <div class="extra-item">
              <a>今日</a>
              <a>本周</a>
              <a>本月</a>
              <a>本年</a>
            </div>
            <el-input type='range' :style="{ width: '256px' }" />
          </div>
          <el-tab-pane loading="true" tab="销售额" key="1">
            <el-row>
              <el-col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
                <bar :data="barData" id="bardata" title="销售额排行" />
              </el-col>
              <el-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
                <rank-list title="门店销售排行榜" :list="rankList" />
              </el-col>
            </el-row>
          </el-tab-pane>
          <el-tab-pane tab="访问量" key="2">
            <el-row>
              <el-col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
                <bar :data="barData2" id="barData2" title="销售额趋势" />
              </el-col>
              <el-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
                <rank-list title="门店销售排行榜" :list="rankList" />
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-card>

    <div class="antd-pro-pages-dashboard-analysis-twoColLayout" :class="isDesktop() ? 'desktop' : ''">
      <el-row :gutter="24" type="flex" :style="{ marginTop: '24px' }">
        <el-col :xl="12" :lg="24" :md="24" :sm="24" :xs="24">
          <el-card :loading="loading" :bordered="false" title="线上热门搜索" :style="{ height: '100%' }">
            <el-dropdown  trigger="click" placement="bottomLeft"  >
              <a class="ant-dropdown-link" href="#">
                 <ellipsis-outlined />
              </a>
              <el-menu slot="overlay">
                <el-menu-item>
                  <a href="javascript:;">操作一</a>
                </el-menu-item>
                <el-menu-item>
                  <a href="javascript:;">操作二</a>
                </el-menu-item>
              </el-menu>
            </el-dropdown>
            <el-row :gutter="68">
              <el-col :xs="24" :sm="12" :style="{ marginBottom: ' 24px' }">
                <number-info :total="12321" :sub-total="17.1">
                  <span slot="subtitle">
                    <span>搜索用户数</span>
                    <el-tooltip content="指标说明"  >

                      <info-circle-outlined :style="{ marginLeft: '8px' }" />
                    </el-tooltip>
                  </span>
                </number-info>
                <!-- miniChart -->
                <div>
                  <mini-smooth-area
                    id="searchUserData"
                    :style="{ height: '45px' }"
                    :dataSource="searchUserData"
                    :scale="searchUserScale"
                  />
                </div>
              </el-col>
              <el-col :xs="24" :sm="12" :style="{ marginBottom: ' 24px' }">
                <number-info :total="2.7" :sub-total="26.2" status="down">
                  <span slot="subtitle">
                    <span>人均搜索次数</span>
                    <el-tooltip content="指标说明"  >
                      <info-circle-outlined :style="{ marginLeft: '8px' }" />
                    </el-tooltip>
                  </span>
                </number-info>
                <!-- miniChart -->
                <div>
                  <mini-smooth-area
                    :style="{ height: '45px' }"
                    id="Searchuser2"
                    :dataSource="searchUserData"
                    :scale="searchUserScale"
                  />
                </div>
              </el-col>
            </el-row>
            <div class="ant-table-wrapper">
              <el-table
                row-key="index"
                size="small"
                :columns="searchTableColumns"
                :data="searchData"
                :pagination="{ pageSize: 5 }"
              >
<!--                <template v-slot="{row}">-->
<!--                  <trend :flag="row.status === 0 ? 'up' : 'down'"> {{ text }}% </trend>-->
<!--                </template>-->
              </el-table>
            </div>
          </el-card>
        </el-col>
        <el-col :xl="12" :lg="24" :md="24" :sm="24" :xs="24">
          <el-card
            class="antd-pro-pages-dashboard-analysis-salesCard"
            :loading="loading"
            :bordered="false"
            title="销售额类别占比"
            :style="{ height: '100%' }"
          >
            <div   style="height: inherit">
              <!-- style="bottom: 12px;display: inline-block;" -->
              <span class="dashboard-analysis-iconGroup">
                <el-dropdown  trigger="click" placement="bottomLeft">
                   <ellipsis-outlined />
                  <el-menu slot="overlay">
                    <el-menu-item>
                      <a href="javascript:;">操作一</a>
                    </el-menu-item>
                    <el-menu-item>
                      <a href="javascript:;">操作二</a>
                    </el-menu-item>
                  </el-menu>
                </el-dropdown>
              </span>
              <div class="analysis-salesTypeRadio">
                <el-radio-group defaultValue="a">
                  <el-radio-button value="a">全部渠道</el-radio-button>
                  <el-radio-button value="b">线上</el-radio-button>
                  <el-radio-button value="c">门店</el-radio-button>
                </el-radio-group>
              </div>
            </div>
            <h4>销售额</h4>
            <div>
              <!-- style="width: calc(100% - 240px);" -->
              <div style="height: 400px; width: 100%" id="chart5">
                <!--                  <v-tooltip :showTitle="false" dataKey="item*percent" />-->
                <!--                  <v-axis />-->
                <!--               -->
                <!--                  <v-legend dataKey="item"/>-->
                <!--                  <v-pie position="percent" color="item" :vStyle="pieStyle" />-->
                <!--                  <v-coord type="theta" :radius="0.75" :innerRadius="0.6" />-->
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import moment from 'moment'
import {
  ChartCard,
  MiniArea,
  MiniBar,
  MiniProgress,
  RankList,
  Bar,
  Trend,
  NumberInfo,
  MiniSmoothArea,
} from '@/components'
import { mixinDevice } from '@/utils/mixin'

import * as echarts from 'echarts'

import {EllipsisOutlined,InfoCircleOutlined} from '@ant-design/icons-vue'
const barData = { x: [], y: [] }
const barData2 = { x: [], y: [] }
for (let i = 0; i < 12; i += 1) {
  barData.x.push(`${i + 1}月`)
  barData.y.push(Math.floor(Math.random() * 1000) + 200)
  barData2.x.push(`${i + 1}月`)
  barData2.y.push(Math.floor(Math.random() * 1000) + 200)
}

const rankList = []
for (let i = 0; i < 7; i++) {
  rankList.push({
    name: '白鹭岛 ' + (i + 1) + ' 号店',
    total: 1234.56 - i * 100,
  })
}

const searchUserData = { x: [], y: [] }
for (let i = 0; i < 7; i++) {
  searchUserData.x.push(moment().add(i, 'days').format('YYYY-MM-DD'))
  searchUserData.y.push(Math.ceil(Math.random() * 10))
}
const searchUserScale = [
  {
    dataKey: 'x',
    alias: '时间',
  },
  {
    dataKey: 'y',
    alias: '用户数',
    min: 0,
    max: 10,
  },
]

const searchTableColumns = [
  {
    dataIndex: 'MenuIndex.vue',
    title: '排名',
    width: 90,
  },
  {
    dataIndex: 'keyword',
    title: '搜索关键词',
  },
  {
    dataIndex: 'count',
    title: '用户数',
  },
  {
    dataIndex: 'range',
    title: '周涨幅',
    align: 'right',
    sorter: (a, b) => a.range - b.range,
    scopedSlots: { customRender: 'range' },
  },
]
const searchData = []
for (let i = 0; i < 50; i += 1) {
  searchData.push({
    index: i + 1,
    keyword: `搜索关键词-${i}`,
    count: Math.floor(Math.random() * 1000),
    range: Math.floor(Math.random() * 100),
    status: Math.floor((Math.random() * 10) % 2),
  })
}


const pieData = [
  { name: '家用电器', value: 32.2 },
  { name: '食用酒水', value: 21 },
  { name: '个护健康', value: 17 },
  { name: '服饰箱包', value: 13 },
  { name: '母婴产品', value: 9 },
  { name: '其他', value: 7.8 },
]
let total = 0
for (let i = 0; i <pieData.length; i++) {
  total += pieData[i].value
}
console.log(total)
export default {
  name: 'Analysis',
  mixins: [mixinDevice],
  components: {
    ChartCard,
    MiniArea,
    MiniBar,
    MiniProgress,
    RankList,
    Bar,
    Trend,
    NumberInfo,
    MiniSmoothArea,EllipsisOutlined,InfoCircleOutlined
  },
  data() {
    return {
      loading: true,
      rankList,

      // 搜索用户数
      searchUserData,
      searchUserScale,
      searchTableColumns,
      searchData,

      barData,
      barData2,

      //
      pieData,

      pieStyle: {
        stroke: '#fff',
        lineWidth: 1,
      },
    }
  },
  created() {
    // setTimeout(() => {

    this.loading = !this.loading
    // }, 1000)
  },
  mounted() {
    let mychart = echarts.init(document.getElementById('chart5'))
    mychart.setOption({
      tooltip: {
        trigger: 'item',
        formatter: function (params) {
          return ((params.value / total) * 100).toFixed(1) + '%'
        },
      },
      legend: {
        top: '5%',
        left: 'center',
      },
      grid: {
        top: '40px',
        left: '0px',
        right: '0px',
        bottom: '18px',
      },

      series: [
        {
          type: 'pie',
          radius: ['40%', '70%'],
          avoidLabelOverlap: false,
          label: {
            show: false,
            position: 'center',
          },
          emphasis: {
            label: {
              show: true,
              fontSize: '40',
              fontWeight: 'bold',
            },
          },
          labelLine: {
            show: false,
          },

          data: this.pieData,
        },
      ],
    })
  },
}
</script>

<style lang="less" scoped>
.extra-wrapper {
  line-height: 55px;
  padding-right: 24px;

  .extra-item {
    display: inline-block;
    margin-right: 24px;

    a {
      margin-left: 24px;
    }
  }
}

.antd-pro-pages-dashboard-analysis-twoColLayout {
  position: relative;
  display: flex;
  display: block;
  flex-flow: row wrap;
}

.antd-pro-pages-dashboard-analysis-salesCard {
  height: calc(100% - 24px);
  /deep/ .ant-card-head {
    position: relative;
  }
}

.dashboard-analysis-iconGroup {
  i {
    margin-left: 16px;
    color: rgba(0, 0, 0, 0.45);
    cursor: pointer;
    transition: color 0.32s;
    color: black;
  }
}
.analysis-salesTypeRadio {
  position: absolute;
  right: 54px;
  bottom: 12px;
}
</style>
