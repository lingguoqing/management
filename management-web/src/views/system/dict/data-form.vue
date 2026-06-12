<script setup>
import { ref, watch } from 'vue'
import { message } from 'ant-design-vue'
import { dictApi } from '@/api/dict'

const props = defineProps({
  visible: Boolean,
  record: [Object, null],
  dictType: String,
})

const emit = defineEmits(['update:visible', 'success'])

const formRef = ref()
const loading = ref(false)
const isEdit = ref(false)
const form = ref({
  id: undefined,
  dictType: props.dictType,
  dictLabel: '', dictValue: '',
  sortOrder: 0, isDefault: 0,
  status: 1, remark: '',
})

watch(() => props.visible, (val) => {
  if (val) {
    form.value.dictType = props.dictType
    if (props.record) {
      isEdit.value = true
      form.value = { ...props.record }
    } else {
      isEdit.value = false
      form.value = { id: undefined, dictType: props.dictType, dictLabel: '', dictValue: '', sortOrder: 0, isDefault: 0, status: 1, remark: '' }
    }
  }
}, { immediate: true })

async function handleSubmit() {
  await formRef.value.validate()
  loading.value = true
  try {
    if (isEdit.value) {
      await dictApi.updateData(form.value)
      message.success('修改成功')
    } else {
      await dictApi.createData(form.value)
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
  <a-modal :open="visible" :title="isEdit ? '修改字典数据' : '新增字典数据'" :confirm-loading="loading" width="480px" @ok="handleSubmit" @cancel="() => emit('update:visible', false)">
    <a-form ref="formRef" :model="form" layout="vertical" style="margin-top: 12px">
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="字典标签" name="dictLabel" :rules="[{ required: true }]">
            <a-input v-model:value="form.dictLabel" placeholder="如：男" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="字典值" name="dictValue" :rules="[{ required: true }]">
            <a-input v-model:value="form.dictValue" placeholder="如：1" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="排序号" name="sortOrder">
            <a-input-number v-model:value="form.sortOrder" :min="0" style="width: 100%" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="状态" name="status">
            <a-select v-model:value="form.status">
              <a-select-option :value="1">正常</a-select-option>
              <a-select-option :value="0">停用</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>