// vite.config.ts
import { defineConfig, loadEnv } from "vite";
import path from "path";
import vue from "@vitejs/plugin-vue";
import Components from "unplugin-vue-components/vite";
import { VantResolver } from "unplugin-vue-components/resolvers";
var __vite_injected_original_dirname = "D:\\CC.Yi\\CC.Yi\\Yi.Vue3.x.Vant";
var vite_config_default = defineConfig(
  ({ mode, command }) => {
    const env = loadEnv(mode, process.cwd());
    const { VITE_APP_ENV, VITE_APP_BASE_URL } = env;
    return {
      plugins: [vue(), Components({
        resolvers: [VantResolver()]
      })],
      resolve: {
        alias: {
          "~": path.resolve(__vite_injected_original_dirname, "./"),
          "@": path.join(__vite_injected_original_dirname, "./src")
        }
      },
      server: {
        port: 17e3,
        host: true,
        open: true,
        proxy: {
          "/dev-api": {
            target: VITE_APP_BASE_URL,
            changeOrigin: true,
            rewrite: (p) => p.replace(/^\/dev-api/, "")
          },
          "/dev-ws": {
            target: VITE_APP_BASE_URL,
            changeOrigin: true,
            rewrite: (p) => p.replace(/^\/dev-ws/, ""),
            ws: true
          }
        }
      }
    };
  }
);
export {
  vite_config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5jb25maWcudHMiXSwKICAic291cmNlc0NvbnRlbnQiOiBbImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJEOlxcXFxDQy5ZaVxcXFxDQy5ZaVxcXFxZaS5WdWUzLnguVmFudFwiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9maWxlbmFtZSA9IFwiRDpcXFxcQ0MuWWlcXFxcQ0MuWWlcXFxcWWkuVnVlMy54LlZhbnRcXFxcdml0ZS5jb25maWcudHNcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfaW1wb3J0X21ldGFfdXJsID0gXCJmaWxlOi8vL0Q6L0NDLllpL0NDLllpL1lpLlZ1ZTMueC5WYW50L3ZpdGUuY29uZmlnLnRzXCI7aW1wb3J0IHsgZGVmaW5lQ29uZmlnLCBsb2FkRW52IH0gZnJvbSAndml0ZSdcclxuaW1wb3J0IHBhdGggZnJvbSAncGF0aCdcclxuaW1wb3J0IHZ1ZSBmcm9tICdAdml0ZWpzL3BsdWdpbi12dWUnXHJcbmltcG9ydCBDb21wb25lbnRzIGZyb20gJ3VucGx1Z2luLXZ1ZS1jb21wb25lbnRzL3ZpdGUnO1xyXG5pbXBvcnQgeyBWYW50UmVzb2x2ZXIgfSBmcm9tICd1bnBsdWdpbi12dWUtY29tcG9uZW50cy9yZXNvbHZlcnMnO1xyXG5cclxuXHJcbi8vIGh0dHBzOi8vdml0ZWpzLmRldi9jb25maWcvXHJcbmV4cG9ydCBkZWZhdWx0IGRlZmluZUNvbmZpZygoeyBtb2RlLCBjb21tYW5kIH0pID0+IHtcclxuICBjb25zdCBlbnYgPSBsb2FkRW52KG1vZGUsIHByb2Nlc3MuY3dkKCkpXHJcbiAgY29uc3QgeyBWSVRFX0FQUF9FTlYsIFZJVEVfQVBQX0JBU0VfVVJMfSA9IGVudlxyXG4gIHJldHVybiB7XHJcbiAgICBwbHVnaW5zOiBbdnVlKCksIENvbXBvbmVudHMoe1xyXG4gICAgICByZXNvbHZlcnM6IFtWYW50UmVzb2x2ZXIoKV0sXHJcbiAgICB9KSxdLFxyXG4gICAgcmVzb2x2ZToge1xyXG4gICAgICAvLyBodHRwczovL2NuLnZpdGVqcy5kZXYvY29uZmlnLyNyZXNvbHZlLWFsaWFzXHJcbiAgICAgIGFsaWFzOiB7XHJcbiAgICAgICAgLy8gXHU4QkJFXHU3RjZFXHU4REVGXHU1Rjg0XHJcbiAgICAgICAgJ34nOiBwYXRoLnJlc29sdmUoX19kaXJuYW1lLCAnLi8nKSxcclxuICAgICAgICAvLyBcdThCQkVcdTdGNkVcdTUyMkJcdTU0MERcclxuICAgICAgICAvLyAnQCc6IHBhdGgucmVzb2x2ZShfX2Rpcm5hbWUsICcuL3NyYycpLFxyXG4gICAgICAgIFwiQFwiOiBwYXRoLmpvaW4oX19kaXJuYW1lLCBcIi4vc3JjXCIpLFxyXG4gICAgICB9XHJcbiAgICB9LFxyXG4gICAgc2VydmVyOiB7XHJcbiAgICAgIHBvcnQ6IDE3MDAwLFxyXG4gICAgICBob3N0OiB0cnVlLFxyXG4gICAgICBvcGVuOiB0cnVlLFxyXG4gIFxyXG4gICAgICBcclxuICAgICAgcHJveHk6IHtcclxuICAgICAgICAvLyBodHRwczovL2NuLnZpdGVqcy5kZXYvY29uZmlnLyNzZXJ2ZXItcHJveHlcclxuICAgICAgICAnL2Rldi1hcGknOiB7XHJcbiAgICAgICAgICB0YXJnZXQ6IFZJVEVfQVBQX0JBU0VfVVJMLFxyXG4gICAgICAgICAgY2hhbmdlT3JpZ2luOiB0cnVlLFxyXG4gICAgICAgICAgcmV3cml0ZTogKHApID0+IHAucmVwbGFjZSgvXlxcL2Rldi1hcGkvLCAnJyksXHJcbiAgICAgICAgfSxcclxuICBcclxuICAgICAgICAnL2Rldi13cyc6IHtcclxuICAgICAgICAgIHRhcmdldDogVklURV9BUFBfQkFTRV9VUkwsXHJcbiAgICAgICAgICBjaGFuZ2VPcmlnaW46IHRydWUsXHJcbiAgICAgICAgICByZXdyaXRlOiAocCkgPT4gcC5yZXBsYWNlKC9eXFwvZGV2LXdzLywgJycpLFxyXG4gICAgICAgICAgd3M6IHRydWVcclxuICAgICAgICB9XHJcbiAgXHJcbiAgICAgIH1cclxuICAgIH0sXHJcbiAgfVxyXG5cclxufVxyXG5cclxuKVxyXG4iXSwKICAibWFwcGluZ3MiOiAiO0FBQWlSLFNBQVMsY0FBYyxlQUFlO0FBQ3ZULE9BQU8sVUFBVTtBQUNqQixPQUFPLFNBQVM7QUFDaEIsT0FBTyxnQkFBZ0I7QUFDdkIsU0FBUyxvQkFBb0I7QUFKN0IsSUFBTSxtQ0FBbUM7QUFRekMsSUFBTyxzQkFBUTtBQUFBLEVBQWEsQ0FBQyxFQUFFLE1BQU0sUUFBUSxNQUFNO0FBQ2pELFVBQU0sTUFBTSxRQUFRLE1BQU0sUUFBUSxJQUFJLENBQUM7QUFDdkMsVUFBTSxFQUFFLGNBQWMsa0JBQWlCLElBQUk7QUFDM0MsV0FBTztBQUFBLE1BQ0wsU0FBUyxDQUFDLElBQUksR0FBRyxXQUFXO0FBQUEsUUFDMUIsV0FBVyxDQUFDLGFBQWEsQ0FBQztBQUFBLE1BQzVCLENBQUMsQ0FBRTtBQUFBLE1BQ0gsU0FBUztBQUFBLFFBRVAsT0FBTztBQUFBLFVBRUwsS0FBSyxLQUFLLFFBQVEsa0NBQVcsSUFBSTtBQUFBLFVBR2pDLEtBQUssS0FBSyxLQUFLLGtDQUFXLE9BQU87QUFBQSxRQUNuQztBQUFBLE1BQ0Y7QUFBQSxNQUNBLFFBQVE7QUFBQSxRQUNOLE1BQU07QUFBQSxRQUNOLE1BQU07QUFBQSxRQUNOLE1BQU07QUFBQSxRQUdOLE9BQU87QUFBQSxVQUVMLFlBQVk7QUFBQSxZQUNWLFFBQVE7QUFBQSxZQUNSLGNBQWM7QUFBQSxZQUNkLFNBQVMsQ0FBQyxNQUFNLEVBQUUsUUFBUSxjQUFjLEVBQUU7QUFBQSxVQUM1QztBQUFBLFVBRUEsV0FBVztBQUFBLFlBQ1QsUUFBUTtBQUFBLFlBQ1IsY0FBYztBQUFBLFlBQ2QsU0FBUyxDQUFDLE1BQU0sRUFBRSxRQUFRLGFBQWEsRUFBRTtBQUFBLFlBQ3pDLElBQUk7QUFBQSxVQUNOO0FBQUEsUUFFRjtBQUFBLE1BQ0Y7QUFBQSxJQUNGO0FBQUEsRUFFRjtBQUVBOyIsCiAgIm5hbWVzIjogW10KfQo=
