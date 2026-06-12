<script setup>
import { ref, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import { deptApi } from '@/api/dept'
import DeptForm from './form.vue'

const { t } = useI18n()

const loading = ref(false)
const treeData = ref([])

const formVisible = ref(false)
const formData = ref(null)

onMounted(() => fetchTree())

async function fetchTree() {
  loading.value = true
  try {
    const res = await deptApi.tree()
    treeData.value = res.data
  } catch { /* Axios interceptor handles error message */ } finally {
    loading.value = false
  }
}

function handleAdd(parent) {
  formData.value = parent ? { parentId: parent.id } : null
  formVisible.value = true
}

function handleEdit(record) {
  formData.value = { ...record }
  formVisible.value = true
}

async function handleDelete(id) {
  await deptApi.delete(id)
  message.success(t('common.deleteSuccess'))
  fetchTree()
}

const columns = [
  { title: '部门名称', dataIndex: 'deptName', key: 'deptName' },
  { title: '部门编码', dataIndex: 'deptCode', key: 'deptCode' },
  { title: '负责人', dataIndex: 'leader', key: 'leader' },
  { title: '联系电话', dataIndex: 'phone', key: 'phone' },
  { title: '排序', dataIndex: 'sortOrder', key: 'sortOrder', width: 60 },
  { title: t('common.status'), dataIndex: 'status', key: 'status', width: 80 },
  { title: t('common.action'), key: 'action', width: 200 },
]
</script>

<template>
  <div class="content-page">
    <div class="page-header">
      <h2 class="page-title">{{ t('menu.dept') }}</h2>
    </div>

    <div class="table-container">
      <div style="padding: 12px 16px">
        <a-button type="primary" @click="handleAdd()">{{ t('common.add') }}根部门</a-button>
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
          <template v-if="column.key === 'status'">
            <a-tag :color="record.status === 1 ? 'green' : 'red'">
              {{ record.status === 1 ? t('common.enabled') : t('common.disabled') }}
            </a-tag>
          </template>
          <template v-if="column.key === 'action'">
            <div class="action-buttons">
              <a-button type="link" size="small" @click="handleAdd(record)">添加子级</a-button>
              <a-button type="link" size="small" @click="handleEdit(record)">{{ t('common.edit') }}</a-button>
              <a-popconfirm :title="t('common.deleteConfirm')" @confirm="handleDelete(record.id)">
                <a-button type="link" size="small" danger>{{ t('common.delete') }}</a-button>
              </a-popconfirm>
            </div>
          </template>
        </template>
      </a-table>
    </div>

    <DeptForm v-if="formVisible" v-model:visible="formVisible" :record="formData" @success="fetchTree" />
  </div>
</template>