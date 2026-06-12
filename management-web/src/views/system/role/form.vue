<script setup>
import { ref, watch } from 'vue'
import { message } from 'ant-design-vue'
import { useI18n } from 'vue-i18n'
import { roleApi } from '@/api/role'

const { t } = useI18n()

const props = defineProps({
  visible: Boolean,
  record: [Object, null],
})

const emit = defineEmits(['update:visible', 'success'])

const formRef = ref()
const loading = ref(false)
const isEdit = ref(false)
const form = ref({
  id: undefined,
  roleName: '',
  roleCode: '',
  roleDesc: '',
  dataScope: 1,
  sortOrder: 0,
  status: 1,
})

watch(
  () => props.visible,
  (val) => {
    if (val && props.record) {
      isEdit.value = true
      form.value = { ...props.record }
    } else {
      isEdit.value = false
      form.value = {
        id: undefined,
        roleName: '', roleCode: '', roleDesc: '',
        dataScope: 1, sortOrder: 0, status: 1,
      }
    }
  },
  { immediate: true },
)

async function handleSubmit() {
  await formRef.value.validate()
  loading.value = true
  try {
    if (isEdit.value) {
      await roleApi.update(form.value)
      message.success(t('common.updateSuccess'))
    } else {
      await roleApi.create(form.value)
      message.success(t('common.createSuccess'))
    }
    emit('success')
    emit('update:visible', false)
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <a-modal :open="visible" :title="isEdit ? t('common.edit') : t('common.add')" :confirm-loading="loading" width="520px" @ok="handleSubmit" @cancel="() => emit('update:visible', false)">
    <a-form ref="formRef" :model="form" layout="vertical" style="margin-top: 16px">
      <a-form-item :label="t('role.roleName')" name="roleName" :rules="[{ required: true }]">
        <a-input v-model:value="form.roleName" />
      </a-form-item>
      <a-form-item :label="t('role.roleCode')" name="roleCode" :rules="[{ required: true }]">
        <a-input v-model:value="form.roleCode" :disabled="isEdit" />
      </a-form-item>
      <a-form-item :label="t('role.roleDesc')" name="roleDesc">
        <a-textarea v-model:value="form.roleDesc" :rows="3" />
      </a-form-item>
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item :label="t('role.dataScope')" name="dataScope">
            <a-select v-model:value="form.dataScope">
              <a-select-option :value="1">全部数据权限</a-select-option>
              <a-select-option :value="2">本部门及子部门</a-select-option>
              <a-select-option :value="3">仅本部门</a-select-option>
              <a-select-option :value="4">仅本人</a-select-option>
            </a-select>
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
    </a-form>
  </a-modal>
</template>