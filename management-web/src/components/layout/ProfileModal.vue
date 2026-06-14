<script setup>
import { ref, watch } from 'vue'
import { message } from 'ant-design-vue'
import { authApi } from '@/api/auth'
import { useUserStore } from '@/stores/user'

const props = defineProps({
  visible: Boolean,
})

const emit = defineEmits(['update:visible', 'success'])

const userStore = useUserStore()
const formRef = ref()
const pwdRef = ref()
const loading = ref(false)
const showPassword = ref(false)

function getInitialForm() {
  return {
    nickname: '',
    email: '',
    phone: '',
    gender: 0,
    remark: '',
  }
}

const form = ref(getInitialForm())
const newPassword = ref('')

watch(
  () => props.visible,
  (val) => {
    if (val) {
      form.value = {
        nickname: userStore.userInfo?.nickname || '',
        email: userStore.userInfo?.email || '',
        phone: userStore.userInfo?.phone || '',
        gender: userStore.userInfo?.gender || 0,
        remark: userStore.userInfo?.remark || '',
      }
      newPassword.value = ''
      showPassword.value = false
    }
  },
)

async function handleSubmit() {
  await formRef.value.validate()
  loading.value = true
  try {
    await authApi.updateProfile(form.value)
    message.success('修改成功')
    emit('success')
    emit('update:visible', false)
  } finally {
    loading.value = false
  }
}

async function handlePasswordSubmit() {
  if (!newPassword.value || newPassword.value.length < 6) {
    message.error('密码长度不能少于6位')
    return
  }
  loading.value = true
  try {
    await authApi.updatePassword({ newPassword: newPassword.value })
    message.success('密码修改成功')
    showPassword.value = false
    newPassword.value = ''
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <a-modal
    :open="visible"
    title="个人信息"
    :confirm-loading="loading"
    width="520px"
    @ok="handleSubmit"
    @cancel="() => emit('update:visible', false)"
  >
    <a-form ref="formRef" :model="form" layout="vertical" style="margin-top: 16px">
      <a-form-item label="用户名">
        <a-input :value="userStore.userInfo?.username" disabled />
      </a-form-item>

      <a-form-item label="昵称" name="nickname">
        <a-input v-model:value="form.nickname" placeholder="请输入昵称" />
      </a-form-item>

      <a-form-item label="邮箱" name="email">
        <a-input v-model:value="form.email" placeholder="请输入邮箱" />
      </a-form-item>

      <a-form-item label="手机号" name="phone">
        <a-input v-model:value="form.phone" placeholder="请输入手机号" />
      </a-form-item>

      <a-form-item label="性别" name="gender">
        <a-radio-group v-model:value="form.gender">
          <a-radio :value="0">未知</a-radio>
          <a-radio :value="1">男</a-radio>
          <a-radio :value="2">女</a-radio>
        </a-radio-group>
      </a-form-item>

      <a-form-item label="备注" name="remark">
        <a-textarea v-model:value="form.remark" placeholder="备注" :rows="2" />
      </a-form-item>
    </a-form>

    <!-- 修改密码区域 -->
    <div class="password-section">
      <div class="password-toggle" @click="showPassword = !showPassword">
        <span>{{ showPassword ? '收起' : '修改密码' }}</span>
        <span :class="['arrow', { up: showPassword }]">▼</span>
      </div>

      <a-form
        v-if="showPassword"
        layout="vertical"
        class="password-form"
      >
        <a-form-item label="新密码">
          <a-input-password
            v-model:value="newPassword"
            placeholder="请输入新密码（至少6位）"
          />
        </a-form-item>

        <a-button type="primary" block @click="handlePasswordSubmit">
          确认修改密码
        </a-button>
      </a-form>
    </div>
  </a-modal>
</template>

<style scoped>
.password-section {
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px dashed #e5e7eb;
}

.password-toggle {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  color: #4f46e5;
  font-size: 13px;
  font-weight: 500;
  user-select: none;
}

.password-toggle:hover {
  color: #6366f1;
}

.arrow {
  font-size: 10px;
  transition: transform 0.2s;
}

.arrow.up {
  transform: rotate(180deg);
}

.password-form {
  margin-top: 16px;
}

.password-form :deep(.ant-input-password) {
  height: 36px;
}
</style>