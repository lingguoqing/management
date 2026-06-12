<script setup>
import { ref, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { roleApi } from '@/api/role'
import { permissionApi } from '@/api/permission'

const props = defineProps({
  visible: Boolean,
  roleId: Number,
})

const emit = defineEmits(['update:visible', 'success'])

const loading = ref(false)
const confirmLoading = ref(false)
const treeData = ref([])
const checkedKeys = ref([])
const halfCheckedKeys = ref([])

onMounted(async () => {
  const [treeRes, permRes] = await Promise.all([
    permissionApi.tree(),
    roleApi.getPermIds(props.roleId),
  ])
  treeData.value = treeRes.data
  checkedKeys.value = permRes.data
})

async function handleSubmit() {
  confirmLoading.value = true
  try {
    const permIds = [...checkedKeys.value, ...halfCheckedKeys.value]
    await roleApi.assignPerms(props.roleId, permIds)
    message.success('权限分配成功')
    emit('success')
    emit('update:visible', false)
  } finally {
    confirmLoading.value = false
  }
}

/** Track half-checked parent nodes — v-model handles checkedKeys correctly on its own */
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
    <a-tree
      v-if="treeData.length > 0"
      v-model:checkedKeys="checkedKeys"
      checkable
      :tree-data="treeData"
      :field-names="{ children: 'children', title: 'permName', key: 'id' }"
      :default-expand-all="true"
      style="max-height: 420px; overflow: auto"
      @check="handleCheck"
    />
    <a-empty v-else description="暂无权限数据" />
  </a-modal>
</template>