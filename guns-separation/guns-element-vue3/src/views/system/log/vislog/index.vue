<template>
  <el-card :bordered="false">
    <div class="table-page-search-wrapper" v-if="hasPerm('sysVisLog:page')">
      <el-form layout="inline">
        <el-row :gutter="48">
          <el-col :md="8" :sm="24">
            <el-form-item label="日志名称">
              <el-input v-model="queryParam.name" allow-clear placeholder="请输入日志名称"/>
            </el-form-item>
          </el-col>
          <el-col :md="8" :sm="24">
            <el-form-item label="访问类型">
              <el-select v-model="queryParam.visType" allow-clear placeholder="请选择访问类型" >
                <el-option v-for="(item,index) in visTypeDict" :key="index" :value="item.code" >{{ item.value }}</el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <template v-if="advanced">
            <el-col :md="8" :sm="24">
              <el-form-item label="是否成功">
                <el-select v-model="queryParam.success" placeholder="请选择是否成功" >
                  <el-option v-for="(item,index) in successDict" :key="index" :value="item.code" >{{ item.value }}</el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </template>
          <el-col :md="!advanced && 8 || 24" :sm="24">
            <span class="table-page-search-submitButtons" :style="advanced && { float: 'right', overflow: 'hidden' } || {} ">
              <el-button type="primary" @click="$refs.table.refresh(true)" >查询</el-button>
              <el-button style="margin-left: 8px" @click="() => queryParam = {}">重置</el-button>
              <a @click="toggleAdvanced" style="margin-left: 8px">
                {{ advanced ? '收起' : '展开' }}
                <a-icon :type="advanced ? 'up' : 'down'"/>
              </a>
            </span>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div class="table-operator" v-if="hasPerm('sysVisLog:delete')">
      <a-popconfirm v-if="hasPerm('sysVisLog:delete')" placement="top" title="确认清空日志？" @confirm="() => sysVisLogDelete()">
        <el-button >清空日志</el-button>
      </a-popconfirm>
    </div>
    <s-table
      ref="table"
      size="default"
      :columns="columns"
      :data="loadData"
      :alert="true"
      :rowKey="(record) => record.id"
      :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
    >
      <span slot="name" slot-scope="text">
        <ellipsis :length="10" tooltip>{{ text }}</ellipsis>
      </span>
      <span slot="visTime" slot-scope="text">
        <ellipsis :length="10" tooltip>{{ text }}</ellipsis>
      </span>
      <span slot="visType" slot-scope="text">
        {{ visTypeFilter(text) }}
      </span>
      <span slot="success" slot-scope="text">
        {{ successFilter(text) }}
      </span>
      <span slot="action" slot-scope="text, record">
        <span slot="action" >
          <a @click="$refs.detailsVislog.details(record)">查看详情</a>
        </span>
      </span>
    </s-table>
    <details-vislog ref="detailsVislog"/>
  </el-card>
</template>
<script>
  import { STable, Ellipsis } from '@/components'
  import { sysVisLogPage, sysVisLogDelete } from '@/api/modular/system/logManage'
  import detailsVislog from './details'
  import { sysDictTypeDropDown } from '@/api/modular/system/dictManage'
  export default {
    components: {
      STable,
      Ellipsis,
      detailsVislog
    },
    data () {
      return {
        advanced: false,
        // 查询参数
        queryParam: {},
        // 表头
        columns: [
          {
            title: '日志名称',
            dataIndex: 'name',
            scopedSlots: { customRender: 'name' }
          },
          {
            title: '访问类型',
            dataIndex: 'visType',
            scopedSlots: { customRender: 'visType' }
          },
          {
            title: '是否成功',
            dataIndex: 'success',
            scopedSlots: { customRender: 'success' }
          },
          {
            title: 'ip',
            dataIndex: 'ip'
          },
          {
            title: '浏览器',
            dataIndex: 'browser'
          },
          {
            title: '访问时间',
            dataIndex: 'visTime',
            scopedSlots: { customRender: 'visTime' }
          },
          {
            title: '访问人',
            dataIndex: 'account'
          },
          {
            title: '详情',
            dataIndex: 'action',
            width: '150px',
            scopedSlots: { customRender: 'action' }
          }
        ],
        // 加载数据方法 必须为 Promise 对象
        loadData: parameter => {
          return sysVisLogPage(Object.assign(parameter, this.queryParam)).then((res) => {
            return res.data
          })
        },
        selectedRowKeys: [],
        selectedRows: [],
        defaultExpandedKeys: [],
        visTypeDict: [],
        successDict: []
      }
    },
    /**
     * 相当于html的onload方法，进来初始化
     */
    created () {
      this.sysDictTypeDropDown()
    },
    methods: {
      visTypeFilter (visType) {
        // eslint-disable-next-line eqeqeq
        const values = this.visTypeDict.filter(item => item.code == visType)
        if (values.length > 0) {
          return values[0].value
        }
      },
      successFilter (success) {
        // eslint-disable-next-line eqeqeq
        const values = this.successDict.filter(item => item.code == success)
        if (values.length > 0) {
          return values[0].value
        }
      },
      /**
       * 获取字典数据
       */
      sysDictTypeDropDown () {
        sysDictTypeDropDown({ code: 'vis_type' }).then((res) => {
          this.visTypeDict = res.data
        })
        sysDictTypeDropDown({ code: 'yes_or_no' }).then((res) => {
          this.successDict = res.data
        })
      },
      /**
       * 清空日志
       */
      sysVisLogDelete () {
        sysVisLogDelete().then((res) => {
          if (res.success) {
            this.$message.success('清空成功')
            this.$refs.table.refresh(true)
          } else {
            this.$message.error('清空失败：' + res.message)
          }
        })
      },
      toggleAdvanced () {
        this.advanced = !this.advanced
      },
      onSelectChange (selectedRowKeys, selectedRows) {
        this.selectedRowKeys = selectedRowKeys
        this.selectedRows = selectedRows
      }
    }
  }
</script>
<style lang="less">
  .table-operator {
    margin-bottom: 18px;
  }
  button {
    margin-right: 8px;
  }
</style>
