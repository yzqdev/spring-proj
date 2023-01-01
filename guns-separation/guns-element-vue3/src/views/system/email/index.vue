<template>
  <el-card :bordered="false">

    <a-spin :spinning="confirmLoading">
      <el-tabs default-active-key="1" >
        <el-tab-pane key="1" tab="发送邮件" @change="tabsCallback" v-if="hasPerm('email:sendEmail')">
          <el-form :form="form1">
            <el-form-item
              label="收件邮箱"
            >
              <el-input placeholder="请输入收件邮箱" v-decorator="['to', {rules: [{type: 'email',message: '请输入正确的邮箱!'},{required: true, message: '请输入收件邮箱！'}]}]" />
            </el-form-item>
            <el-form-item
              label="邮件标题"
            >
              <el-input placeholder="请输入邮件标题" v-decorator="['title', {rules: [{required: true, message: '请输入邮件标题！'}]}]" />
            </el-form-item>
            <el-form-item
              label="邮件内容"
            >
              <a-textarea :rows="4" placeholder="请输入备注" v-decorator="['content', {rules: [{required: true, message: '请输入邮件内容！'}]}]"></a-textarea>
            </el-form-item>
            <el-form-item class="subForm-item">
              <el-button type="primary" @click="handleSubmit1" :loading="confirmLoading">发送</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane key="2" tab="发送Html邮件" @change="tabsCallback" v-if="hasPerm('email:sendEmailHtml')">
          <el-form :form="form2">
            <el-form-item
              label="收件邮箱"
            >
              <el-input placeholder="请输入收件邮箱" v-decorator="['to',{rules: [ {type: 'email',message: '请输入正确的邮箱!'},{required: true, message: '请输入收件邮箱！'}]}]" />
            </el-form-item>
            <el-form-item
              label="邮件标题"
            >
              <el-input placeholder="请输入邮件标题" v-decorator="['title', {rules: [{required: true, message: '请输入邮件标题！'}]}]" />
            </el-form-item>
            <el-form-item
              label="邮件内容"
            >
              <antd-editor :uploadConfig="editorUploadConfig" v-model="editorContent" @onchange="changeEditor" @oninit="getEditor" />
            </el-form-item>
            <el-form-item class="subForm-item">
              <el-button type="primary" @click="handleSubmit2" :loading="confirmLoading">发送</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>

    </a-spin>

  </el-card>
</template>

<script>
  import { emailSendEmail, emailSendEmailHtml } from '@/api/modular/system/emailManage'
  import { AntdEditor } from '@/components'
  // eslint-disable-next-line no-unused-vars
  import { sysFileInfoUpload, sysFileInfoDownload } from '@/api/modular/system/fileManage'

  export default {
    components: {
      AntdEditor
    },

    data () {
      return {
        editor:null,
        editorContentText: '',
        editorUploadConfig: {
          method: 'http',
          callback: this.editorUploadImage
        },
        confirmLoading: false,
        editorContent: '',
        form1: this.$form.createForm(this),
        form2: this.$form.createForm(this)
      }
    },

    methods: {

      tabsCallback (key) {
        if (key === '1') {
          // eslint-disable-next-line no-labels
          form1: this.$form.createForm(this)
          this.form2.resetFields()
          this.editor.txt.clear()
        }
        if (key === '2') {
          // eslint-disable-next-line no-labels
          form2: this.$form.createForm(this)
          this.form1.resetFields()
        }
      },
      /**
       * 编辑器回调上传及回传图片url
       */
      editorUploadImage (files, insert) {
        const formData = new FormData()
        files.forEach(file => {
          formData.append('file', file)
        })
        sysFileInfoUpload(formData).then((res) => {
          if (res.success) {
            insert(import.meta.env.VITE_APP_API_BASE_URL + '/sysFileInfo/preview?id=' + res.data)
          } else {
            this.$message.error('编辑器上传图片失败：' + res.message)
          }
        })
      },
      getEditor (editor) {
        this.editor = editor
      },
      changeEditor (html, ele) {
        console.log('html=',html)
        console.log("ele=",ele)
        console.log(`%cemail改变嫦娥`,`color:red;font-size:16px;background:transparent`)
        this.editorContent = html
        this.editorContentText = ele.text()
      },

      /**
       * 发送邮件
       */
      handleSubmit1 () {
        const { form1: { validateFields } } = this
        this.confirmLoading = true
        validateFields((errors, values) => {
          if (!errors) {
            emailSendEmail(values).then((res) => {
              if (res.success) {
                this.$message.success('发送成功')
                this.confirmLoading = false
                this.form1.resetFields()
              } else {
                this.$message.error('发送失败：' + res.message)
              }
            }).finally((res) => {
              this.confirmLoading = false
            })
          } else {
            this.confirmLoading = false
          }
        })
      },
      /**
       * 发送Html邮件
       */
      handleSubmit2 () {
        const { form2: { validateFields } } = this
        // eslint-disable-next-line eqeqeq
        if (this.editorContent == '') {
          this.$message.error('请填写邮件内容')
          return
        }
        this.confirmLoading = true
        validateFields((errors, values) => {
          if (!errors) {
            values.content = this.editorContent
            emailSendEmailHtml(values).then((res) => {
              if (res.success) {
                this.$message.success('发送成功')
                this.confirmLoading = false
                this.editor.txt.clear()
                this.form2.resetFields()
              } else {
                this.$message.error('发送失败：' + res.message)
              }
            }).finally((res) => {
              this.confirmLoading = false
            })
          } else {
            this.confirmLoading = false
          }
        })
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
