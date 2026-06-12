import { defineStore } from 'pinia'
import { ref } from 'vue'

/** 应用全局 Store */
export const useAppStore = defineStore(
  'app',
  () => {
    /** 侧边栏折叠 */
    const collapsed = ref(false)
    /** 当前语言 */
    const language = ref('zh-CN')
    /** 当前主题模式 */
    const theme = ref('light')

    function toggleCollapsed() {
      collapsed.value = !collapsed.value
    }

    function setLanguage(lang) {
      language.value = lang
    }

    function toggleTheme() {
      theme.value = theme.value === 'light' ? 'dark' : 'light'
    }

    return {
      collapsed,
      language,
      theme,
      toggleCollapsed,
      setLanguage,
      toggleTheme,
    }
  },
  {
    persist: {
      key: 'rbac-app',
    },
  },
)