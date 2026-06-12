<script setup>
import { ref, watch } from 'vue'
import { message } from 'ant-design-vue'
import { dictApi } from '@/api/dict'

const props = defineProps({
  visible: Boolean,
  record: [Object, null],
})

const emit = defineEmits(['update:visible', 'success'])

const formRef = ref()
const loading = ref(false)
const isEdit = ref(false)
const form = ref({ id: undefined, dictName: '', dictType: '', status: 1, remark: '' })

watch(() => props.visible, (val) => {
  if (val && props.record) {
    isEdit.value = true
    form.value = { ...props.record }
  } else {
    isEdit.value = false
    form.value = { id: undefined, dictName: '', dictType: '', status: 1, remark: '' }
  }
}, { immediate: true })

async function handleSubmit() {
  await formRef.value.validate()
  loading.value = true
  try {
    if (isEdit.value) {
      await dictApi.updateType(form.value)
      message.success('修改成功')
    } else {
      await dictApi.createType(form.value)
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
  <a-modal :open="visible" :title="isEdit ? '修改字典类型' : '新增字典类型'" :confirm-loading="loading" width="480px" @ok="handleSubmit" @cancel="() => emit('update:visible', false)">
    <a-form ref="formRef" :model="form" layout="vertical" style="margin-top: 12px">
      <a-form-item label="字典名称" name="dictName" :rules="[{ required: true }]">
        <a-input v-model:value="form.dictName" placeholder="如：用户性别" />
      </a-form-item>
      <a-form-item label="字典编码" name="dictType" :rules="[{ required: true }]">
        <a-input v-model:value="form.dictType" :disabled="isEdit" placeholder="如：sys_user_gender" />
      </a-form-item>
      <a-form-item label="状态" name="status">
        <a-select v-model:value="form.status">
          <a-select-option :value="1">正常</a-select-option>
          <a-select-option :value="0">停用</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="备注" name="remark">
        <a-textarea v-model:value="form.remark" :rows="2" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>