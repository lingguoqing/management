<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import { roleApi } from '@/api/role'
import { formatDateTime } from '@/utils'
import RoleForm from './form.vue'
import PermModal from './perm-modal.vue'

const { t } = useI18n()

const loading = ref(false)
const list = ref([])
const total = ref(0)

const query = reactive({
  page: 1,
  pageSize: 12,
  keyword: '',
  status: undefined,
})

const formVisible = ref(false)
const formData = ref(null)

const permVisible = ref(false)
const permRoleId = ref(0)

onMounted(() => fetchList())

async function fetchList() {
  loading.value = true
  try {
    const res = await roleApi.page(query)
    list.value = res.data.records
    total.value = res.data.total
  } catch { /* Axios interceptor handles error message */ } finally {
    loading.value = false
  }
}

function handleSearch() {
  query.page = 1
  fetchList()
}

function handleAdd() {
  formData.value = null
  formVisible.value = true
}

function handleEdit(record) {
  formData.value = { ...record }
  formVisible.value = true
}

async function handleDelete(id) {
  await roleApi.delete(id)
  message.success(t('common.deleteSuccess'))
  fetchList()
}

function handleAssignPerm(record) {
  permRoleId.value = record.id
  permVisible.value = true
}

const columns = [
  { title: t('role.roleName'), dataIndex: 'roleName', key: 'roleName' },
  { title: t('role.roleCode'), dataIndex: 'roleCode', key: 'roleCode' },
  { title: t('role.roleDesc'), dataIndex: 'roleDesc', key: 'roleDesc' },
  { title: t('common.status'), dataIndex: 'status', key: 'status' },
  { title: t('common.createTime'), dataIndex: 'createTime', key: 'createTime' },
  { title: t('common.action'), key: 'action', width: 240 },
]
</script>

<template>
  <div class="content-page">
    <div class="page-header">
      <h2 class="page-title">{{ t('menu.role') }}</h2>
    </div>

    <div class="search-bar">
      <a-input v-model:value="query.keyword" :placeholder="t('common.search') + '...'" style="width: 240px" @pressEnter="handleSearch" />
      <a-select v-model:value="query.status" :placeholder="t('common.status')" style="width: 120px" allow-clear>
        <a-select-option :value="1">{{ t('common.enabled') }}</a-select-option>
        <a-select-option :value="0">{{ t('common.disabled') }}</a-select-option>
      </a-select>
      <a-button type="primary" @click="handleSearch">{{ t('common.search') }}</a-button>
    </div>

    <div class="table-container">
      <div style="padding: 12px 16px">
        <a-button type="primary" @click="handleAdd">{{ t('common.add') }}</a-button>
      </div>
      <a-table :columns="columns" :data-source="list" :loading="loading" row-key="id"
        :pagination="{ current: query.page, pageSize: query.pageSize, total, showSizeChanger: true, showTotal: (t) => `共 ${t} 条` }"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-tag :color="record.status === 1 ? 'green' : 'red'">
              {{ record.status === 1 ? t('common.enabled') : t('common.disabled') }}
            </a-tag>
          </template>
          <template v-if="column.key === 'createTime'">
            {{ formatDateTime(record.createTime) }}
          </template>
          <template v-if="column.key === 'action'">
            <div class="action-buttons">
              <a-button type="link" size="small" @click="handleAssignPerm(record)">{{ t('role.assignPerm') }}</a-button>
              <a-button type="link" size="small" @click="handleEdit(record)">{{ t('common.edit') }}</a-button>
              <a-popconfirm :title="t('common.deleteConfirm')" @confirm="handleDelete(record.id)">
                <a-button type="link" size="small" danger>{{ t('common.delete') }}</a-button>
              </a-popconfirm>
            </div>
          </template>
        </template>
      </a-table>
    </div>

    <RoleForm v-if="formVisible" v-model:visible="formVisible" :record="formData" @success="fetchList" />
    <PermModal v-if="permVisible" v-model:visible="permVisible" :role-id="permRoleId" @success="fetchList" />
  </div>
</template>