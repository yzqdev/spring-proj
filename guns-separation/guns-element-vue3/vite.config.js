// vite.config.js
const resolve = path.resolve
import { defineConfig } from 'vite'
import vueJsx from '@vitejs/plugin-vue-jsx'
import vue from '@vitejs/plugin-vue'
import path from 'path'
export default defineConfig({
  plugins: [
    vue(),
    vueJsx({
      // options are passed on to @vue/babel-plugin-jsx
    }),
  ],
  css: {
    preprocessorOptions: {
      less: {
        javascriptEnabled: true,
      },
    },
  },
  resolve: {
    extensions: ['.vue', '.mjs', '.js', '.ts', '.jsx', '.tsx', '.json'],
    //导入时想要省略的扩展名列表。注意，不 建议忽略自定义导入类型的扩展名（例如：.vue），因为它会干扰 IDE 和类型支持。
    alias: [
      { find: '@', replacement: resolve(__dirname, './src') },
      { find: '@views', replacement: resolve(__dirname, './src/views') },
      {
        find: '@components',
        replacement: path.resolve(__dirname, './src/components'),
      },
      { find: '@utils', replacement: path.resolve(__dirname, './src/utils') },
    ],
  },
  build: {
    // sourcemap: true,
    minify: false,
  },
  server: {
    port: 8920, fs: {
      // Allow serving files from one level up to the project root
      allow: ['..']
    }
  },
})

// 这里的 options 可配置 vueTemplateOptions， jsx
// 详情查看 https://github.com/underfin/vite-plugin-vue2
