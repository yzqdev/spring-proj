<template>
  <el-dialog
    title="新增参数"
    :width="900"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
    @cancel="handleCancel"
  >
    <a-spin :spinning="formLoading">
      <el-form :form="form">
        <el-form-item
          label="参数名称"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          has-feedback
        >
          <el-input placeholder="请输入参数名称" v-decorator="['name', {rules: [{required: true, message: '请输入参数名称！'}]}]" />
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
          label="系统参数"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <el-radio-group v-decorator="['sysFlag',{rules: [{ required: true, message: '请选择是否为系统参数！' }]}]" >
            <el-radio-button value="Y" > 是 </el-radio-button>
            <el-radio-button value="N" >  否 </el-radio-button>
          </el-radio-group>
        </el-form-item>

        <el-form-item
          label="所属分类"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          has-feedback
        >
          <el-select style="width: 100%" placeholder="请选择所属分类" v-decorator="['groupCode', {rules: [{ required: true, message: '请选择取所属分类！' }]}]" >
            <el-option v-for="(item,index) in groupCodeList" :key="index" :value="item.code" >{{ item.value }}</el-option>
          </el-select>
        </el-form-item>

        <el-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="参数值"
        >
          <el-input placeholder="请输入参数值" v-decorator="['value', {rules: [{required: true, message: '请输入参数值！'}]}]" />
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
  import { sysDictTypeDropDown, sysConfigAdd } from '@/api/modular/system/configManage'
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
        formLoading: true,
        groupCodeList: [],
        form: this.$form.createForm(this)
      }
    },
    methods: {
      // 初始化方法
      add () {
        this.visible = true
        this.sysDictTypeDropDown()
      },

      /**
       * 获取所属分类
       */
      sysDictTypeDropDown () {
        sysDictTypeDropDown({ code: 'consts_type' }).then((res) => {
          this.groupCodeList = res.data
          this.formLoading = false
        })
      },

      handleSubmit () {
        const { form: { validateFields } } = this
        this.confirmLoading = true
        validateFields((errors, values) => {
          if (!errors) {
            sysConfigAdd(values).then((res) => {
              this.confirmLoading = false
              if (res.success) {
                this.$message.success('新增成功')
                this.$emit('ok', values)
                this.handleCancel()
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
