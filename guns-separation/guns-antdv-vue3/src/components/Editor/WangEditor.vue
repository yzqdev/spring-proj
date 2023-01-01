<template>
  <div>
    <div id="editor" ref="myEditor"></div>
    <slot></slot>
  </div>
</template>
<script>
  import WangEditor from 'wangeditor'
  export default {
    name: 'ComponentWangeditor',
    data () {
      return {
        edit: '',
        editor:null,
      }
    },
    props: {
      value: {
        type: String,
        default: ''
      },
      config: {
        type: [Array,Object],
        default: () => {
          return {}
        }
      },
      uploadConfig: {
        type: [Array,Object],
        default: () => {
          return {
            method: 'http', // 支持custom(objurl)和http(服务器)和base64
            url: '/'
          }
        }
      }
    },
    computed: {
      customConfig () {
        return {
          // pasteFilterStyle: false, // 关闭掉粘贴样式的过滤
          // pasteIgnoreImg: false, // 粘贴时不忽略图片
          ...this.config
        }
      }
    },
    watch: {

    },
    components: {

    },
    methods: {
      readBlobAsDataURL (blob, callback) {
        let a = new FileReader()
        a.onload = function (e) { callback(e.target.result) }
        a.readAsDataURL(blob)
      },
      initEditor () {
        let self = this

        // this.editor = new WangEditor('#editor')
        this.editor = new WangEditor(this.$refs.myEditor)

        //这里直接等会把默认的覆盖掉 important!
        this.editor.config = Object.assign(this.editor.config,this.customConfig)
        this.editor.config.uploadImgMaxLength = 5


        this.editor.config.onchange = function (newHtml) { // 编辑区域内容变化时
          console.log(newHtml)
          console.log(self.editor.txt)
          console.log(`%ceditor内部`,`color:red;font-size:16px;background:transparent`)
          self.$emit('input', newHtml)
          self.$emit('onchange', newHtml, self.editor.txt )

        }

        this.editor.config.customUploadImg = function (files, insert) {
          if (self.uploadConfig.method === 'custom') {
            files.forEach(file => {
              let fileUrl = URL.createObjectURL(file)
              insert(fileUrl)
            })
          }
          if (self.uploadConfig.method === 'base64') {
            files.forEach(file => {
              self.readBlobAsDataURL(file, function (dataurl) {
                insert(dataurl)
              })
            })
          }
          if (self.uploadConfig.method === 'http') {
            if (self.uploadConfig.callback) {
              self.uploadConfig.callback(files, insert)
            } else {
              let formData = new FormData()
              files.forEach(file => {
                formData.append('file', file)
              })
              self.$axios.post(self.uploadConfig.url, formData).then(({ data }) => {
                if (data.status === 'success') {
                  insert(data.url)
                }
              })
            }
          }
        }
        this.editor.create() // 生成编辑器
        this.editor.txt.text(this.value) // 生成编辑器
        this.$emit('oninit', this.editor)
      }
    },
    beforeCreate () {
    },
    created () {
    },
    beforeMount () {
    }, beforeDestroy() {
      // 调用销毁 API 对当前编辑器实例进行销毁
      this.editor.destroy()
      this.editor = null
    },
    mounted () {
      this.initEditor()
    }
  }
</script>

<style >
  .w-e-toolbar{
    flex-wrap:wrap;
  }

</style>
