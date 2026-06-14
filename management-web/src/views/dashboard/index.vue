<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { dashboardApi } from '@/api/dashboard'
import {
  UserOutlined,
  TeamOutlined,
  MenuOutlined,
  FileTextOutlined,
} from '@ant-design/icons-vue'

const userStore = useUserStore()
const loading = ref(false)
const stats = ref([
  { title: '用户总数', value: 0, icon: UserOutlined, color: '#1a1a2e' },
  { title: '角色数量', value: 0, icon: TeamOutlined, color: '#4f46e5' },
  { title: '菜单数量', value: 0, icon: MenuOutlined, color: '#0891b2' },
  { title: '今日操作', value: 0, icon: FileTextOutlined, color: '#059669' },
])

onMounted(async () => {
  loading.value = true
  try {
    const res = await dashboardApi.stats()
    const data = res.data
    stats.value[0].value = data.userCount ?? 0
    stats.value[1].value = data.roleCount ?? 0
    stats.value[2].value = data.permCount ?? 0
    stats.value[3].value = data.todayOperationCount ?? 0
  } catch {
    // ignore
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div class="content-page">
    <div class="page-header">
      <h2 class="page-title">仪表盘</h2>
      <p style="color: #6b7280; font-size: 13px; margin-top: 4px">
        欢迎回来，{{ userStore.userInfo?.nickname || userStore.userInfo?.username || 'Admin' }}
      </p>
    </div>

    <!-- 统计卡片 -->
    <div style="display: grid; grid-template-columns: repeat(auto-fill, minmax(240px, 1fr)); gap: 16px; margin-bottom: 24px">
      <div
        v-for="item in stats"
        :key="item.title"
        class="stat-card"
        style="display: flex; align-items: center; gap: 16px"
      >
        <div
          style="width: 48px; height: 48px; border-radius: 12px; display: flex; align-items: center; justify-content: center; font-size: 22px; color: #fff"
          :style="{ background: item.color }"
        >
          <component :is="item.icon" />
        </div>
        <div>
          <div style="font-size: 24px; font-weight: 700; color: #1a1a2e">{{ item.value }}</div>
          <div style="font-size: 13px; color: #6b7280">{{ item.title }}</div>
        </div>
      </div>
    </div>

    <!-- 快捷入口 -->
    <div class="stat-card" style="margin-bottom: 16px">
      <h4 style="margin-bottom: 12px; font-size: 15px; font-weight: 600">快捷操作</h4>
      <div style="display: flex; gap: 12px; flex-wrap: wrap">
        <router-link to="/system/user">
          <a-button type="default" style="border-radius: 8px">
            <UserOutlined /> 用户管理
          </a-button>
        </router-link>
        <router-link to="/system/role">
          <a-button type="default" style="border-radius: 8px">
            <TeamOutlined /> 角色管理
          </a-button>
        </router-link>
        <router-link to="/system/permission">
          <a-button type="default" style="border-radius: 8px">
            <MenuOutlined /> 菜单管理
          </a-button>
        </router-link>
      </div>
    </div>
  </div>
</template>