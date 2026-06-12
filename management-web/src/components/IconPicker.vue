<script setup>
import { ref, computed } from 'vue'
import * as Icons from '@ant-design/icons-vue'

const props = defineProps({
  modelValue: String,
})

const emit = defineEmits(['update:modelValue'])

const open = ref(false)

// 图标中文名称映射
const iconNameMap = {
  DashboardOutlined: '仪表盘',
  SettingOutlined: '设置',
  MonitorOutlined: '监控',
  UserOutlined: '用户',
  TeamOutlined: '团队',
  MenuOutlined: '菜单',
  ApartmentOutlined: '组织架构',
  BookOutlined: '书籍',
  FileTextOutlined: '文件',
  SolutionOutlined: '解决方案',
  HomeOutlined: '首页',
  DesktopOutlined: '桌面',
  LaptopOutlined: '笔记本',
  ProjectOutlined: '项目',
  AppstoreOutlined: '应用',
  AppstoreAddOutlined: '添加应用',
  DatabaseOutlined: '数据库',
  CloudOutlined: '云端',
  SafetyOutlined: '安全',
  KeyOutlined: '钥匙',
  LockOutlined: '锁',
  EyeOutlined: '眼睛',
  CameraOutlined: '相机',
  BellOutlined: '铃铛',
  MessageOutlined: '消息',
  MailOutlined: '邮件',
  PhoneOutlined: '电话',
  MobileOutlined: '手机',
  TabletOutlined: '平板',
  ClockCircleOutlined: '时钟',
  CalendarOutlined: '日历',
  SearchOutlined: '搜索',
  FilterOutlined: '筛选',
  SortAscendingOutlined: '升序',
  SortDescendingOutlined: '降序',
  PlusOutlined: '添加',
  PlusCircleOutlined: '添加圆形',
  MinusOutlined: '减少',
  MinusCircleOutlined: '减少圆形',
  CloseOutlined: '关闭',
  CheckOutlined: '勾选',
  CheckCircleOutlined: '成功',
  CloseCircleOutlined: '错误',
  ExclamationCircleOutlined: '警告',
  InfoCircleOutlined: '信息',
  QuestionCircleOutlined: '帮助',
  EditOutlined: '编辑',
  DeleteOutlined: '删除',
  CopyOutlined: '复制',
  CutOutlined: '剪切',
  PaperClipOutlined: '附件',
  DownloadOutlined: '下载',
  UploadOutlined: '上传',
  ImportOutlined: '导入',
  ExportOutlined: '导出',
  SaveOutlined: '保存',
  PrinterOutlined: '打印',
  RefreshOutlined: '刷新',
  SyncOutlined: '同步',
  PoweroffOutlined: '关机',
  LoginOutlined: '登录',
  LogoutOutlined: '退出',
  SmileOutlined: '微笑',
  MehOutlined: '面无表情',
  FrownOutlined: '皱眉',
  TrophyOutlined: '奖杯',
  StarOutlined: '星星',
  HeartOutlined: '爱心',
  LikeOutlined: '点赞',
  DislikeOutlined: '点踩',
  CommentOutlined: '评论',
  FileOutlined: '文件',
  FolderOutlined: '文件夹',
  FolderOpenOutlined: '打开文件夹',
  FileAddOutlined: '添加文件',
  FileExcelOutlined: 'Excel文件',
  FilePdfOutlined: 'PDF文件',
  FileImageOutlined: '图片',
  FileWordOutlined: 'Word文件',
  FilePptOutlined: 'PPT文件',
  FileZipOutlined: '压缩文件',
  VideoCameraOutlined: '视频',
  AudioOutlined: '音频',
  PlayCircleOutlined: '播放',
  PauseCircleOutlined: '暂停',
  StopOutlined: '停止',
  FastBackwardOutlined: '快退',
  FastForwardOutlined: '快进',
  BarChartOutlined: '柱状图',
  LineChartOutlined: '折线图',
  PieChartOutlined: '饼图',
  DotChartOutlined: '点图',
  TableOutlined: '表格',
  RigthOutlined: '右侧',
  LeftOutlined: '左侧',
  UpOutlined: '向上',
  DownOutlined: '向下',
  ArrowRightOutlined: '右箭头',
  ArrowLeftOutlined: '左箭头',
  ArrowUpOutlined: '上箭头',
  ArrowDownOutlined: '下箭头',
  SwapOutlined: '交换',
  SwapLeftOutlined: '左交换',
  SwapRightOutlined: '右交换',
  GlobalOutlined: '全局',
  ApiOutlined: 'API',
  ToolOutlined: '工具',
  BuildOutlined: '构建',
  ExperimentOutlined: '实验',
  HistoryOutlined: '历史',
  UndoOutlined: '撤销',
  RedoOutlined: '重做',
  SelectOutlined: '选择',
  LoadingOutlined: '加载中',
  Loading3QuarterOutlined: '加载中',
}

const iconList = Object.keys(Icons).filter(k => k.endsWith('Outlined') || k.endsWith('Filled') || k.endsWith('TwoTone'))

const selectedIcon = computed(() => props.modelValue)

function selectIcon(name) {
  emit('update:modelValue', name)
  open.value = false
}

function getIconName(name) {
  return iconNameMap[name] || name.replace(/Outlined|Filled|TwoTone$/, '')
}
</script>

<template>
  <a-popover v-model:open="open" trigger="click" placement="bottomLeft" :width="340">
    <template #content>
      <div class="icon-picker-grid">
        <a-tooltip v-for="name in iconList" :key="name" :title="getIconName(name)" placement="top">
          <div
            class="icon-item"
            :class="{ selected: selectedIcon === name }"
            @click="selectIcon(name)"
          >
            <component :is="Icons[name]" />
          </div>
        </a-tooltip>
      </div>
    </template>
    <a-input :value="modelValue ? `${getIconName(modelValue)} (${modelValue})` : ''" readonly placeholder="点击选择图标">
      <template #prefix>
        <component v-if="modelValue && Icons[modelValue]" :is="Icons[modelValue]" />
      </template>
    </a-input>
  </a-popover>
</template>

<style scoped>
.icon-picker-grid {
  display: grid;
  grid-template-columns: repeat(8, 1fr);
  gap: 4px;
  max-height: 280px;
  overflow-y: auto;
  padding: 8px;
}

.icon-item {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.15s ease;
  color: #6b7280;
  border: 1px solid transparent;
}

.icon-item:hover {
  background: #f3f4f6;
  color: #1a1a2e;
  border-color: #e5e7eb;
}

.icon-item.selected {
  background: #1a1a2e;
  color: #fff;
  border-color: #1a1a2e;
}
</style>