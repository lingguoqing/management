<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import { logApi } from '@/api/log.js'
import { formatDateTime } from '@/utils/index.js'

const { t } = useI18n()

const loading = ref(false)
const list = ref([])
const total = ref(0)

const query = reactive({
  page: 1,
  pageSize: 12,
  keyword: '',
  status: undefined,
  startTime: '',
  endTime: '',
})

const rangeValue = ref([])

onMounted(() => fetchList())

async function fetchList() {
  loading.value = true
  try {
    const res = await logApi.pageLogin(query)
    list.value = res.data.records
    total.value = res.data.total
  } catch { /* ignore */ } finally {
    loading.value = false
  }
}

function handleRangeChange(_dates, dateStrings) {
  query.startTime = dateStrings[0] || ''
  query.endTime = dateStrings[1] || ''
}

function handlePageChange(page, pageSize) {
  query.page = page
  query.pageSize = pageSize
  fetchList()
}

async function handleClear() {
  await logApi.clearLogin()
  message.success('登录日志已清空')
  fetchList()
}

const columns = [
  { title: '用户名', dataIndex: 'username', key: 'username', width: 120 },
  { title: '登录IP', dataIndex: 'ip', key: 'ip', width: 140 },
  { title: '登录地点', dataIndex: 'location', key: 'location', width: 140 },
  { title: '浏览器', dataIndex: 'browser', key: 'browser' },
  { title: '操作系统', dataIndex: 'os', key: 'os' },
  { title: '状态', dataIndex: 'status', key: 'status', width: 80 },
  { title: '提示信息', dataIndex: 'msg', key: 'msg' },
  { title: '登录时间', dataIndex: 'loginTime', key: 'loginTime', width: 170 },
]
</script>

<template>
  <div class="content-page">
    <div class="page-header">
      <h2 class="page-title">{{ t('menu.loginLog') }}</h2>
    </div>

    <div class="search-bar">
      <a-input v-model:value="query.keyword" placeholder="用户名..." style="width: 200px" @pressEnter="fetchList" />
      <a-select v-model:value="query.status" placeholder="状态" style="width: 100px" allow-clear>
        <a-select-option :value="1">成功</a-select-option>
        <a-select-option :value="0">失败</a-select-option>
      </a-select>
      <a-range-picker v-model:value="rangeValue" style="width: 260px" @change="handleRangeChange" />
      <a-button type="primary" @click="query.page = 1; fetchList()">{{ t('common.search') }}</a-button>
      <a-popconfirm title="确定清空所有登录日志？" @confirm="handleClear">
        <a-button danger>清空日志</a-button>
      </a-popconfirm>
    </div>

    <div class="table-container">
      <a-table
        :columns="columns"
        :data-source="list"
        :loading="loading"
        :pagination="{ current: query.page, pageSize: query.pageSize, total, showSizeChanger: true, showTotal: (t) => `共 ${t} 条` }"
        row-key="id"
        size="small"
        @change="({ current, pageSize }) => handlePageChange(current, pageSize)"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-tag :color="record.status === 1 ? 'green' : 'red'">
              {{ record.status === 1 ? '成功' : '失败' }}
            </a-tag>
          </template>
          <template v-if="column.key === 'loginTime'">
            {{ formatDateTime(record.loginTime) }}
          </template>
        </template>
      </a-table>
    </div>
  </div>
</template>