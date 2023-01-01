<template>
  <div class="content" v-loading="loading" element-loading-text="拼命加载中">
    <div
      id="container"
      class="editor"
      v-if="getFileSuffix(file.name) !== 'md'"
    ></div>

    <div
      class="dialog-scroll markdown-body"
      v-if="getFileSuffix(file.name) === 'md'"
    >
      <md-editor-v3 preview-only v-model="text"></md-editor-v3>
    </div>
  </div>
</template>

<script>
import * as monaco from "monaco-editor";
import "./useWorker";
export default {
  name: "TextPreview",
  components: {},
  data() {
    return {
      text: "",
      loading: true,
      editor: "",
    };
  },
  props: {
    file: Object,
  },
  mounted() {
    let file = this.file;

    this.$http
      .get(file.src, { withCredentials: false })
      .then((response) => {
        this.loading = false;
        this.text = response.data;
        this.initMonaco();
      })
      .catch(() => {
        this.$http
          .get("/common/content", { params: { url: file.src } })
          .then((response) => {
            this.loading = false;
            this.text = response.data.data;
            this.initMonaco();
          });
      });
  },
  methods: {
    getFileSuffix(name) {
      let suffix = name
        .substring(name.lastIndexOf(".") + 1)
        .toLocaleLowerCase();
      console.log("拓展名是", suffix);
      let langMap = new Map([
        ["css", "css"],
        ["xml", "xml"],
        ["yml", "yml"],
        ["html", "html"],
        ["less", "less"],
        ["php", "php"],
        ["sh", "shell"],
        ["js", "javascript"],
        ["ts", "typescript"],
        ["json", "json"],
        ["java", "java"],
        ["go", "go"],
        ["py", "python"],
        ["cs", "csharp"],
      ]);
      return langMap.get(suffix) ? langMap.get(suffix) : suffix;
    },
    initMonaco() {
      if (this.getFileSuffix(this.file.name) !== "md") {
        monaco.editor.create(document.getElementById("container"), {
          value: this.text,
          language: this.getFileSuffix(this.file.name),
          theme: "vs",
        });
      }
    },
  },
  computed: {
    // markdownHtml() {
    //     // url 新窗口打开.
    //     let renderer = new marked.Renderer();
    //     renderer.link = function() {
    //         let link = marked.Renderer.prototype.link.apply(this, arguments);
    //         return link.replace("<a","<a target='_blank'");
    //     };
    //     marked.setOptions({
    //         renderer: renderer
    //     });
    //
    //     return marked(this.text, {
    //         highlight: function(code) {
    //             return hljs.highlightAuto(code).value;
    //         }
    //     });
    // }
  },
};
</script>

<style lang="scss" scoped>
.content {
  img {
    max-height: 150vh;
    max-width: 150vh;
    vertical-align: middle;
  }
  .markdown-body {
    pre {
      margin-right: 20px;
      background-color: rgba(27, 31, 35, 0.05);
    }
  }
}

.dialog-scroll {
  height: calc(100vh - 5vh - 54px - 55px - 5vh);
  overflow-y: auto;
  margin: 0;
}

.editor {
  width: 100%;
  height: 80vh;
}
</style>
