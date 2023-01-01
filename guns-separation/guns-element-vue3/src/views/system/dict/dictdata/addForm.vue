<template>
  <el-dialog
    title="新增字典值"
    :width="900"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
    @cancel="handleCancel"
  >
    <a-spin :spinning="confirmLoading">
      <el-form :form="form">
        <el-form-item
          style="display: none;"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          has-feedback
        >
          <el-input v-decorator="['typeId']" />
        </el-form-item>

        <el-form-item
          label="字典值"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          has-feedback
        >
          <el-input placeholder="请输入字典值" v-decorator="['value', {rules: [{required: true, message: '请输入字典值！'}]}]" />
        </el-form-item>

        <el-form-item
          label="唯一编码"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          has-feedback
        >
          <el-input placeholder="请输入唯一编码" v-decorator="['code', {rules: [{required: true, message: '请输入唯一编码！'}]}]" />
        </el-form-item>

        <el-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="排序"
        >
          <el-input-number placeholder="请输入排序" style="width: 100%" v-decorator="['sort', { initialValue: 100 }]" :min="1" :max="1000" />
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
  import { sysDictDataAdd } from '@/api/modular/system/dictDataManage'
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
        form: this.$form.createForm(this)
      }
    },
    methods: {
      // 初始化方法
      add (record) {
        this.visible = true
        // 增加上级类型ID
        setTimeout(() => {
          this.form.setFieldsValue(
            {
              typeId: record
            }
          )
        }, 100)
      },

      handleSubmit () {
        const { form: { validateFields } } = this
        this.confirmLoading = true
        validateFields((errors, values) => {
          if (!errors) {
            sysDictDataAdd(values).then((res) => {
              if (res.success) {
                this.$message.success('新增成功')
                this.visible = false
                this.confirmLoading = false
                this.$emit('ok', values)
                this.form.resetFields()
              } else {
                this.$message.error('新增失败：' + res.message)
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
