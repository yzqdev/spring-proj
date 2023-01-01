<template>
  <a-card :bordered='false'>

    <a-spin :spinning='confirmLoading'>
      <a-tabs default-active-key='1'>
        <a-tab-pane key='1' tab='发送邮件' @change='tabsCallback' v-if="hasPerm('email:sendEmail')">
          <a-form-model ref='form1' :model='form1' :rules='rules1'>
            <a-form-model-item
              label='收件邮箱' prop='email'
            >
              <a-input placeholder='请输入收件邮箱' v-model='form1.tos' />
            </a-form-model-item>
            <a-form-model-item
              label='邮件标题' prop='title'
            >
              <a-input placeholder='请输入邮件标题' v-model='form1.title' />
            </a-form-model-item>
            <a-form-model-item
              label='邮件内容' prop='content'
            >
              <a-textarea :rows='4' placeholder='请输入备注' v-model='form1.content'></a-textarea>
            </a-form-model-item>
            <a-form-model-item class='subForm-item'>
              <a-button type='primary' @click='handleSubmit1' :loading='confirmLoading'>发送</a-button>
            </a-form-model-item>
          </a-form-model>
        </a-tab-pane>
        <a-tab-pane key='2' tab='发送Html邮件' @change='tabsCallback' v-if="hasPerm('email:sendEmailHtml')">
          <a-form-model ref='form2' :model='form2' :rules='rules1'>
            <a-form-model-item
              label='收件邮箱' prop='tos'
            >
              <a-input placeholder='请输入收件邮箱'
                      v-model='form2.tos'   />
            </a-form-model-item>
            <a-form-model-item
              label='邮件标题' prop='title'
            >
              <a-input placeholder='请输入邮件标题' v-model='form2.title' />
            </a-form-model-item>
            <a-form-model-item
              label='邮件内容' prop='content'
            >
              <antd-editor :uploadConfig='editorUploadConfig' v-model='form2.content' @onchange='changeEditor'
                           @oninit='getEditor' />
            </a-form-model-item>
            <a-form-model-item class='subForm-item'>
              <a-button type='primary' @click='handleSubmit2' :loading='confirmLoading'>发送</a-button>
            </a-form-model-item>
          </a-form-model>
        </a-tab-pane>
      </a-tabs>

    </a-spin>

  </a-card>
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

  data() {
    return {
      editor: null,
      editorContentText: '',
      editorUploadConfig: {
        method: 'http',
        callback: this.editorUploadImage
      },
      confirmLoading: false,
      editorContent: '',
      form1: {},
      form2: {},
      rules1: {
        tos: [{ type: 'email', message: '请输入正确的邮箱!' }, { required: true, message: '请输入收件邮箱！' }],
        title: [{ required: true, message: '请输入邮件标题！' }],
        content: [{ required: true, message: '请输入邮件内容！' }]
      }
    }
  },

  methods: {

    tabsCallback(key) {
      if (key === '1') {
        // eslint-disable-next-line no-labels

        this.form2.resetFields()
        this.editor.destroy()
      }
      if (key === '2') {
        // eslint-disable-next-line no-labels
        this.form1.resetFields()
      }
    },
    /**
     * 编辑器回调上传及回传图片url
     */
    editorUploadImage(files, insert) {
      const formData = new FormData()
      files.forEach(file => {
        formData.append('file', file)
      })
      sysFileInfoUpload(formData).then((res) => {
        if (res.success) {
          insert(process.env.VUE_APP_API_BASE_URL + '/sysFileInfo/preview?id=' + res.data)
        } else {
          this.$message.error('编辑器上传图片失败：' + res.message)
        }
      })
    },
    getEditor(editor) {
      this.editor = editor
    },
    changeEditor(html, txt) {
      console.log('html=', html)

      console.log(`%cemail改变嫦娥`, `color:red;font-size:16px;background:transparent`)
      this.editorContent = html
      this.editorContentText = txt
    },

    /**
     * 发送邮件
     */
    handleSubmit1() {
      this.confirmLoading = true
      this.$refs.form1.validate((valid) => {
        if (valid) {
          this.form1.tos=[this.form1.tos]
          emailSendEmail(this.form1).then((res) => {
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
        }else {
          this.confirmLoading = false
        }
      })

    },
    /**
     * 发送Html邮件
     */
    handleSubmit2() {

      // eslint-disable-next-line eqeqeq
      if (this.form2.content == '') {
        this.$message.error('请填写邮件内容')
        return
      }
      this.confirmLoading = true
      this.$refs.form2.validate((valid) => {
        if (valid) {
          this.form2.tos=[this.form2.tos]
          emailSendEmailHtml(this.form2).then((res) => {
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
        }else {
          this.confirmLoading = false
        }
      })

    }

  }
}
</script>

<style lang='less'>
.table-operator {
  margin-bottom: 18px;
}

button {
  margin-right: 8px;
}

</style>
