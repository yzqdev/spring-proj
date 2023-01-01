<template>
  <el-dialog
    title="日志详情"
    :width="900"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel"
  >
    <a-spin :spinning="confirmLoading">
      <el-form :form="form">
        <el-row :gutter="24">
          <el-col :md="12" :sm="24">
            <el-form-item
              label="方法名称"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              <el-input v-decorator="['methodName']" />
            </el-form-item>
          </el-col>
          <el-col :md="12" :sm="24">
            <el-form-item
              label="地址"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              <el-input v-decorator="['location']" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :md="12" :sm="24">
            <el-form-item
              label="浏览器"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              <el-input v-decorator="['browser']" />
            </el-form-item>
          </el-col>
          <el-col :md="12" :sm="24">
            <el-form-item
              label="操作系统"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              <el-input v-decorator="['os']" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :md="12" :sm="24">
            <el-form-item
              label="类名称"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              <a-textarea :rows="4" v-decorator="['className']"/>
            </el-form-item>
          </el-col>
          <el-col :md="12" :sm="24">
            <el-form-item
              label="具体消息"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              <a-textarea :rows="4" v-decorator="['message']"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :md="12" :sm="24">
            <el-form-item
              label="请求参数"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              <a-textarea :rows="4" v-decorator="['param']"/>
            </el-form-item>
          </el-col>
          <el-col :md="12" :sm="24">
            <el-form-item
              label="返回结果"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
            >
              <a-textarea :rows="4" v-decorator="['result']"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </a-spin>
  </el-dialog>
</template>
<script>
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
      details (record) {
        this.visible = true
        setTimeout(() => {
          this.form.setFieldsValue(
            {
              location: record.location,
              browser: record.browser,
              os: record.os,
              className: record.className,
              methodName: record.methodName,
              param: record.param,
              result: record.result,
              message: record.message
            }
          )
        }, 100)
      },
      handleCancel () {
        this.form.resetFields()
        this.visible = false
      }
    }
  }
</script>
