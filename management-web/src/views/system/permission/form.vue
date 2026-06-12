<script setup>
import { ref, watch, computed } from 'vue'
import { message } from 'ant-design-vue'
import { permissionApi } from '@/api/permission'
import IconPicker from '@/components/IconPicker.vue'

const props = defineProps({
  visible: Boolean,
  record: Object,
})

const emit = defineEmits(['update:visible', 'success'])

const formRef = ref()
const loading = ref(false)
const isEdit = ref(false)

// 表单数据初始化
function getInitialForm() {
  return {
    id: undefined,
    parentId: 0,
    permName: '',
    permCode: '',
    permType: 2,
    path: '',
    component: '',
    icon: '',
    sortOrder: 0,
    visible: 1,
    keepAlive: 0,
    status: 1,
  }
}

const form = ref(getInitialForm())

// 判断是否为编辑模式
const isEditMode = computed(() => props.record && props.record.id !== undefined)

// 监听 visible 变化，初始化或重置表单
watch(
  () => props.visible,
  (val) => {
    if (val) {
      initForm()
    }
  },
  { immediate: true },
)

// 监听 record 深度变化
watch(
  () => props.record,
  () => {
    if (props.visible) {
      initForm()
    }
  },
  { deep: true },
)

function initForm() {
  console.log('[PermForm] initForm, record:', props.record)
  if (!props.record) {
    // 新增根节点
    isEdit.value = false
    form.value = getInitialForm()
    return
  }

  const record = props.record
  if (record.id !== undefined) {
    // 编辑已有权限
    isEdit.value = true
    form.value = {
      id: record.id,
      parentId: record.parentId ?? 0,
      permName: record.permName ?? '',
      permCode: record.permCode ?? '',
      permType: record.permType ?? 2,
      path: record.path ?? '',
      component: record.component ?? '',
      icon: record.icon ?? '',
      sortOrder: record.sortOrder ?? 0,
      visible: record.visible ?? 1,
      keepAlive: record.keepAlive ?? 0,
      status: record.status ?? 1,
    }
  } else {
    // 添加子级
    isEdit.value = false
    form.value = getInitialForm()
    form.value.parentId = record.parentId ?? 0
    form.value.permType = record.permType === 1 ? 2 : 2
  }
}

async function handleSubmit() {
  await formRef.value.validate()
  loading.value = true
  try {
    if (isEdit.value) {
      await permissionApi.update(form.value)
      message.success('修改成功')
    } else {
      await permissionApi.create(form.value)
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
  <a-modal :open="visible" :title="isEdit ? '修改权限' : '新增权限'" :confirm-loading="loading" width="560px" @ok="handleSubmit" @cancel="() => emit('update:visible', false)">
    <a-form ref="formRef" :model="form" layout="vertical" style="margin-top: 16px">
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="权限名称" name="permName" :rules="[{ required: true }]">
            <a-input v-model:value="form.permName" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="类型" name="permType" :rules="[{ required: true }]">
            <a-select v-model:value="form.permType" :disabled="isEdit">
              <a-select-option :value="1">目录</a-select-option>
              <a-select-option :value="2">菜单</a-select-option>
              <a-select-option :value="3">按钮</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>

      <a-form-item v-if="form.permType === 3" label="权限标识" name="permCode" :rules="[{ required: form.permType === 3, message: '按钮权限必须填写权限标识' }]">
        <a-input v-model:value="form.permCode" placeholder="如: sys:user:add" />
      </a-form-item>

      <template v-if="form.permType <= 2">
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="路由路径" name="path" :rules="[{ required: form.permType === 2, message: '菜单类型必须填写路由路径' }]">
              <a-input v-model:value="form.path" placeholder="/system/user" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="图标" name="icon">
              <IconPicker v-model="form.icon" />
            </a-form-item>
          </a-col>
        </a-row>

        <a-form-item v-if="form.permType === 2" label="组件路径" name="component" :rules="[{ required: form.permType === 2, message: '菜单类型必须填写组件路径' }]">
          <a-input v-model:value="form.component" placeholder="system/user/index" />
        </a-form-item>
      </template>

      <a-row :gutter="16">
        <a-col :span="8">
          <a-form-item label="排序号" name="sortOrder">
            <a-input-number v-model:value="form.sortOrder" :min="0" style="width: 100%" />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="可见" name="visible">
            <a-select v-model:value="form.visible">
              <a-select-option :value="1">是</a-select-option>
              <a-select-option :value="0">否</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="8">
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