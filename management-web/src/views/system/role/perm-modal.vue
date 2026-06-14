<script setup>
import { ref, watch, nextTick } from 'vue'
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
const halfCheckedKeys = ref([])


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
    // 树节点的 id 是字符串，perm-ids 也返回字符串，保持一致避免类型不匹配导致勾选失效
    checkedKeys.value = permRes.data || []
  } finally {
    loading.value = false
    loadLock = false
  }
}

/** 递归收集树节点的所有子孙节点ID */
function collectDescendantIds(node, result = []) {
  if (node.children && node.children.length > 0) {
    for (const child of node.children) {
      result.push(String(child.id))
      collectDescendantIds(child, result)
    }
  }
  return result
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
    // 计算最终权限：checkedKeys + halfCheckedKeys - 未勾选父节点的所有子孙节点
    const checked = new Set(checkedKeys.value.map(String))
    const half = new Set(halfCheckedKeys.value.map(String))
    const finalIds = new Set([...checked, ...half])

    // 找出所有被取消勾选的父节点，移除其子孙节点ID
    for (const node of treeData.value) {
      collectAndRemoveDescendants(node, finalIds, checked)
    }

    const permIds = Array.from(finalIds)
    await roleApi.assignPerms(props.roleId, permIds)
    message.success('权限分配成功')
    emit('success')
    emit('update:visible', false)
  } finally {
    confirmLoading.value = false
  }
}

/** 如果父节点未被勾选，则移除其所有子孙节点ID */
function collectAndRemoveDescendants(node, finalIds, checked) {
  const nodeId = String(node.id)
  if (!checked.has(nodeId) && node.children && node.children.length > 0) {
    // 父节点未勾选，收集所有子孙节点并从最终列表中移除
    const descendantIds = collectDescendantIds(node)
    for (const id of descendantIds) {
      finalIds.delete(id)
    }
  } else if (node.children) {
    // 父节点已勾选或无子节点，继续递归检查子节点
    for (const child of node.children) {
      collectAndRemoveDescendants(child, finalIds, checked)
    }
  }
}

function handleCheck(_checked, info) {
  halfCheckedKeys.value = info.halfCheckedKeys || []
}
</script>

<template>
  <a-modal
    :open="visible"
    title="分配权限"
    :confirm-loading="confirmLoading"
    width="480px"
    @ok="handleSubmit"
    @cancel="() => emit('update:visible', false)"
  >
    <a-spin :spinning="loading">
      <a-tree
        v-if="treeData.length > 0"
        v-model:checkedKeys="checkedKeys"
        checkable
        :tree-data="treeData"
        :field-names="{ children: 'children', title: 'title', key: 'id' }"
        :default-expand-all="true"
        style="max-height: 420px; overflow: auto"
        @check="handleCheck"
      />
      <a-empty v-else description="暂无权限数据" />
    </a-spin>
  </a-modal>
</template>