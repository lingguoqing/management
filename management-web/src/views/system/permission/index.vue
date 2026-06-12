<script setup>
import { ref, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import { permissionApi } from '@/api/permission'
import { formatDateTime } from '@/utils'
import PermForm from './form.vue'

const { t } = useI18n()

const loading = ref(false)
const treeData = ref([])
const expandedKeys = ref([])
const selectedRowKeys = ref([])

const formVisible = ref(false)
const formData = ref(null)
const selectedPerm = ref(null)

onMounted(() => fetchTree())

async function fetchTree() {
  loading.value = true
  try {
    const res = await permissionApi.tree()
    treeData.value = res.data
    expandedKeys.value = res.data.map(i => i.id)
  } catch { /* Axios interceptor handles error message */ } finally {
    loading.value = false
  }
}

function handleAdd(parent) {
  formData.value = parent ? { parentId: parent.id, permType: parent.permType === 1 ? 2 : 2 } : null
  formVisible.value = true
}

async function handleEdit(record) {
  // 先从数据库获取最新详情，确保数据完整
  try {
    const res = await permissionApi.get(record.id)
    formData.value = res.data
  } catch {
    // 获取失败时使用表格传入的 record
    formData.value = { ...record }
  }
  formVisible.value = true
}

async function handleDelete(id) {
  await permissionApi.delete(id)
  message.success(t('common.deleteSuccess'))
  fetchTree()
}

function getTypeTag(type) {
  const map = {
    1: { color: 'blue', text: '目录' },
    2: { color: 'green', text: '菜单' },
    3: { color: 'orange', text: '按钮' },
  }
  return map[type] || { color: 'default', text: '未知' }
}

const columns = [
  { title: '权限名称', dataIndex: 'permName', key: 'permName' },
  { title: '权限标识', dataIndex: 'permCode', key: 'permCode' },
  { title: '类型', dataIndex: 'permType', key: 'permType', width: 80 },
  { title: '路由路径', dataIndex: 'path', key: 'path' },
  { title: '组件路径', dataIndex: 'component', key: 'component' },
  { title: '排序', dataIndex: 'sortOrder', key: 'sortOrder', width: 60 },
  { title: '状态', dataIndex: 'status', key: 'status', width: 80 },
  { title: t('common.action'), key: 'action', width: 200 },
]
</script>

<template>
  <div class="content-page">
    <div class="page-header">
      <h2 class="page-title">{{ t('menu.permission') }}</h2>
    </div>

    <div class="table-container">
      <div style="padding: 12px 16px">
        <a-button type="primary" @click="handleAdd()">{{ t('common.add') }}根节点</a-button>
      </div>
      <a-table
        :columns="columns"
        :data-source="treeData"
        :loading="loading"
        :default-expand-all-rows="true"
        row-key="id"
        :pagination="false"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'permType'">
            <a-tag :color="getTypeTag(record.permType).color">
              {{ getTypeTag(record.permType).text }}
            </a-tag>
          </template>
          <template v-if="column.key === 'status'">
            <a-tag :color="record.status === 1 ? 'green' : 'red'">
              {{ record.status === 1 ? t('common.enabled') : t('common.disabled') }}
            </a-tag>
          </template>
          <template v-if="column.key === 'action'">
            <div class="action-buttons">
              <a-button v-if="record.permType !== 3" type="link" size="small" @click="handleAdd(record)">
                添加子级
              </a-button>
              <a-button type="link" size="small" @click="handleEdit(record)">{{ t('common.edit') }}</a-button>
              <a-popconfirm :title="t('common.deleteConfirm')" @confirm="handleDelete(record.id)">
                <a-button type="link" size="small" danger>{{ t('common.delete') }}</a-button>
              </a-popconfirm>
            </div>
          </template>
        </template>
      </a-table>
    </div>

    <PermForm v-if="formVisible" v-model:visible="formVisible" :record="formData" @success="fetchTree" />
  </div>
</template>