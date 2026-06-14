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
    name: '',
    title: '',
    enName: '',
    path: '',
    component: '',
    redirect: '',
    url: '',
    type: 1,
    permission: '',
    permsType: '1',
    icon: '',
    sort: 1,
    visible: 1,
    isRoute: 1,
    isLeaf: 1,
    alwaysShow: 0,
    keepAlive: 0,
    iFrame: 0,
    hideTab: 0,
    status: 1,
    description: '',
    ruleFlag: 0,
  }
}

const form = ref(getInitialForm())

// 监听 visible + record 组合变化
watch(
  () => [props.visible, props.record],
  ([val, record]) => {
    if (val && record) {
      initForm()
    }
  },
  { immediate: true, deep: true },
)

function initForm() {
  if (!props.record) {
    // 新增根节点
    isEdit.value = false
    form.value = getInitialForm()
    return
  }

  const record = props.record
  if (record.id !== undefined) {
    // 编辑已有权限 - 深拷贝避免引用问题
    isEdit.value = true
    form.value = JSON.parse(JSON.stringify(record))
    // 确保数字字段有默认值
    form.value.sort = form.value.sort ?? 1
    form.value.type = form.value.type ?? 1
    form.value.status = form.value.status ?? 1
    form.value.visible = form.value.visible ?? 1
  } else {
    // 添加子级
    isEdit.value = false
    form.value = getInitialForm()
    form.value.parentId = record.parentId ?? 0
    form.value.type = record.type === 0 ? 1 : record.type
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
  <a-modal :open="visible" :title="isEdit ? '修改权限' : '新增权限'" :confirm-loading="loading" width="640px" @ok="handleSubmit" @cancel="() => emit('update:visible', false)">
    <a-form ref="formRef" :model="form" layout="vertical" style="margin-top: 16px">
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="路由名称" name="name">
            <a-input v-model:value="form.name" placeholder="用于keep-alive缓存" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="菜单标题" name="title" :rules="[{ required: true, message: '菜单标题不能为空' }]">
            <a-input v-model:value="form.title" placeholder="侧边栏显示的名称" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="英文标题" name="enName">
            <a-input v-model:value="form.enName" placeholder="英文标题/i18n键" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="菜单类型" name="type" :rules="[{ required: true }]">
            <a-select v-model:value="form.type" :disabled="isEdit">
              <a-select-option :value="0">目录</a-select-option>
              <a-select-option :value="1">菜单</a-select-option>
              <a-select-option :value="2">按钮</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="路由地址" name="path" :rules="[{ required: form.type === 1, message: '菜单类型必须填写路由地址' }]">
            <a-input v-model:value="form.path" placeholder="如: /system/user" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="组件路径" name="component" :rules="[{ required: form.type === 1, message: '菜单类型必须填写组件路径' }]">
            <a-input v-model:value="form.component" placeholder="如: system/user/index" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="重定向地址" name="redirect">
            <a-input v-model:value="form.redirect" placeholder="一级菜单重定向地址" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="图标" name="icon">
            <IconPicker v-model="form.icon" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="16" v-if="form.type === 2">
        <a-col :span="24">
          <a-form-item label="权限标识" name="permission" :rules="[{ required: form.type === 2, message: '按钮权限必须填写权限标识' }]">
            <a-input v-model:value="form.permission" placeholder="如: system:user:add" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="16">
        <a-col :span="8">
          <a-form-item label="排序" name="sort">
            <a-input-number v-model:value="form.sort" :min="1" style="width: 100%" />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="是否显示" name="visible">
            <a-select v-model:value="form.visible">
              <a-select-option :value="1">显示</a-select-option>
              <a-select-option :value="0">隐藏</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="状态" name="status">
            <a-select v-model:value="form.status">
              <a-select-option :value="1">启用</a-select-option>
              <a-select-option :value="0">停用</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="16">
        <a-col :span="8">
          <a-form-item label="是否缓存" name="keepAlive">
            <a-select v-model:value="form.keepAlive">
              <a-select-option :value="1">是</a-select-option>
              <a-select-option :value="0">否</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="是否外链" name="iFrame">
            <a-select v-model:value="form.iFrame">
              <a-select-option :value="0">否</a-select-option>
              <a-select-option :value="1">是</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="聚合子路由" name="alwaysShow">
            <a-select v-model:value="form.alwaysShow">
              <a-select-option :value="0">否</a-select-option>
              <a-select-option :value="1">是</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>

      <a-form-item label="备注" name="description">
        <a-input v-model:value="form.description" placeholder="备注描述" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>