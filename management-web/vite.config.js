import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import {resolve} from 'path'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import {AntDesignVueResolver} from 'unplugin-vue-components/resolvers'

export default defineConfig({
    plugins: [
        vue(),
        // 自动导入 Vue API（ref, reactive, computed 等）
        AutoImport({
            imports: ['vue', 'vue-router', 'pinia', 'vue-i18n'],
            dts: 'src/auto-imports.d.ts',
        }),
        // 自动导入 Ant Design Vue 组件
        Components({
            resolvers: [AntDesignVueResolver({importStyle: 'less'})],
            dts: 'src/components.d.ts',
        }),
    ],
    resolve: {
        alias: {
            '@': resolve(__dirname, 'src'),
        },
    },
    css: {
        preprocessorOptions: {
            less: {
                javascriptEnabled: true,
                // Ant Design 5 主题定制（Perplexity 风格）
                modifyVars: {
                    'primary-color': '#1a1a2e',
                    'border-radius-base': '8px',
                    'font-family': "'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif",
                },
            },
        },
    },
    server: {
        host: '0.0.0.0',
        port: 5173,
        proxy: {
            '/api': {
                target: 'http://localhost:5631',
                changeOrigin: true,
            },
        },
    },
})