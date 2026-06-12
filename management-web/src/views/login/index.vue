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
  username: 'admin',
  password: 'admin123',
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
  <div class="search-center-page" @keyup="handleKeyup">
    <!-- Logo -->
    <div class="search-logo">{{ t('login.title') }}</div>
    <p class="search-subtitle">{{ t('login.subtitle') }}</p>

    <!-- 登录表单卡片 -->
    <div class="search-box-wrapper login-card">
      <a-form
        :model="form"
        layout="vertical"
        size="large"
        autocomplete="off"
      >
        <a-form-item>
          <a-input
            v-model:value="form.username"
            :placeholder="t('login.username')"
            size="large"
          >
            <template #prefix>
              <UserOutlined style="color: #9ca3af" />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item>
          <a-input-password
            v-model:value="form.password"
            :placeholder="t('login.password')"
            size="large"
          >
            <template #prefix>
              <LockOutlined style="color: #9ca3af" />
            </template>
          </a-input-password>
        </a-form-item>

        <a-form-item>
          <a-button
            type="primary"
            size="large"
            :loading="loading"
            block
            style="height: 48px; border-radius: 12px; font-size: 15px; font-weight: 600"
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
.login-card {
  background: #fff;
  border-radius: 16px;
  padding: 40px 36px;
  box-shadow: 0 2px 16px rgba(0, 0, 0, .06);
  border: 1px solid #e5e7eb;
}

.login-card :deep(.ant-form-item) {
  margin-bottom: 20px;
}

.login-card :deep(.ant-input-affix-wrapper) {
  height: 48px;
  border-radius: 10px;
  border: 1px solid #e5e7eb;
  font-size: 15px;
}

.login-card :deep(.ant-input-affix-wrapper:focus-within) {
  border-color: #1a1a2e;
  box-shadow: 0 0 0 2px rgba(26, 26, 46, .1);
}
</style>