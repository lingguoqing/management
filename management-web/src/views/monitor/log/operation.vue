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

// a-range-picker 绑定值（Dayjs 或空）
const rangeValue = ref([])

onMounted(() => fetchList())

async function fetchList() {
  loading.value = true
  try {
    const res = await logApi.pageOperation(query)
    list.value = res.data.records
    total.value = res.data.total
  } catch { /* ignore */ } finally {
    loading.value = false
  }
}

/** 日期范围变化时同步到 query */
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
  await logApi.clearOperation()
  message.success('操作日志已清空')
  fetchList()
}

const columns = [
  { title: '操作用户', dataIndex: 'username', key: 'username', width: 100 },
  { title: '操作模块', dataIndex: 'module', key: 'module', width: 100 },
  { title: '操作类型', dataIndex: 'operation', key: 'operation', width: 100 },
  { title: '请求地址', dataIndex: 'requestUrl', key: 'requestUrl' },
  { title: '请求方式', dataIndex: 'requestMethod', key: 'requestMethod', width: 80 },
  { title: '操作IP', dataIndex: 'ip', key: 'ip', width: 130 },
  { title: '耗时(ms)', dataIndex: 'costTime', key: 'costTime', width: 90 },
  { title: '状态', dataIndex: 'status', key: 'status', width: 80 },
  { title: '操作时间', dataIndex: 'createTime', key: 'createTime', width: 170 },
]
</script>

<template>
  <div class="content-page">
    <div class="page-header">
      <h2 class="page-title">{{ t('menu.operationLog') }}</h2>
    </div>

    <div class="search-bar">
      <a-input v-model:value="query.keyword" placeholder="用户/模块/操作..." style="width: 200px" @pressEnter="fetchList" />
      <a-range-picker v-model:value="rangeValue" style="width: 260px" @change="handleRangeChange" />
      <a-button type="primary" @click="query.page = 1; fetchList()">{{ t('common.search') }}</a-button>
      <a-popconfirm title="确定清空所有操作日志？" @confirm="handleClear">
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
          <template v-if="column.key === 'requestUrl'">
            <a-tooltip :title="record.requestUrl">
              <span style="max-width: 200px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; display: inline-block">
                {{ record.requestUrl }}
              </span>
            </a-tooltip>
          </template>
          <template v-if="column.key === 'createTime'">
            {{ formatDateTime(record.createTime) }}
          </template>
        </template>
      </a-table>
    </div>
  </div>
</template>