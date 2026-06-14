<script setup>
import { ref, watch, nextTick, computed } from 'vue'
import { message } from 'ant-design-vue'
import { roleApi } from '@/api/role'
import { permissionApi } from '@/api/permission'

const props = defineProps({
  visible: Boolean,
  roleId: [Number, String],
})

const emit = defineEmits(['update:visible', 'success'])

const loading = ref(false)
const confirmLoading = ref(false)
const treeData = ref([])
const checkedKeys = ref([])
const collapsedNodes = ref([]) // 记录被收起的节点ID

let loadLock = false
async function loadData() {
  if (!props.roleId) return
  if (loadLock) return
  loadLock = true
  loading.value = true
  try {
    const [treeRes, permRes] = await Promise.all([
      permissionApi.tree(),
      roleApi.getPermIds(props.roleId),
    ])
    treeData.value = treeRes.data
    checkedKeys.value = permRes.data ? permRes.data.map(String) : []
    // 默认全部展开，所以清空折叠记录
    collapsedNodes.value = []
  } finally {
    loading.value = false
    loadLock = false
  }
}

watch(
  () => [props.visible, props.roleId],
  async ([visible, roleId]) => {
    if (visible && roleId) {
      await nextTick()
      await loadData()
    }
  },
  { immediate: true },
)

async function handleSubmit() {
  confirmLoading.value = true
  try {
    const permIds = [...checkedKeys.value]
    await roleApi.assignPerms(props.roleId, permIds)
    message.success('权限分配成功')
    emit('success')
    emit('update:visible', false)
  } finally {
    confirmLoading.value = false
  }
}

/** 切换展开/收起 */
function toggleExpand(nodeId) {
  const id = String(nodeId)
  const idx = collapsedNodes.value.indexOf(id)
  if (idx >= 0) {
    collapsedNodes.value.splice(idx, 1)
  } else {
    collapsedNodes.value.push(id)
  }
}

/** 切换节点勾选状态（带父子联动） */
function toggleNode(node) {
  const nodeId = String(node.id)
  const idx = checkedKeys.value.indexOf(nodeId)
  if (idx >= 0) {
    // 取消勾选：同时取消所有子节点
    checkedKeys.value.splice(idx, 1)
    removeDescendants(node, checkedKeys.value)
  } else {
    // 勾选：同时勾选所有子节点和祖先节点
    checkedKeys.value.push(nodeId)
    addDescendants(node, checkedKeys.value)
    checkAncestors(nodeId, checkedKeys.value)
  }
}

/** 递归添加所有子节点 */
function addDescendants(node, list) {
  if (node.children) {
    for (const child of node.children) {
      const childId = String(child.id)
      if (!list.includes(childId)) {
        list.push(childId)
      }
      addDescendants(child, list)
    }
  }
}

/** 递归勾选所有祖先节点 */
function checkAncestors(nodeId, list) {
  // 在treeData中查找当前节点的父节点
  function findParent(nodes, targetId, parent = null) {
    for (const node of nodes) {
      if (String(node.id) === String(targetId)) {
        return parent
      }
      if (node.children) {
        const found = findParent(node.children, targetId, node)
        if (found) return found
      }
    }
    return null
  }

  let parent = findParent(treeData.value, nodeId)
  while (parent) {
    const parentId = String(parent.id)
    if (!list.includes(parentId)) {
      list.push(parentId)
    }
    // 继续向上查找父节点的父节点
    parent = findParent(treeData.value, parentId)
  }
}

/** 递归移除所有子节点 */
function removeDescendants(node, list) {
  if (node.children) {
    for (const child of node.children) {
      const childId = String(child.id)
      const idx = list.indexOf(childId)
      if (idx >= 0) list.splice(idx, 1)
      removeDescendants(child, list)
    }
  }
}

/** 检查节点是否有子节点 */
function hasChildren(node) {
  return node.children && node.children.length > 0
}

/** 检查节点是否可勾选（目录、菜单和按钮都可勾选） */
function isCheckable(node) {
  return true
}

/** 获取节点类型标签 */
function getTypeLabel(type) {
  if (type === 0) return '目录'
  if (type === 1) return '菜单'
  return '按钮'
}

/** 扁平化树结构用于渲染（带层级信息） */
const flatTree = computed(() => {
  const result = []
  function flatten(nodes, depth = 0, parentExpanded = true) {
    for (const node of nodes) {
      const nodeId = String(node.id)
      const hc = hasChildren(node)
      // 节点展开条件：有子节点 + 未被折叠 + 父节点已展开
      const isExpanded = parentExpanded && hc && !collapsedNodes.value.includes(nodeId)
      result.push({ ...node, depth, isExpanded, hasChildren: hc })
      if (hc && isExpanded) {
        flatten(node.children, depth + 1, true)
      }
    }
  }
  flatten(treeData.value)
  return result
})
</script>

<template>
  <a-modal
    :open="visible"
    title="分配权限"
    :confirm-loading="confirmLoading"
    width="560px"
    @ok="handleSubmit"
    @cancel="() => emit('update:visible', false)"
  >
    <a-spin :spinning="loading">
      <div v-if="flatTree.length > 0" class="perm-container">
        <div
          v-for="node in flatTree"
          :key="node.id"
          class="perm-row"
          :class="{
            'perm-row--child': node.depth > 0,
            'perm-row--checked': checkedKeys.includes(String(node.id)),
            'perm-row--dir': node.type === 0,
            'perm-row--menu': node.type === 1,
            'perm-row--btn': node.type === 2,
          }"
          :style="{ paddingLeft: (node.depth * 20 + 12) + 'px' }"
        >
          <!-- 展开/收起按钮 -->
          <span
            v-if="node.hasChildren"
            class="perm-toggle"
            @click.stop="toggleExpand(node.id)"
          >
            <svg :class="{ 'rotated': !node.isExpanded }" viewBox="0 0 16 16" fill="currentColor">
              <path d="M6 4l4 4-4 4"/>
            </svg>
          </span>
          <span v-else class="perm-toggle-placeholder"></span>

          <!-- 勾选框 -->
          <span
            v-if="isCheckable(node)"
            class="perm-checkbox"
            :class="{ 'is-checked': checkedKeys.includes(String(node.id)) }"
            @click.stop="toggleNode(node)"
          >
            <svg v-if="checkedKeys.includes(String(node.id))" viewBox="0 0 16 16" fill="currentColor">
              <path d="M13.78 4.22a.75.75 0 010 1.06l-7.25 7.25a.75.75 0 01-1.06 0L2.22 9.28a.75.75 0 011.06-1.06L6 10.94l6.72-6.72a.75.75 0 011.06 0z"/>
            </svg>
          </span>

          <!-- 类型标签 -->
          <span class="perm-badge" :class="'perm-badge--' + (node.type === 0 ? 'dir' : node.type === 1 ? 'menu' : 'btn')">
            {{ getTypeLabel(node.type) }}
          </span>

          <!-- 节点名称 -->
          <span class="perm-title" @click.stop="isCheckable(node) && toggleNode(node)">
            {{ node.title }}
          </span>
        </div>
      </div>
      <a-empty v-else description="暂无权限数据" />
    </a-spin>
  </a-modal>
</template>

<style scoped>
.perm-container {
  max-height: 420px;
  overflow-y: auto;
  padding: 4px;
  background: #F5F7FA;
  border-radius: 8px;
}

.perm-row {
  display: flex;
  align-items: center;
  padding: 10px 12px;
  margin: 2px 0;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.15s ease;
  gap: 8px;
}

.perm-row:hover {
  background: rgba(59, 130, 246, 0.06);
}

.perm-row--child {
  background: rgba(255, 255, 255, 0.7);
  margin-left: 8px;
  border-left: 2px solid #E2E8F0;
}

.perm-row--checked {
  background: rgba(59, 130, 246, 0.08);
}

/* 展开/收起箭头 */
.perm-toggle {
  width: 16px;
  height: 16px;
  color: #64748B;
  cursor: pointer;
  transition: transform 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.perm-toggle svg {
  width: 12px;
  height: 12px;
}

.perm-toggle svg.rotated {
  transform: rotate(0deg);
}

.perm-toggle:not(.perm-toggle-placeholder) svg {
  transform: rotate(90deg);
}

.perm-toggle-placeholder {
  width: 16px;
  flex-shrink: 0;
}

/* 勾选框 */
.perm-checkbox {
  width: 18px;
  height: 18px;
  border: 2px solid #CBD5E1;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.15s ease;
  flex-shrink: 0;
  cursor: pointer;
}

.perm-checkbox svg {
  width: 12px;
  height: 12px;
}

.perm-checkbox.is-checked {
  background: #3B82F6;
  border-color: #3B82F6;
  color: white;
}

.perm-checkbox:hover:not(.is-checked) {
  border-color: #3B82F6;
}

/* 类型标签 */
.perm-badge {
  font-size: 11px;
  font-weight: 600;
  padding: 2px 8px;
  border-radius: 4px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  flex-shrink: 0;
}

.perm-badge--dir {
  background: rgba(245, 158, 11, 0.12);
  color: #D97706;
}

.perm-badge--menu {
  background: rgba(59, 130, 246, 0.12);
  color: #2563EB;
}

.perm-badge--btn {
  background: rgba(139, 92, 246, 0.12);
  color: #7C3AED;
}

/* 节点标题 */
.perm-title {
  flex: 1;
  font-size: 14px;
  color: #334155;
  font-weight: 500;
}

.perm-row--checked .perm-title {
  color: #1E40AF;
}

/* 滚动条美化 */
.perm-container::-webkit-scrollbar {
  width: 6px;
}

.perm-container::-webkit-scrollbar-track {
  background: transparent;
}

.perm-container::-webkit-scrollbar-thumb {
  background: #CBD5E1;
  border-radius: 3px;
}

.perm-container::-webkit-scrollbar-thumb:hover {
  background: #94A3B8;
}
</style>
