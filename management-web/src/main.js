import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import Antd from 'ant-design-vue'
import App from './App.vue'
import router from './router'
import i18n from './locales'
import 'ant-design-vue/dist/reset.css'
import '@/assets/styles/global.less'

const app = createApp(App)

// Pinia 状态管理 + 持久化插件
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

app.use(pinia)
app.use(router)
app.use(i18n)
app.use(Antd)

app.mount('#app')