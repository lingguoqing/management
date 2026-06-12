<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import { dictApi } from '@/api/dict'
import { formatDateTime } from '@/utils'
import DictTypeForm from './type-form.vue'
import DictDataForm from './data-form.vue'

const { t } = useI18n()

// 字典类型表格
const typeLoading = ref(false)
const typeList = ref([])
const typeTotal = ref(0)
const typeQuery = reactive({ page: 1, pageSize: 10, keyword: '' })
const typeFormVisible = ref(false)
const typeFormData = ref(null)

// 字典数据表格
const currentType = ref('')
const dataLoading = ref(false)
const dataList = ref([])
const dataTotal = ref(0)
const dataQuery = reactive({ page: 1, pageSize: 10, keyword: '' })
const dataFormVisible = ref(false)
const dataFormData = ref(null)

onMounted(() => fetchTypes())

async function fetchTypes() {
  typeLoading.value = true
  try {
    const res = await dictApi.pageTypes(typeQuery)
    typeList.value = res.data.records
    typeTotal.value = res.data.total
  } catch { /* ignore */ } finally {
    typeLoading.value = false
  }
}

function handleSelectType(record) {
  currentType.value = record.dictType
  dataQuery.keyword = ''
  dataQuery.page = 1
  fetchData()
}

async function fetchData() {
  if (!currentType.value) return
  dataLoading.value = true
  try {
    const res = await dictApi.pageData({ ...dataQuery, page: dataQuery.page, pageSize: dataQuery.pageSize })
    dataList.value = res.data.records
    dataTotal.value = res.data.total
  } catch { /* ignore */ } finally {
    dataLoading.value = false
  }
}

const typeColumns = [
  { title: '字典名称', dataIndex: 'dictName', key: 'dictName' },
  { title: '字典编码', dataIndex: 'dictType', key: 'dictType' },
  { title: t('common.status'), dataIndex: 'status', key: 'status', width: 80 },
  { title: t('common.createTime'), dataIndex: 'createTime', key: 'createTime' },
  { title: t('common.action'), key: 'action', width: 200 },
]

const dataColumns = [
  { title: '字典标签', dataIndex: 'dictLabel', key: 'dictLabel' },
  { title: '字典值', dataIndex: 'dictValue', key: 'dictValue' },
  { title: '排序', dataIndex: 'sortOrder', key: 'sortOrder', width: 80 },
  { title: t('common.status'), dataIndex: 'status', key: 'status', width: 80 },
  { title: t('common.createTime'), dataIndex: 'createTime', key: 'createTime' },
  { title: t('common.action'), key: 'action', width: 160 },
]
</script>

<template>
  <div class="content-page">
    <div class="page-header">
      <h2 class="page-title">{{ t('menu.dict') }}</h2>
    </div>

    <a-row :gutter="16">
      <!-- 左侧：字典类型列表 -->
      <a-col :span="8">
        <div class="search-bar" style="flex-direction: column; gap: 8px">
          <a-input v-model:value="typeQuery.keyword" placeholder="搜索字典..." size="small" @pressEnter="fetchTypes" />
        </div>
        <div class="table-container">
          <div style="padding: 8px 12px">
            <a-button type="primary" size="small" @click="typeFormData = null; typeFormVisible = true">
              {{ t('common.add') }}
            </a-button>
          </div>
          <a-table
            :columns="typeColumns"
            :data-source="typeList"
            :loading="typeLoading"
            :pagination="{ pageSize: 10, size: 'small' }"
            row-key="id"
            size="small"
            @row-click="handleSelectType"
          >
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'status'">
                <a-tag :color="record.status === 1 ? 'green' : 'red'" style="font-size: 11px">
                  {{ record.status === 1 ? t('common.enabled') : t('common.disabled') }}
                </a-tag>
              </template>
              <template v-if="column.key === 'createTime'">
                {{ formatDateTime(record.createTime).slice(0, 10) }}
              </template>
              <template v-if="column.key === 'action'">
                <div class="action-buttons">
                  <a-button type="link" size="small" @click.stop="typeFormData = record; typeFormVisible = true">
                    {{ t('common.edit') }}
                  </a-button>
                  <a-popconfirm :title="t('common.deleteConfirm')" @confirm="dictApi.deleteType(record.id).then(fetchTypes)">
                    <a-button type="link" size="small" danger>{{ t('common.delete') }}</a-button>
                  </a-popconfirm>
                </div>
              </template>
            </template>
          </a-table>
        </div>
      </a-col>

      <!-- 右侧：字典数据列表 -->
      <a-col :span="16">
        <div class="table-container">
          <div style="padding: 8px 12px; display: flex; justify-content: space-between">
            <span style="font-weight: 600; font-size: 14px">
              {{ currentType ? `字典数据 (${currentType})` : '请选择左侧字典类型' }}
            </span>
            <a-button type="primary" size="small" :disabled="!currentType" @click="dataFormData = null; dataFormVisible = true">
              {{ t('common.add') }}
            </a-button>
          </div>
          <a-table
            :columns="dataColumns"
            :data-source="dataList"
            :loading="dataLoading"
            :pagination="{ pageSize: 10, size: 'small' }"
            row-key="id"
            size="small"
          >
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'status'">
                <a-tag :color="record.status === 1 ? 'green' : 'red'" style="font-size: 11px">
                  {{ record.status === 1 ? t('common.enabled') : t('common.disabled') }}
                </a-tag>
              </template>
              <template v-if="column.key === 'createTime'">
                {{ formatDateTime(record.createTime).slice(0, 10) }}
              </template>
              <template v-if="column.key === 'action'">
                <div class="action-buttons">
                  <a-button type="link" size="small" @click="dataFormData = record; dataFormVisible = true">
                    {{ t('common.edit') }}
                  </a-button>
                  <a-popconfirm :title="t('common.deleteConfirm')" @confirm="dictApi.deleteData(record.id).then(fetchData)">
                    <a-button type="link" size="small" danger>{{ t('common.delete') }}</a-button>
                  </a-popconfirm>
                </div>
              </template>
            </template>
          </a-table>
        </div>
      </a-col>
    </a-row>

    <DictTypeForm v-if="typeFormVisible" v-model:visible="typeFormVisible" :record="typeFormData" @success="fetchTypes" />
    <DictDataForm v-if="dataFormVisible" v-model:visible="dataFormVisible" :record="dataFormData" :dict-type="currentType" @success="fetchData" />
  </div>
</template>