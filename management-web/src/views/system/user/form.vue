<script setup>
import { ref, watch } from 'vue'
import { message } from 'ant-design-vue'
import { useI18n } from 'vue-i18n'
import { userApi } from '@/api/user'

const { t } = useI18n()

const props = defineProps({
  visible: Boolean,
  record: [Object, null],
  roleList: Array,
  deptTree: Array,
})

const emit = defineEmits(['update:visible', 'success'])

const formRef = ref()
const loading = ref(false)
const form = ref({
  id: undefined,
  username: '',
  password: '',
  nickname: '',
  email: '',
  phone: '',
  gender: 0,
  deptId: undefined,
  status: 1,
  roleIds: [],
})

const isEdit = ref(false)

watch(
  () => props.visible,
  (val) => {
    if (val) {
      if (props.record) {
        isEdit.value = true
        form.value = {
          id: props.record.id,
          username: props.record.username,
          password: '',
          nickname: props.record.nickname || '',
          email: props.record.email || '',
          phone: props.record.phone || '',
          gender: props.record.gender || 0,
          deptId: props.record.deptId,
          status: props.record.status ?? 1,
          roleIds: props.record.roleIds || [],
        }
      } else {
        isEdit.value = false
        form.value = {
          id: undefined,
          username: '',
          password: '',
          nickname: '',
          email: '',
          phone: '',
          gender: 0,
          deptId: undefined,
          status: 1,
          roleIds: [],
        }
      }
    }
  },
  { immediate: true },
)

async function handleSubmit() {
  try {
    await formRef.value.validate()
    loading.value = true
    if (isEdit.value) {
      await userApi.update(form.value)
      message.success(t('common.updateSuccess'))
    } else {
      await userApi.create(form.value)
      message.success(t('common.createSuccess'))
    }
    emit('success')
    handleClose()
  } catch {
    // validation failed
  } finally {
    loading.value = false
  }
}

function handleClose() {
  emit('update:visible', false)
}
</script>

<template>
  <a-modal
    :open="visible"
    :title="isEdit ? t('common.edit') : t('common.add')"
    :confirm-loading="loading"
    width="620px"
    @ok="handleSubmit"
    @cancel="handleClose"
  >
    <a-form ref="formRef" :model="form" layout="vertical" style="margin-top: 16px">
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item
            :label="t('user.username')"
            name="username"
            :rules="[{ required: true, message: '请输入用户名' }]"
          >
            <a-input v-model:value="form.username" :disabled="isEdit" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            :label="t('user.password')"
            name="password"
            :rules="isEdit ? [] : [{ required: true, message: '请输入密码' }]"
          >
            <a-input-password
              v-model:value="form.password"
              :placeholder="isEdit ? '留空则不修改密码' : '请输入密码'"
            />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item :label="t('user.nickname')" name="nickname">
            <a-input v-model:value="form.nickname" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item :label="t('user.phone')" name="phone">
            <a-input v-model:value="form.phone" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item :label="t('user.email')" name="email">
            <a-input v-model:value="form.email" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item :label="t('user.gender')" name="gender">
            <a-select v-model:value="form.gender">
              <a-select-option :value="0">{{ t('user.unknown') }}</a-select-option>
              <a-select-option :value="1">{{ t('user.male') }}</a-select-option>
              <a-select-option :value="2">{{ t('user.female') }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item :label="t('user.dept')" name="deptId">
            <a-tree-select
              v-model:value="form.deptId"
              :placeholder="t('user.dept')"
              :tree-data="deptTree"
              :field-names="{ children: 'children', label: 'deptName', value: 'id' }"
              style="width: 100%"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item :label="t('common.status')" name="status">
            <a-select v-model:value="form.status">
              <a-select-option :value="1">{{ t('common.enabled') }}</a-select-option>
              <a-select-option :value="0">{{ t('common.disabled') }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>

      <a-form-item :label="t('user.role')" name="roleIds">
        <a-select
          v-model:value="form.roleIds"
          mode="multiple"
          :placeholder="t('user.role')"
          style="width: 100%"
        >
          <a-select-option
            v-for="r in roleList"
            :key="r.id"
            :value="r.id"
          >
            {{ r.roleName }}
          </a-select-option>
        </a-select>
      </a-form-item>
    </a-form>
  </a-modal>
</template>