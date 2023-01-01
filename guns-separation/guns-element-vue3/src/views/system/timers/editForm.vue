<template>
  <el-dialog
    title="编辑定时任务"
    :width="900"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
    @cancel="handleCancel"
  >
    <a-spin :spinning="formLoading">
      <el-form :form="form">

        <el-form-item
          style="display: none;"
        >
          <el-input v-decorator="['id']" />
        </el-form-item>
        <el-form-item
          style="display: none;"
        >
          <el-input v-decorator="['jobStatus']" />
        </el-form-item>

        <el-form-item
          label="任务名称"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          has-feedback
        >
          <el-input placeholder="请输入任务名称" v-decorator="['timerName', {rules: [{required: true, message: '请输入任务名称！'}]}]" />
        </el-form-item>

        <el-form-item
          label="任务class类名"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          has-feedback
        >
          <el-select style="width: 100%" placeholder="请选择任务class类名" v-decorator="['actionClass', {rules: [{ required: true, message: '请选择任务class类名！' }]}]" >
            <el-option v-for="(item,index) in actionClassData" :key="index" :value="item" >{{ item }}</el-option>
          </el-select>
        </el-form-item>

        <el-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="任务表达式"
        >
          <el-input placeholder="请输入任务表达式" v-decorator="['cron', {rules: [{required: true, message: '请输入任务class类名！'}]}]" />
        </el-form-item>

        <el-form-item
          label="备注"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          has-feedback
        >
          <a-textarea :rows="4" placeholder="请输入备注" v-decorator="['remark']"></a-textarea>
        </el-form-item>

      </el-form>

    </a-spin>
  </el-dialog>
</template>

<script>
  import { sysTimersEdit, sysTimersGetActionClasses } from '@/api/modular/system/timersManage'

  export default {
    data () {
      return {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 15 }
        },
        visible: false,
        confirmLoading: false,
        actionClassData: [],
        formLoading: false,
        form: this.$form.createForm(this)
      }
    },
    methods: {
      // 初始化方法
      edit (record) {
        this.visible = true
        this.formLoading = true
        this.getActionClass()
        setTimeout(() => {
          this.form.setFieldsValue(
            {
              id: record.id,
              timerName: record.timerName,
              actionClass: record.actionClass,
              cron: record.cron,
              jobStatus: record.jobStatus,
              remark: record.remark
            }
          )
        }, 100)
      },

      /**
       * 获取选择器下拉框数据
       */
      getActionClass () {
        sysTimersGetActionClasses().then((res) => {
          this.formLoading = false
          if (res.success) {
            this.actionClassData = res.data
          } else {
            this.$message.error('获取选择器下拉框数据')
          }
        })
      },

      handleSubmit () {
        const { form: { validateFields } } = this
        this.confirmLoading = true
        validateFields((errors, values) => {
          if (!errors) {
            sysTimersEdit(values).then((res) => {
              if (res.success) {
                this.$message.success('编辑成功')
                this.visible = false
                this.confirmLoading = false
                this.$emit('ok', values)
                this.form.resetFields()
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
      handleCancel () {
        this.form.resetFields()
        this.visible = false
      }
    }
  }
</script>
