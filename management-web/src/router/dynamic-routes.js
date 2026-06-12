// 使用 Vite 的 glob 导入，所有 views 下的 vue 文件都会被导入
const modules = import.meta.glob('@/views/**/*.vue')

// 未开发页面占位组件
const developingComponent = () => import('@/views/system/developing.vue')

/**
 * 根据后端 component 路径动态解析组件
 * 后端路径格式: 'test/test/index' -> 前端文件: '@/views/test/test/index.vue'
 */
function resolveComponent(componentPath) {
  if (!componentPath) return developingComponent

  // 将 'test/test/index' 转换为 '/src/views/test/test/index.vue'
  const filePath = componentPath.replace(/^\//, '')
  const fullPath = `/src/views/${filePath}.vue`

  // 使用 glob 匹配的模块
  return modules[fullPath] || developingComponent
}

/** Names of dynamically added routes (for cleanup on re-add / logout) */
let dynamicRouteNames = []

/** Generate and add dynamic routes from backend menu data */
export function addDynamicRoutes(router, menus) {
  // Remove previously added dynamic routes
  for (const name of dynamicRouteNames) {
    if (router.hasRoute(name)) {
      router.removeRoute(name)
    }
  }
  dynamicRouteNames = []

  function process(items) {
    for (const item of items) {
      // Directory (permType=1): add redirect route
      if (item.permType === 1 && item.path) {
        const firstChild = item.children?.find((c) => c.permType === 2 && c.path)
        const routeName = item.permName
        if (!router.hasRoute(routeName)) {
          // 找到有效的子菜单路径才添加重定向路由
          if (firstChild?.path && firstChild.path !== item.path) {
            router.addRoute('Main', {
              path: item.path.replace(/^\//, ''),
              name: routeName,
              redirect: firstChild.path.replace(/^\//, ''),
            })
            dynamicRouteNames.push(routeName)
          }
          // 如果没有有效子菜单，不添加路由（避免点击时报错）
        }
      }
      // Menu (permType=2): add page route
      if (item.permType === 2 && item.component) {
        const routeName = item.permName
        // 自动根据后端 component 路径解析组件
        const component = resolveComponent(item.component)
        if (!router.hasRoute(routeName)) {
          router.addRoute('Main', {
            path: item.path.replace(/^\//, ''),
            name: routeName,
            component: component,
            meta: {
              title: item.permName,
              permission: item.permCode,
              icon: item.icon,
              keepAlive: item.keepAlive === 1,
            },
          })
          dynamicRouteNames.push(routeName)
        }
      }
      if (item.children) process(item.children)
    }
  }

  process(menus || [])
}

/** Clear dynamic routes (called on logout) */
export function resetRoutes(router) {
  for (const name of dynamicRouteNames) {
    if (router.hasRoute(name)) {
      router.removeRoute(name)
    }
  }
  dynamicRouteNames = []
}