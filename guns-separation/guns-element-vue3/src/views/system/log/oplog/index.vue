<template>
  <el-card :bordered="false">
    <div class="table-page-search-wrapper" v-if="hasPerm('sysOpLog:page')">
      <el-form layout="inline">
        <el-row :gutter="48">
          <el-col :md="8" :sm="24">
            <el-form-item label="日志名称">
              <el-input v-model="queryParam.name" allow-clear placeholder="请输入日志名称"/>
            </el-form-item>
          </el-col>
          <el-col :md="8" :sm="24">
            <el-select v-model="queryParam.opType" allow-clear placeholder="请选择操作类型" >
              <el-option v-for="(item,index) in opTypeDict" :key="index" :value="item.code" >{{ item.value }}</el-option>
            </el-select>
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
              <el-button type="primary" @click="$refs.table.refresh(true)">查询</el-button>
              <el-button style="margin-left: 8px" @click="() => queryParam = {}">重置</el-button>
              <a @click="toggleAdvanced" style="margin-left: 8px">
                {{ advanced ? '收起' : '展开' }}
<!--                <a-icon :type="advanced ? 'up' : 'down'"/>-->
              </a>
            </span>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div class="table-operator" v-if="hasPerm('sysOpLog:delete')">
      <a-popconfirm placement="top" title="确认清空日志？" @confirm="() => sysOpLogDelete()">
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
      <span slot="opType" slot-scope="text">
        {{ opTypeFilter(text) }}
      </span>
      <span slot="success" slot-scope="text">
        {{ successFilter(text) }}
      </span>
      <span slot="name" slot-scope="text">
        <ellipsis :length="10" tooltip>{{ text }}</ellipsis>
      </span>
      <span slot="url" slot-scope="text">
        <ellipsis :length="10" tooltip>{{ text }}</ellipsis>
      </span>
      <span slot="opTime" slot-scope="text">
        <ellipsis :length="10" tooltip>{{ text }}</ellipsis>
      </span>
      <span slot="action" slot-scope="text, record">
        <span slot="action" >
          <a @click="$refs.detailsOplog.details(record)">查看详情</a>
        </span>
      </span>
    </s-table>
    <details-oplog ref="detailsOplog"/>
  </el-card>
</template>
<script>
  import { STable, Ellipsis } from '@/components'
  import { sysOpLogPage, sysOpLogDelete } from '@/api/modular/system/logManage'
  import detailsOplog from './details'
  import { sysDictTypeDropDown } from '@/api/modular/system/dictManage'
  export default {
    components: {
      STable,
      Ellipsis,
      detailsOplog
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
            title: '操作类型',
            dataIndex: 'opType',
            scopedSlots: { customRender: 'opType' }
          },
          {
            title: '执行结果',
            dataIndex: 'success',
            scopedSlots: { customRender: 'success' }
          },
          {
            title: 'ip',
            dataIndex: 'ip'
          },
          {
            title: '请求地址',
            dataIndex: 'url',
            scopedSlots: { customRender: 'url' }
          },
          {
            title: '操作时间',
            dataIndex: 'opTime',
            scopedSlots: { customRender: 'opTime' }
          },
          {
            title: '操作人',
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
          return sysOpLogPage(Object.assign(parameter, this.queryParam)).then((res) => {
            return res.data
          })
        },
        selectedRowKeys: [],
        selectedRows: [],
        defaultExpandedKeys: [],
        opTypeDict: [],
        successDict: []
      }
    },
    created () {
      this.sysDictTypeDropDown()
    },
    methods: {
      opTypeFilter (opType) {
        // eslint-disable-next-line eqeqeq
        const values = this.opTypeDict.filter(item => item.code == opType)
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
        sysDictTypeDropDown({ code: 'op_type' }).then((res) => {
          this.opTypeDict = res.data
        })
        sysDictTypeDropDown({ code: 'yes_or_no' }).then((res) => {
          this.successDict = res.data
        })
      },
      /**
       * 清空日志
       */
      sysOpLogDelete () {
        sysOpLogDelete().then((res) => {
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
