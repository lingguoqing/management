<script setup>
import { ref, watch } from 'vue'
import { message } from 'ant-design-vue'
import { deptApi } from '@/api/dept'

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
  parentId: 0,
  deptName: '',
  deptCode: '',
  sortOrder: 0,
  leader: '',
  phone: '',
  email: '',
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
        parentId: props.record?.parentId || 0,
        deptName: '', deptCode: '',
        sortOrder: 0, leader: '', phone: '', email: '',
        status: 1,
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
      await deptApi.update(form.value)
      message.success('修改成功')
    } else {
      await deptApi.create(form.value)
      message.success('新增成功')
    }
    emit('success')
    emit('update:visible', false)
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <a-modal :open="visible" :title="isEdit ? '修改部门' : '新增部门'" :confirm-loading="loading" width="520px" @ok="handleSubmit" @cancel="() => emit('update:visible', false)">
    <a-form ref="formRef" :model="form" layout="vertical" style="margin-top: 16px">
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="部门名称" name="deptName" :rules="[{ required: true }]">
            <a-input v-model:value="form.deptName" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="部门编码" name="deptCode" :rules="[{ required: true }]">
            <a-input v-model:value="form.deptCode" :disabled="isEdit" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="负责人" name="leader">
            <a-input v-model:value="form.leader" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="联系电话" name="phone">
            <a-input v-model:value="form.phone" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="邮箱" name="email">
            <a-input v-model:value="form.email" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="排序号" name="sortOrder">
            <a-input-number v-model:value="form.sortOrder" :min="0" style="width: 100%" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-form-item label="状态" name="status">
        <a-select v-model:value="form.status">
          <a-select-option :value="1">正常</a-select-option>
          <a-select-option :value="0">停用</a-select-option>
        </a-select>
      </a-form-item>
    </a-form>
  </a-modal>
</template>