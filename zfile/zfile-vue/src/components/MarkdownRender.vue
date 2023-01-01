<template>
  <div>
    <div class="markdown-body">
      <md-editor-v3 v-model="text" />
    </div>
    <template>
      <el-backtop target=".markdown-body" :bottom="20" :right="30">
        <el-tooltip placement="top" content="回到顶部">
          <transition name="fade">
            <div class="back-to-ceiling">
              <svg class="Icon Icon--backToTopArrow" aria-hidden="true">
                <use xlink:href="#el-icon-my-to-top"></use>
              </svg>
            </div>
          </transition>
        </el-tooltip>
      </el-backtop>
    </template>
  </div>
</template>

<script setup>
let props = defineProps({
  text: String,
});

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
</script>

<style lang="scss" scoped>
:deep(.markdown-body) {
  box-sizing: border-box;
  min-width: 200px;
  max-width: 980px;
  margin: 0 auto;
  padding: 30px;
  img {
    max-height: 150vh;
    max-width: 150vh;
    vertical-align: middle;
  }
  pre {
    background-color: rgba(27, 31, 35, 0.05);
  }
}

.back-to-ceiling {
  right: 50px;
  bottom: 50px;
  width: 40px;
  height: 40px;
  border-radius: 4px;
  line-height: 45px;
  background: #e7eaf1;
  display: inline-block;
  text-align: center;
  cursor: pointer;
}

.back-to-ceiling:hover {
  background: #d5dbe7;
}

.back-to-ceiling .Icon {
  fill: #9aaabf;
  background: none;
}

.back-to-ceiling .Icon--backToTopArrow {
  height: 16px;
  width: 16px;
}
</style>
