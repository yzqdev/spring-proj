/* eslint-disable vue/no-template-shadow */
<template>
  <a-modal
    title='编辑用户'
    :width='900'
    :visible='visible'
    :confirmLoading='confirmLoading'
    @ok='handleSubmit'
    @cancel='handleCancel'
  >
    <a-spin :spinning='confirmLoading'>
      <a-form-model :model='form' ref='editForm' :rules='rules'>
        <a-divider orientation='left'>基本信息</a-divider>
        <a-row :gutter='24'>
          <a-col :md='12' :sm='24'>

            <a-form-model-item
              style='display: none;'
            >
              <a-input v-model='form.id' />
            </a-form-model-item>
            <a-form-model-item
              label='账号'
              :labelCol='labelCol'
              :wrapperCol='wrapperCol'
              has-feedback prop='account'
            >
              <a-input placeholder='请输入账号' v-model='form.account' />
            </a-form-model-item>

          </a-col>
          <a-col :md='12' :sm='24'>
            <a-form :form='form'>
              <a-form-model-item
                label='姓名'
                :labelCol='labelCol'
                :wrapperCol='wrapperCol'
                has-feedback prop='name'
              >
                <a-input v-model='form.name' placeholder='请输入姓名' />
              </a-form-model-item>
            </a-form>
          </a-col>
        </a-row>
        <a-row :gutter='24'>
          <a-col :md='12' :sm='24'>
            <a-form-model-item
              label='昵称'
              :labelCol='labelCol'
              :wrapperCol='wrapperCol'
              has-feedback prop='nickName'
            >
              <a-input placeholder='请输入昵称' v-model='form.nickName' />
            </a-form-model-item>
          </a-col>
          <a-col :md='12' :sm='24'>
            <a-form-model-item
              label='生日'
              :labelCol='labelCol'
              :wrapperCol='wrapperCol' prop='birthday'
              has-feedback
            >
              <a-date-picker placeholder='请选择生日' @change='onChange' style='width: 100%' v-model='form.birthday' />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row :gutter='24'>
          <a-col :md='12' :sm='24'>
            <a-form :form='form'>
              <a-form-model-item
                label='性别'
                :labelCol='labelCol'
                :wrapperCol='wrapperCol' prop='sex'
              >
                <a-radio-group v-model='form.sex'>
                  <a-radio :value='1'>男</a-radio>
                  <a-radio :value='2'>女</a-radio>
                </a-radio-group>
              </a-form-model-item>
            </a-form>
          </a-col>
          <a-col :md='12' :sm='24'>
            <a-form-model-item
              label='邮箱'
              :labelCol='labelCol'
              :wrapperCol='wrapperCol'
              has-feedback prop='email'
            >
              <a-input placeholder='请输入邮箱' v-model='form.email' />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row :gutter='24'>
          <a-col :md='12' :sm='24'>
            <a-form-model-item
              label='手机号'
              :labelCol='labelCol'
              :wrapperCol='wrapperCol'
              has-feedback prop='phone'
            >
              <a-input placeholder='请输入手机号' v-model='form.phone' />
            </a-form-model-item>
          </a-col>
          <a-col :md='12' :sm='24'>
            <a-form :form='form'>
              <a-form-model-item
                label='电话'
                :labelCol='labelCol'
                :wrapperCol='wrapperCol'
                has-feedback prop='tel'
              >
                <a-input placeholder='请输入电话' v-model='form.tel' />
              </a-form-model-item>
            </a-form>
          </a-col>
        </a-row>
      </a-form-model>
      <a-form-model :model='sysEmpParam' :rules='rules2'>
        <a-divider orientation='left'>员工信息</a-divider>
        <a-row :gutter='24'>
          <a-col :md='12' :sm='24'>

            <a-form-model-item
              label='机构'
              :labelCol='labelCol'
              :wrapperCol='wrapperCol'
              has-feedback prop='orgId'
            >
              <a-tree-select v-model='sysEmpParam.orgId'
                             style='width: 100%'
                             :dropdownStyle="{ maxHeight: '300px', overflow: 'auto' }"
                             :treeData='orgTree'
                             placeholder='请选择机构'
                             treeDefaultExpandAll
                             @change='e => initrOrgName(e)'
              >
                <span slot='title' slot-scope='{ id }'>{{ id }}</span>
              </a-tree-select>
            </a-form-model-item>
            <a-form-model-item v-show='false' prop='orgName'>
              <a-input v-model='sysEmpParam.orgName' />
            </a-form-model-item>

          </a-col>
          <a-col :md='12' :sm='24'>
            <a-form-model-item
              label='工号'
              :labelCol='labelCol'
              :wrapperCol='wrapperCol'
              has-feedback prop='jobNum'
            >
              <a-input placeholder='请输入工号' v-model='sysEmpParam.jobNum' />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row :gutter='24'>
          <a-col :md='24' :sm='24'>
            <a-form-model-item
              label='职位信息'
              :labelCol='labelCol_JG'
              :wrapperCol='wrapperCol_JG'
              has-feedback prop='posIdList'
            >
              <a-select
                mode='multiple'
                style='width: 100%' v-model='sysEmpParam.posIdList'
                placeholder='请选择职位信息'
              >
                <a-select-option v-for='(item,index) in posList' :key='index' :value='item.id'>{{ item.name }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row :gutter='24'>
          <a-col :md='24' :sm='24'>
            <a-form-model-item
              label='附属信息:'
              :labelCol='labelCol_JG'
              :wrapperCol='wrapperCol_JG'
            >
              <a-table
                :columns='columns'
                :dataSource='data'
                :pagination='false'
                :loading='memberLoading'
              >
                <template v-for="(col,index) in ['extOrgId','extPosId']" :slot='col' slot-scope='text, record'>
                  <template v-if='index == 0'>
                    <template v-if="record.extOrgId !=''">
                      <a-tree-select
                        :key='col'
                        :treeData='orgTree'
                        style='width: 100%'
                        placeholder='请选择附属机构'
                        :defaultValue='record.extOrgId'
                        treeDefaultExpandAll
                        @change='e => handleChange(e,record.key,col)'
                      >
                        <span slot='title' slot-scope='{ id }'>{{ id }}</span>
                      </a-tree-select>
                    </template>
                    <template v-else>
                      <a-tree-select
                        :key='col'
                        :treeData='orgTree'
                        style='width: 100%'
                        placeholder='请选择附属机构'
                        treeDefaultExpandAll
                        @change='e => handleChange(e,record.key,col)'
                      >
                        <span slot='title' slot-scope='{ id }'>{{ id }}</span>
                      </a-tree-select>
                    </template>
                  </template>
                  <template v-if='index == 1'>
                    <template v-if="record.extOrgId !=''">
                      <a-select
                        :key='col'
                        style='width: 100%'
                        placeholder='请选择附属职位'
                        :default-value='record.extPosId'
                        @change='e => handleChange(e,record.key,col)'
                        has-feedback
                      >
                        // eslint-disable-next-line vue/no-template-shadow
                        <a-select-option v-for='(item,indexs) in posList' :key='indexs' :value='item.id'>{{ item.name
                          }}
                        </a-select-option>
                      </a-select>
                    </template>
                    <template v-else>
                      <a-select
                        :key='col'
                        style='width: 100%'
                        placeholder='请选择附属职位'
                        @change='e => handleChange(e,record.key,col)'
                        has-feedback
                      >
                        // eslint-disable-next-line vue/no-template-shadow
                        <a-select-option v-for='(item,indexs) in posList' :key='indexs' :value='item.id'>{{ item.name
                          }}
                        </a-select-option>
                      </a-select>
                    </template>
                  </template>
                </template>
                <template slot='operation' slot-scope='text, record'>
                  <a @click='remove(record.key)'>删除</a>
                </template>
              </a-table>
              <a-button style='width: 100%; margin-top: 16px; margin-bottom: 8px' type='dashed' icon='plus'
                        @click='newMember'>增行
              </a-button>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-spin>
  </a-modal>
</template>
<script>
import { sysUserEdit, sysUserDetail } from '@/api/modular/system/userManage'
import { getOrgTree, getOrgList } from '@/api/modular/system/orgManage'
import { sysPosList } from '@/api/modular/system/posManage'
import moment from 'moment'

export default {
  data() {
    return {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      // 机构行样式
      labelCol_JG: {
        xs: { span: 24 },
        sm: { span: 3 }
      },
      wrapperCol_JG: {
        xs: { span: 24 },
        sm: { span: 20 }
      },
      count: 1,
      columns: [
        {
          title: '附属机构',
          dataIndex: 'extOrgId',
          width: '45%',
          scopedSlots: { customRender: 'extOrgId' }
        },
        {
          title: '附属岗位',
          dataIndex: 'extPosId',
          width: '45%',
          scopedSlots: { customRender: 'extPosId' }
        },
        {
          title: '操作',
          key: 'action',
          scopedSlots: { customRender: 'operation' }
        }
      ],
      visible: false,
      confirmLoading: false,
      orgTree: [],
      orgList: [],
      posList: [],
      sysEmpParam: { orgName: '', posIdList: '', jobNum: '', orgId: '' },
      sysEmpParamExtList: [],
      memberLoading: false,
      form: {}, rules: {
        account: [{ required: true, min: 5, message: '请输入至少五个字符的账号！' }],
        name: [{ required: true, message: '请输入姓名！' }],
        sex: [{ required: true, message: '请选择性别！' }],
        phone: [{ required: true, message: '请输入手机号！' }]
      },
      rules2: {
        orgId: [{ required: true, message: '请选择机构！' }],
        posIdList: [{ required: true, message: '请选择职位信息！' }]
      },
      data: [],
      birthdayString: ''
    }
  },
  methods: {
    // 初始化方法
    edit(record) {
      this.confirmLoading = true
      this.visible = true
      this.getOrgData()
      this.getPosList()
      // 基本信息加人表单
      setTimeout(() => {
        this.form = {
          id: record.id,
          account: record.account,
          name: record.name,
          nickName: record.nickName,
          sex: record.sex,
          email: record.email,
          phone: record.phone,
          tel: record.tel
        }

      }, 100)
      // 时间单独处理
      if (record.birthday != null) {
        this.form.birthday = moment(record.birthday, 'YYYY-MM-DD')
      }
      this.birthdayString = moment(record.birthday).format('YYYY-MM-DD')
      // 职位信息加入表单
      this.getUserDetaile(record.id)
    },
    /**
     * 通过用户ID查询出用户详情，将职位信息填充
     * @param id
     */
    getUserDetaile(id) {
      sysUserDetail({ 'id': id }).then((res) => {
        const SysEmpInfo = res.data.sysEmpInfo
        const Positions = []
        SysEmpInfo.positions.forEach(item => {
          Positions.push(item.posId)
        })
        this.sysEmpParam = {
          orgName: SysEmpInfo.orgName,
          posIdList: Positions,
          jobNum: SysEmpInfo.jobNum,
          orgId: SysEmpInfo.orgId
        }

        SysEmpInfo.extOrgPos.forEach(item => {
          const length = this.data.length
          this.data.push({
            key: length === 0 ? '1' : (parseInt(this.data[length - 1].key) + 1).toString(),
            extOrgId: item.orgId,
            extPosId: item.posId
          })
        })
        this.confirmLoading = false
      })
    },
    /**
     * 增行
     */
    newMember() {
      const length = this.data.length
      this.data.push({
        key: length === 0 ? '1' : (parseInt(this.data[length - 1].key) + 1).toString(),
        extOrgId: '',
        extPosId: ''
      })
    },
    /**
     * 删除
     */
    remove(key) {
      const newData = this.data.filter(item => item.key !== key)
      this.data = newData
    },
    /**
     * 选择子表单单项触发
     */
    handleChange(value, key, column) {
      const newData = [...this.data]
      const target = newData.find(item => key === item.key)
      if (target) {
        target[column] = value
        this.data = newData
      }
    },
    /**
     * 获取机构数据，并加载于表单中
     */
    getOrgData() {
      getOrgTree().then((res) => {
        this.orgTree = res.data
      })
      getOrgList().then((res) => {
        this.orgList = res.data
      })
    },
    /**
     * 获取职位list列表
     */
    getPosList() {
      sysPosList().then((res) => {
        this.posList = res.data
      })
    },
    /**
     * 选择树机构，初始化机构名称于表单中
     */
    initrOrgName(value) {
      this.form.getFieldDecorator('sysEmpParam.orgName', { initialValue: this.orgList.find(item => value === item.id).name })
    },
    /**
     * 子表单json重构
     */
    JsonReconsitution() {
      this.sysEmpParamExtList = []
      const newData = [...this.data]
      newData.forEach(item => {
        // eslint-disable-next-line eqeqeq
        if (item.extOrgId != '' & item.extPosId != '') {
          this.sysEmpParamExtList.push({ orgId: item.extOrgId, posId: item.extPosId })
        }
      })
    },
    /**
     * 日期需单独转换
     */
    onChange(date, dateString) {
      this.birthdayString = moment(date).format('YYYY-MM-DD')
    },
    handleSubmit() {
      this.confirmLoading = true
      this.$refs.editForm.validate((valid) => {
        if (valid) {
          this.JsonReconsitution()
          this.sysEmpParam['extIds'] = this.sysEmpParamExtList
          // eslint-disable-next-line eqeqeq
          if (this.birthdayString == 'Invalid date') {
            this.birthdayString = ''
          }
          this.form.birthday = this.birthdayString
          this.form.sysEmpParam = this.sysEmpParam
          sysUserEdit(this.form).then((res) => {
            if (res.success) {
              this.$message.success('编辑成功')
              this.confirmLoading = false
              this.$emit('ok', this.form)
              this.handleCancel()
            } else {
              this.$message.error('编辑失败：' + res.message)
            }
          }).finally((res) => {
            this.confirmLoading = false
          })
        } else {
          this.confirmLoading = false
        }
      })
    },
    handleCancel() {
      this.$refs.editForm.resetFields()
      this.visible = false
      // 清理子表单中数据
      this.data = []
      // 清理时间
      this.birthdayString = ''
      this.form.birthday = null
    }
  }
}
</script>
