<script setup>
import { useI18n } from 'vue-i18n'
import { BuildOutlined } from '@ant-design/icons-vue'

const { t } = useI18n()
</script>

<template>
  <div class="content-page">
    <div class="developing-container">
      <a-result
        status="info"
        :title="t('common.developing') || '页面开发中'"
        :sub-title="t('common.developingTip') || '该功能正在开发中，敬请期待...'"
      >
        <template #icon>
          <BuildOutlined style="font-size: 64px; color: #1890ff" />
        </template>
      </a-result>
    </div>
  </div>
</template>

<style scoped>
.developing-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 400px;
}
</style>