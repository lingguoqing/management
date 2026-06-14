<script setup>
import { ref, watch, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useAppStore } from '@/stores/app'
import { authApi } from '@/api/auth'
import { resetRoutes } from '@/router/dynamic-routes'
import {
  DashboardOutlined,
  SettingOutlined,
  MonitorOutlined,
  LogoutOutlined,
  UserOutlined,
  GlobalOutlined,
  TeamOutlined,
  MenuOutlined,
  ApartmentOutlined,
  BookOutlined,
  FileTextOutlined,
  SolutionOutlined,
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  HomeOutlined,
  DesktopOutlined,
  AppstoreOutlined,
  AppstoreAddOutlined,
  DatabaseOutlined,
  CloudOutlined,
  SafetyOutlined,
  KeyOutlined,
  LockOutlined,
  EyeOutlined,
  CameraOutlined,
  BellOutlined,
  MessageOutlined,
  MailOutlined,
  PhoneOutlined,
  MobileOutlined,
  ClockCircleOutlined,
  CalendarOutlined,
  SearchOutlined,
  AudioOutlined,
  VideoCameraOutlined,
} from '@ant-design/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const appStore = useAppStore()

const selectedKeys = ref([])
const openKeys = ref([])

// 图标映射表
const iconMap = {
  DashboardOutlined,
  SettingOutlined,
  MonitorOutlined,
  UserOutlined,
  TeamOutlined,
  MenuOutlined,
  ApartmentOutlined,
  BookOutlined,
  FileTextOutlined,
  SolutionOutlined,
  HomeOutlined,
  DesktopOutlined,
  AppstoreOutlined,
  AppstoreAddOutlined,
  DatabaseOutlined,
  CloudOutlined,
  SafetyOutlined,
  KeyOutlined,
  LockOutlined,
  EyeOutlined,
  CameraOutlined,
  BellOutlined,
  MessageOutlined,
  MailOutlined,
  PhoneOutlined,
  MobileOutlined,
  ClockCircleOutlined,
  CalendarOutlined,
  SearchOutlined,
  AudioOutlined,
  VideoCameraOutlined,
}

const routeParentKey = computed(() => {
  const parts = route.path.split('/')
  return parts.length > 2 ? '/' + parts[1] : ''
})

watch(
  [() => route.path, () => userStore.menus],
  () => {
    selectedKeys.value = [route.path]
    if (routeParentKey.value) {
      openKeys.value = [routeParentKey.value]
    }
  },
  { immediate: true },
)

function handleMenuClick({ key }) {
  router.push(key)
}

async function handleLogout() {
  try { await authApi.logout() } catch { /* ignore */ }
  userStore.logout()
  resetRoutes(router)
  router.push('/login')
}

function switchLanguage() {
  appStore.setLanguage(appStore.language === 'zh-CN' ? 'en-US' : 'zh-CN')
}

function toggleCollapse() {
  appStore.toggleCollapsed()
}
</script>

<template>
  <a-layout class="layout-root">
    <!-- 侧边栏 -->
    <a-layout-sider
      class="sidebar"
      theme="light"
      :width="200"
      :collapsed-width="64"
      :collapsed="appStore.collapsed"
      :trigger="null"
      collapsible
    >
      <!-- Logo 区域 -->
      <div class="sidebar-header">
        <div class="logo-mark">
          <svg width="24" height="24" viewBox="0 0 28 28" fill="none">
            <rect width="28" height="28" rx="5" fill="#1a1a2e"/>
            <path d="M8 14l4 4 8-8" stroke="#fff" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </div>
        <span v-show="!appStore.collapsed" class="logo-text">RBAC</span>
      </div>

      <!-- 菜单 -->
      <a-menu
        v-model:selectedKeys="selectedKeys"
        v-model:openKeys="openKeys"
        mode="inline"
        theme="light"
        :inline-collapsed="appStore.collapsed"
        @click="handleMenuClick"
      >
        <template v-for="menu in userStore.menus" :key="menu.id">
          <a-sub-menu v-if="menu.children && menu.children.length" :key="menu.path || String(menu.id)">
            <template #icon>
              <component :is="iconMap[menu.icon]" v-if="menu.icon && iconMap[menu.icon]" />
            </template>
            <template #title>{{ menu.title }}</template>
            <template v-for="child in menu.children" :key="child.id">
              <a-sub-menu v-if="child.children && child.children.length" :key="child.path || String(child.id)">
                <template #icon>
                  <component :is="iconMap[child.icon]" v-if="child.icon && iconMap[child.icon]" />
                </template>
                <template #title>{{ child.title }}</template>
                <a-menu-item v-for="sub in child.children" :key="sub.path || String(sub.id)">
                  {{ sub.title }}
                </a-menu-item>
              </a-sub-menu>
              <a-menu-item v-else :key="child.path || String(child.id)">
                <template #icon>
                  <component :is="iconMap[child.icon]" v-if="child.icon && iconMap[child.icon]" />
                </template>
                {{ child.title }}
              </a-menu-item>
            </template>
          </a-sub-menu>
          <a-menu-item v-else :key="menu.path || String(menu.id)">
            <template #icon>
              <component :is="iconMap[menu.icon]" v-if="menu.icon && iconMap[menu.icon]" />
            </template>
            {{ menu.title }}
          </a-menu-item>
        </template>
      </a-menu>
    </a-layout-sider>

    <!-- 主内容区 -->
    <a-layout :class="['main-layout', { collapsed: appStore.collapsed }]">
      <!-- 顶部导航 -->
      <a-layout-header class="header">
        <div class="header-left">
          <button class="trigger-btn" @click="toggleCollapse">
            <MenuFoldOutlined v-if="appStore.collapsed" />
            <MenuUnfoldOutlined v-else />
          </button>
          <span class="page-title">{{ route.meta.title || '概览' }}</span>
        </div>

        <div class="header-right">
          <button class="header-btn" @click="switchLanguage">
            <GlobalOutlined />
            <span>{{ appStore.language === 'zh-CN' ? 'EN' : '中' }}</span>
          </button>

          <a-dropdown>
            <div class="user-info">
              <a-avatar :size="28" class="avatar">
                {{ (userStore.userInfo?.nickname || userStore.userInfo?.username || 'A').charAt(0).toUpperCase() }}
              </a-avatar>
              <span class="username">{{ userStore.userInfo?.nickname || userStore.userInfo?.username || 'Admin' }}</span>
            </div>
            <template #overlay>
              <a-menu>
                <a-menu-item key="logout" @click="handleLogout">
                  <LogoutOutlined />
                  退出登录
                </a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
        </div>
      </a-layout-header>

      <!-- 内容区 -->
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<style scoped>
.layout-root {
  min-height: 100vh;
  background: #fafafa;
}

.sidebar {
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  background: #fff !important;
  border-right: 1px solid #e5e7eb;
  z-index: 100;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
}

.sidebar :deep(.ant-layout-sider-children) {
  display: flex;
  flex-direction: column;
}

.sidebar-header {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 16px;
  border-bottom: 1px solid #e5e7eb;
  min-height: 56px;
}

.logo-mark {
  flex-shrink: 0;
  display: flex;
  align-items: center;
}

.logo-text {
  font-size: 15px;
  font-weight: 700;
  color: #1a1a2e;
  letter-spacing: -0.3px;
  white-space: nowrap;
  overflow: hidden;
}

:deep(.ant-menu) {
  background: transparent !important;
  border-right: none;
  padding: 4px 8px;
}

:deep(.ant-menu-inline-collapsed) {
  width: 64px;
}

:deep(.ant-menu-item) {
  margin: 2px 0;
  border-radius: 6px;
  height: 36px;
  line-height: 36px;
  transition: all 0.15s ease;
}

:deep(.ant-menu-item:hover) {
  background: #f3f4f6 !important;
}

:deep(.ant-menu-item-selected) {
  background: #1a1a2e !important;
  color: #fff !important;
  font-weight: 500;
}

:deep(.ant-menu-item-selected .anticon) {
  color: #fff !important;
}

:deep(.ant-menu-submenu-title) {
  margin: 2px 0;
  border-radius: 6px;
  height: 36px;
  line-height: 36px;
  transition: all 0.15s ease;
}

:deep(.ant-menu-submenu-title:hover) {
  background: #f3f4f6 !important;
}

:deep(.ant-menu-submenu-selected) {
  color: #1a1a2e !important;
}

:deep(.ant-menu-submenu-selected .anticon) {
  color: #1a1a2e !important;
}

.main-layout {
  margin-left: 200px;
  transition: margin-left 0.2s ease;
}

.main-layout.collapsed {
  margin-left: 64px;
}

.header {
  position: sticky;
  top: 0;
  z-index: 50;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  height: 56px;
  background: #fff !important;
  border-bottom: 1px solid #e5e7eb;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.trigger-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  border: none;
  background: transparent;
  color: #6b7280;
  cursor: pointer;
  border-radius: 6px;
  transition: all 0.15s ease;
}

.trigger-btn:hover {
  background: #f3f4f6;
  color: #1a1a2e;
}

.page-title {
  font-size: 14px;
  font-weight: 600;
  color: #1a1a2e;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 8px;
}

.header-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  border: none;
  background: transparent;
  color: #6b7280;
  font-size: 12px;
  cursor: pointer;
  border-radius: 6px;
  transition: all 0.15s ease;
}

.header-btn:hover {
  background: #f3f4f6;
  color: #1a1a2e;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 4px 8px;
  cursor: pointer;
  border-radius: 6px;
  transition: all 0.15s ease;
}

.user-info:hover {
  background: #f3f4f6;
}

.avatar {
  background: #1a1a2e;
  font-size: 12px;
  font-weight: 600;
}

.username {
  font-size: 13px;
  color: #374151;
}

.content {
  min-height: calc(100vh - 56px);
}
</style>