<template>
  <el-dialog
    title="应用编辑"
    :width="900"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
    @cancel="handleCancel"
  >
    <a-spin :spinning="confirmLoading">

      <el-form :form="form" >
        <el-form-item
          style="display: none;"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <el-input v-decorator="['id']" />
        </el-form-item>
        <el-form-item
          style="display: none;"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <el-input v-decorator="['active']" />
        </el-form-item>

        <el-form-item
          label="应用名称"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          has-feedback
        >
          <el-input placeholder="请输入应用名称" v-decorator="['name', {rules: [{required: true, message: '请输入应用名称！'}]}]" />
        </el-form-item>

        <el-form-item
          label="唯一编码"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          has-feedback
        >
          <el-input placeholder="请输入唯一编码" v-decorator="['code', {rules: [{required: true, message: '请输入唯一编码！'}]}]" />
        </el-form-item>
      </el-form>

    </a-spin>
  </el-dialog>
</template>

<script>
  import { sysAppEdit } from '@/api/modular/system/appManage'

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
        visibleDef: false,
        form: this.$form.createForm(this)
      }
    },
    methods: {

      // 初始化方法
      edit (record) {
        this.visible = true
        setTimeout(() => {
          this.form.setFieldsValue(
            {
              id: record.id,
              name: record.name,
              code: record.code,
              active: record.active
            }
          )
        }, 100)
      },

      handleSubmit () {
        const { form: { validateFields } } = this
        this.confirmLoading = true
        validateFields((errors, values) => {
          if (!errors) {
            sysAppEdit(values).then((res) => {
              if (res.success) {
                this.$message.success('编辑成功')
                this.visible = false
                this.confirmLoading = false
                this.$emit('ok', values)
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
