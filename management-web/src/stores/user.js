import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi } from '@/api/auth'

/** 用户 Store —— 管理登录状态、Token 与用户信息 */
export const useUserStore = defineStore(
  'user',
  () => {
    const token = ref('')
    const userInfo = ref(null)

    const isLoggedIn = computed(() => !!token.value)
    const roles = computed(() => userInfo.value?.roles || [])
    const permissions = computed(() => userInfo.value?.permissions || [])
    const menus = computed(() => userInfo.value?.menus || [])

    /** 是否有指定权限 */
    const hasPermission = (perm) => {
      if (roles.value.includes('admin')) return true
      return permissions.value.includes(perm)
    }

    /** 登录（只获取 token，用户信息通过 fetchUserInfo 获取） */
    async function login(username, password) {
      const res = await authApi.login({ username, password })
      token.value = res.data.token
      // 登录成功后立即获取用户信息
      await fetchUserInfo()
    }

    /** 获取用户信息（含菜单、角色、权限） */
    async function fetchUserInfo() {
      const res = await authApi.getUserInfo()
      userInfo.value = res.data
    }

    /** 登出 */
    function logout() {
      token.value = ''
      userInfo.value = null
    }

    return {
      token,
      userInfo,
      isLoggedIn,
      roles,
      permissions,
      menus,
      hasPermission,
      login,
      fetchUserInfo,
      logout,
    }
  },
  {
    persist: {
      key: 'rbac-user',
      pick: ['token'],
    },
  },
)