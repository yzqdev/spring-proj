import { defineConfig, loadEnv } from 'vite'
import path from 'path'
import vue from '@vitejs/plugin-vue'
import Components from 'unplugin-vue-components/vite';
import { VantResolver } from 'unplugin-vue-components/resolvers';


// https://vitejs.dev/config/
export default defineConfig(({ mode, command }) => {
  const env = loadEnv(mode, process.cwd())
  const { VITE_APP_ENV, VITE_APP_BASE_URL} = env
  return {
    plugins: [vue(), Components({
      resolvers: [VantResolver()],
    }),],
    resolve: {
      // https://cn.vitejs.dev/config/#resolve-alias
      alias: {
        // 设置路径
        '~': path.resolve(__dirname, './'),
        // 设置别名
        // '@': path.resolve(__dirname, './src'),
        "@": path.join(__dirname, "./src"),
      }
    },
    server: {
      port: 17000,
      host: true,
      open: true,
  
      
      proxy: {
        // https://cn.vitejs.dev/config/#server-proxy
        '/dev-api': {
          target: VITE_APP_BASE_URL,
          changeOrigin: true,
          rewrite: (p) => p.replace(/^\/dev-api/, ''),
        },
  
        '/dev-ws': {
          target: VITE_APP_BASE_URL,
          changeOrigin: true,
          rewrite: (p) => p.replace(/^\/dev-ws/, ''),
          ws: true
        }
  
      }
    },
  }

}

)
