# vue2-vite-tutor

#### 介绍
 
 > 初始化使用vitejs 
  ```angular2html
# npm 7+, 需要额外的双横线：
npm init @vitejs/app my-vue-app -- --template vue

# yarn
yarn create @vitejs/app my-vue-app --template vue
```
支持如下配置
- `vanilla`
- `vanilla-ts`
- `vue`
- `vue-ts`
- `react`
- `react-ts`
- `preact`
- `preact-ts`
- `lit-element`
- `lit-element-ts`
- `svelte`
- `svelte-ts`

> 注意事项:
```javascript
//  配置alias必须如下形式:
resolve: {
    alias: [
        { find: "@", replacement: resolve(__dirname, "./src") },
        { find: "@views", replacement: resolve(__dirname, "./src/views") },
        {
            find: "@components",
            replacement: path.resolve(__dirname, "./src/components"),
        },
        { find: "@utils", replacement: path.resolve(__dirname, "./src/utils") },
    ]
}



```

引入组件必须添加后缀 .vue
如下
```javascript
import Hello from "@/components/Hello.vue";
```