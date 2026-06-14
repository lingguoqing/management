import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { addDynamicRoutes } from './dynamic-routes'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/login/index.vue'),
      meta: { title: '登录' },
    },
    {
      path: '/',
      name: 'Main',
      component: () => import('@/components/layout/MainLayout.vue'),
    },
  ],
})

// Track whether routes have been loaded
let routesLoaded = false

/** Find the first leaf menu path for redirect from / */
function findFirstMenuPath(menus) {
  for (const item of menus) {
    // type=1 is menu, type=0 is directory (workbench/homepage often type=0 with component)
    if ((item.type === 1 || item.type === 0) && item.path && item.component) return item.path
    if (item.children) {
      const found = findFirstMenuPath(item.children)
      if (found) return found
    }
  }
  return undefined
}

/** Load userInfo and routes synchronously if token exists */
function preloadRoutes() {
  // Don't await - let it run in background
  const userStore = useUserStore()
  if (userStore.token && !routesLoaded) {
    userStore.fetchUserInfo()
      .then(() => {
        addDynamicRoutes(router, userStore.menus)
        routesLoaded = true
      })
      .catch(() => {
        // token invalid, will redirect to login
      })
  }
}

// Route guard
router.beforeEach(async (to, _from, next) => {
  const userStore = useUserStore()

  if (to.path === '/login') {
    if (userStore.isLoggedIn) {
      return next('/')
    }
    routesLoaded = false
    return next()
  }

  if (!userStore.isLoggedIn) {
    return next('/login')
  }

  // If routes not loaded yet, wait for them
  if (!routesLoaded) {
    try {
      await userStore.fetchUserInfo()
      addDynamicRoutes(router, userStore.menus)
      routesLoaded = true
      // Use replace to avoid navigation history issues
      return next({ path: to.path, replace: true })
    } catch {
      userStore.logout()
      return next('/login')
    }
  }

  if (to.path === '/') {
    const firstPath = findFirstMenuPath(userStore.menus)
    if (firstPath) return next(firstPath)
  }

  next()
})

// Start preloading routes when router is ready
router.isReady().then(preloadRoutes)

export default router