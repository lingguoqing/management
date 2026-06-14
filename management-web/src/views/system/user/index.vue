<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import { userApi } from '@/api/user'
import { roleApi } from '@/api/role'
import { deptApi } from '@/api/dept'
import { formatDateTime } from '@/utils'
import { useUserStore } from '@/stores/user'
import UserForm from './form.vue'

const { t } = useI18n()
const userStore = useUserStore()
const hasPermission = userStore.hasPermission

const loading = ref(false)
const list = ref([])
const total = ref(0)
const roleList = ref([])
const deptTree = ref([])

const query = reactive({
  page: 1,
  pageSize: 12,
  keyword: '',
  status: undefined,
  deptId: undefined,
})

const formVisible = ref(false)
const formData = ref(null)

onMounted(() => {
  fetchList()
  fetchDeps()
})

async function fetchList() {
  loading.value = true
  try {
    const res = await userApi.page(query)
    list.value = res.data.records
    total.value = res.data.total
  } catch { /* Axios interceptor handles error message */ } finally {
    loading.value = false
  }
}

async function fetchDeps() {
  try {
    const [roleRes, deptRes] = await Promise.all([roleApi.list(), deptApi.tree()])
    roleList.value = roleRes.data
    deptTree.value = deptRes.data
  } catch { /* ignore */ }
}

function handleSearch() {
  query.page = 1
  fetchList()
}

function handleReset() {
  query.keyword = ''
  query.status = undefined
  query.deptId = undefined
  handleSearch()
}

function handleAdd() {
  formData.value = null
  formVisible.value = true
}

async function handleEdit(record) {
  // 先从数据库获取最新详情，确保角色等数据完整
  try {
    const res = await userApi.detail(record.id)
    formData.value = res.data
  } catch {
    formData.value = { ...record }
  }
  formVisible.value = true
}

async function handleDelete(id) {
  await userApi.delete(id)
  message.success(t('common.deleteSuccess'))
  fetchList()
}

function handlePageChange(page, pageSize) {
  query.page = page
  query.pageSize = pageSize
  fetchList()
}

const columns = [
  { title: t('user.username'), dataIndex: 'username', key: 'username' },
  { title: t('user.nickname'), dataIndex: 'nickname', key: 'nickname' },
  { title: t('user.phone'), dataIndex: 'phone', key: 'phone' },
  { title: t('user.email'), dataIndex: 'email', key: 'email' },
  { title: t('common.status'), dataIndex: 'status', key: 'status' },
  { title: t('common.createTime'), dataIndex: 'createTime', key: 'createTime' },
  { title: t('common.action'), key: 'action', width: 180 },
]
</script>

<template>
  <div class="content-page">
    <div class="page-header">
      <h2 class="page-title">{{ t('menu.user') }}</h2>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <a-input
        v-model:value="query.keyword"
        :placeholder="t('common.search') + '...'"
        style="width: 240px"
        @pressEnter="handleSearch"
      />
      <a-select
        v-model:value="query.status"
        :placeholder="t('common.status')"
        style="width: 120px"
        allow-clear
      >
        <a-select-option :value="1">{{ t('common.enabled') }}</a-select-option>
        <a-select-option :value="0">{{ t('common.disabled') }}</a-select-option>
      </a-select>
      <a-tree-select
        v-model:value="query.deptId"
        :placeholder="t('user.dept')"
        :tree-data="deptTree"
        :field-names="{ children: 'children', label: 'deptName', value: 'id' }"
        style="width: 180px"
        allow-clear
      />
      <a-button type="primary" @click="handleSearch">
        {{ t('common.search') }}
      </a-button>
      <a-button @click="handleReset">{{ t('common.reset') }}</a-button>
    </div>

    <!-- 表格 -->
    <div class="table-container">
      <div style="padding: 12px 16px; display: flex; justify-content: space-between">
        <a-button v-if="hasPermission('sys:user:add')" type="primary" @click="handleAdd">{{ t('common.add') }}</a-button>
      </div>
      <a-table
        :columns="columns"
        :data-source="list"
        :loading="loading"
        :pagination="{
          current: query.page,
          pageSize: query.pageSize,
          total,
          showSizeChanger: true,
          showQuickJumper: true,
          showTotal: (t) => `共 ${t} 条`,
        }"
        row-key="id"
        @change="({ current, pageSize }) => handlePageChange(current, pageSize)"
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
              <a-button v-if="hasPermission('sys:user:edit')" type="link" size="small" @click="handleEdit(record)">{{ t('common.edit') }}</a-button>
              <a-popconfirm
                v-if="hasPermission('sys:user:delete')"
                :title="t('common.deleteConfirm')"
                @confirm="handleDelete(record.id)"
              >
                <a-button type="link" size="small" danger>{{ t('common.delete') }}</a-button>
              </a-popconfirm>
            </div>
          </template>
        </template>
      </a-table>
    </div>

    <!-- 新增/编辑弹窗 -->
    <UserForm
      v-if="formVisible"
      v-model:visible="formVisible"
      :record="formData"
      :role-list="roleList"
      :dept-tree="deptTree"
      @success="fetchList"
    />
  </div>
</template>