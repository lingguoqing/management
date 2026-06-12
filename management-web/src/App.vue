<script setup>
import { useAppStore } from '@/stores/app'
import zhCN from 'ant-design-vue/es/locale/zh_CN'
import enUS from 'ant-design-vue/es/locale/en_US'
import { computed } from 'vue'

const appStore = useAppStore()

const locale = computed(() => (appStore.language === 'zh-CN' ? zhCN : enUS))

const themeConfig = computed(() => ({
  token: {
    colorPrimary: '#1a1a2e',
    borderRadius: 8,
    fontFamily: "'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif",
  },
}))
</script>

<template>
  <a-config-provider :locale="locale" :theme="themeConfig">
    <router-view />
  </a-config-provider>
</template>