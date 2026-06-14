<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { addDynamicRoutes } from '@/router/dynamic-routes'
import { message } from 'ant-design-vue'
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue'
import { useI18n } from 'vue-i18n'

const router = useRouter()
const userStore = useUserStore()
const { t } = useI18n()

const form = ref({
  username: 'test',
  password: '123456',
})

const loading = ref(false)

async function handleLogin() {
  if (!form.value.username) {
    message.warning(t('login.usernameRequired'))
    return
  }
  if (!form.value.password) {
    message.warning(t('login.passwordRequired'))
    return
  }

  loading.value = true
  try {
    await userStore.login(form.value.username, form.value.password)
    addDynamicRoutes(router, userStore.menus)
    message.success(t('login.loginSuccess'))
    router.push('/')
  } catch {
    message.error(t('login.loginFailed'))
  } finally {
    loading.value = false
  }
}

/** 回车登录 */
function handleKeyup(e) {
  if (e.key === 'Enter') {
    handleLogin()
  }
}
</script>

<template>
  <div class="login-page" @keyup="handleKeyup">
    <!-- 背景装饰层 -->
    <div class="bg-layer">
      <div class="orb orb-1"></div>
      <div class="orb orb-2"></div>
    </div>

    <!-- 登录卡片 -->
    <div class="login-card">
      <!-- 品牌区 -->
      <div class="brand">
        <div class="brand-icon">
          <svg width="28" height="28" viewBox="0 0 24 24" fill="none">
            <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </div>
        <h1 class="brand-title">{{ t('login.title') }}</h1>
        <p class="brand-subtitle">{{ t('login.subtitle') }}</p>
      </div>

      <!-- 表单区 -->
      <a-form :model="form" layout="vertical" size="large" autocomplete="off" class="login-form">
        <a-form-item>
          <a-input
            v-model:value="form.username"
            :placeholder="t('login.username')"
            class="input-field"
          >
            <template #prefix>
              <UserOutlined class="input-icon" />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item>
          <a-input-password
            v-model:value="form.password"
            :placeholder="t('login.password')"
            class="input-field"
          >
            <template #prefix>
              <LockOutlined class="input-icon" />
            </template>
          </a-input-password>
        </a-form-item>

        <a-form-item class="submit-item">
          <a-button
            type="primary"
            :loading="loading"
            block
            class="submit-btn"
            @click="handleLogin"
          >
            {{ t('login.submit') }}
          </a-button>
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>

<style scoped>
/* ============================================
   登录页 — 简约纯净白风格
   ============================================ */

.login-page {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  overflow: hidden;
  background: #f8fafc;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

/* ---- 背景装饰层 ---- */
.bg-layer {
  position: absolute;
  inset: 0;
  pointer-events: none;
  z-index: 0;
  overflow: hidden;
}

.orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(100px);
}

.orb-1 {
  width: 500px;
  height: 500px;
  background: linear-gradient(135deg, #e0e7ff, #c7d2fe);
  top: -150px;
  right: -100px;
  opacity: 0.6;
  animation: float1 25s ease-in-out infinite;
}

.orb-2 {
  width: 400px;
  height: 400px;
  background: linear-gradient(135deg, #ddd6fe, #e0e7ff);
  bottom: -100px;
  left: -80px;
  opacity: 0.5;
  animation: float2 30s ease-in-out infinite;
}

@keyframes float1 {
  0%, 100% { transform: translate(0, 0); }
  50% { transform: translate(-30px, 40px); }
}

@keyframes float2 {
  0%, 100% { transform: translate(0, 0); }
  50% { transform: translate(40px, -30px); }
}

/* ---- 登录卡片 ---- */
.login-card {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 400px;
  padding: 48px 40px;
  background: #ffffff;
  border-radius: 24px;
  border: 1px solid #e2e8f0;
  box-shadow:
    0 4px 6px -1px rgba(0, 0, 0, 0.05),
    0 10px 30px -5px rgba(79, 70, 229, 0.08),
    0 0 0 1px rgba(79, 70, 229, 0.03);
}

/* ---- 品牌区 ---- */
.brand {
  text-align: center;
  margin-bottom: 40px;
}

.brand-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 56px;
  height: 56px;
  border-radius: 16px;
  background: linear-gradient(135deg, #4f46e5, #6366f1);
  color: #fff;
  margin-bottom: 20px;
  box-shadow: 0 8px 24px rgba(79, 70, 229, 0.25);
}

.brand-title {
  font-size: 1.75rem;
  font-weight: 700;
  color: #1e293b;
  letter-spacing: -0.5px;
  margin: 0 0 8px;
}

.brand-subtitle {
  font-size: 0.9rem;
  color: #64748b;
  margin: 0;
}

/* ---- 表单区 ---- */
.login-form :deep(.ant-form-item) {
  margin-bottom: 20px;
}

.login-form :deep(.ant-form-item-label > label) {
  color: #475569;
  font-size: 0.8rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.input-field :deep(.ant-input) {
  height: 48px;
  background: #f8fafc !important;
  border: 1.5px solid #e2e8f0 !important;
  border-radius: 12px !important;
  color: #1e293b !important;
  font-size: 0.95rem;
  padding-left: 16px;
  transition: all 0.2s ease;
}

.input-field :deep(.ant-input::placeholder) {
  color: #94a3b8;
}

.input-field :deep(.ant-input:hover) {
  border-color: #c7d2fe !important;
  background: #ffffff !important;
}

.input-field :deep(.ant-input:focus) {
  border-color: #4f46e5 !important;
  background: #ffffff !important;
  box-shadow: 0 0 0 3px rgba(79, 70, 229, 0.1) !important;
}

.input-field :deep(.ant-input-affix-wrapper) {
  height: 48px;
  background: #f8fafc !important;
  border: 1.5px solid #e2e8f0 !important;
  border-radius: 12px !important;
  padding: 0 16px;
  transition: all 0.2s ease;
}

.input-field :deep(.ant-input-affix-wrapper:hover) {
  border-color: #c7d2fe !important;
  background: #ffffff !important;
}

.input-field :deep(.ant-input-affix-wrapper:focus-within) {
  border-color: #4f46e5 !important;
  background: #ffffff !important;
  box-shadow: 0 0 0 3px rgba(79, 70, 229, 0.1) !important;
}

.input-field :deep(.ant-input-prefix) {
  margin-right: 12px;
}

.input-icon {
  color: #94a3b8;
  font-size: 16px;
}

.input-field :deep(.ant-input-password-icon) {
  color: #94a3b8 !important;
}

.input-field :deep(.ant-input-password-icon:hover) {
  color: #4f46e5 !important;
}

/* ---- 提交按钮 ---- */
.submit-item {
  margin-top: 28px;
  margin-bottom: 0 !important;
}

.submit-btn {
  height: 48px;
  border-radius: 12px;
  font-size: 0.95rem;
  font-weight: 600;
  letter-spacing: 0.3px;
  background: linear-gradient(135deg, #4f46e5, #6366f1) !important;
  border: none !important;
  box-shadow: 0 4px 14px rgba(79, 70, 229, 0.3);
  transition: all 0.2s ease;
}

.submit-btn:hover:not(:disabled) {
  background: linear-gradient(135deg, #5b52e8, #7575f5) !important;
  box-shadow: 0 6px 20px rgba(79, 70, 229, 0.4);
  transform: translateY(-1px);
}

.submit-btn:active:not(:disabled) {
  transform: translateY(0);
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* ---- 响应式 ---- */
@media (max-width: 480px) {
  .login-card {
    padding: 36px 24px;
    margin: 16px;
    border-radius: 20px;
  }

  .brand-title {
    font-size: 1.5rem;
  }

  .orb {
    filter: blur(60px);
  }
}
</style>