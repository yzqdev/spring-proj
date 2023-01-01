<template>
  <div>
    <div style="border: 1px solid #ccc; margin-top: 10px;">
      <!-- 工具栏 -->
      <Toolbar
        style="border-bottom: 1px solid #ccc"
        :editor="editor"
        :defaultConfig="toolbarConfig"
      />
      <!-- 编辑器 -->
      <Editor
        style="height: 400px; overflow-y: hidden;"
        :defaultConfig="editorConfig"
        v-model="html"
        @onChange="onChange"
        @onCreated="onCreated"
      />
    </div>
    <slot></slot>
  </div>
</template>
<script>
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import '@wangeditor/editor/dist/css/style.css'
  export default {
    name: 'ComponentWangeditor',

    data () {
      return {
        edit: '',
        editor:null,
        html: '',
        toolbarConfig: {
          // toolbarKeys: [ /* 显示哪些菜单，如何排序、分组 */ ],
          // excludeKeys: [ /* 隐藏哪些菜单 */ ],
        },
        editorConfig: {
          placeholder: '请输入内容...',
          // autoFocus: false,
          // 所有的菜单配置，都要在 MENU_CONF 属性下
          MENU_CONF: {}
        }
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
    components: { Editor, Toolbar },
    methods: {  onCreated(editor) {
        this.editor = Object.seal(editor) // 【注意】一定要用 Object.seal() 否则会报错
      },
      onChange(editor) {
        console.log('onChange', editor.getHtml())
        this.$emit('input', editor.getHtml())
        this.$emit('onchange', editor.getHtml(), editor.getText() )
        // onChange 时获取编辑器最新内容
      },
      readBlobAsDataURL (blob, callback) {
        let a = new FileReader()
        a.onload = function (e) { callback(e.target.result) }
        a.readAsDataURL(blob)
      },
      initEditor () {
        let self = this

        // this.editor = new WangEditor('#editor')

        //这里直接等会把默认的覆盖掉 important!
        this.editorConfig = Object.assign(this.editorConfig,this.customConfig)
        this.editorConfig.uploadImgMaxLength = 5



        this.editorConfig.MENU_CONF['uploadImage'] = {
          // 上传之前触发
          async customUpload(file,insertFn) {
            // file 选中的文件，格式如 { key: file }

            if (self.uploadConfig.method === 'custom') {
              file.forEach(file => {
                let fileUrl = URL.createObjectURL(file)
                insertFn(fileUrl)
              })
            }
            if (self.uploadConfig.method === 'base64') {
              file.forEach(file => {
                self.readBlobAsDataURL(file, function (dataurl) {
                  insertFn(dataurl)
                })
              })
            }
            if (self.uploadConfig.method === 'http') {
              if (self.uploadConfig.callback) {
                self.uploadConfig.callback(file, insertFn)
              } else {
                let formData = new FormData()
                file.forEach(file => {
                  formData.append('file', file)
                })
                self.$axios.post(self.uploadConfig.url, formData).then(({ data }) => {
                  if (data.status === 'success') {
                    insertFn(data.url)
                  }
                })
              }
            }

            return file

            // 可以 return
            // 1. return file 或者 new 一个 file ，接下来将上传
            // 2. return false ，不上传这个 file
          },
          // 上传进度的回调函数
          onProgress(progress ) {
            // progress 是 0-100 的数字
            console.log('progress', progress)
          },
          // 单个文件上传成功之后
          onSuccess(file , res ) {
            console.log(`${file.name} 上传成功`, res)
          },
          // 单个文件上传失败
          onFailed(file , res ) {
            console.log(`${file.name} 上传失败`, res)
          },
          // 上传错误，或者触发 timeout 超时
          onError(file , err , res ) {
            console.log(`${file.name} 上传出错`, err, res)
          },
        }
        this.$emit('oninit', this.editor)
      },
      clear(){

      }
    },
    beforeCreate () {
    },
    created () {
    },
    beforeMount () {
    }, beforeDestroy() {
      const editor = this.editor
      if (editor == null) return
      editor.destroy() // 组件销毁时，及时销毁 editor ，重要！！！
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
