// Vite glob import for all vue files under views
const modules = import.meta.glob('@/views/**/*.vue')

// Placeholder component for undeveloped pages
const developingComponent = () => import('@/views/system/developing.vue')

// Resolve component from backend component path
function resolveComponent(componentPath) {
  if (!componentPath) return developingComponent

  // Remove leading slash and construct full path
  const filePath = componentPath.replace(/^\//, '')
  const fullPath = '/src/views/' + filePath + '.vue'

  // Use glob matched module
  return modules[fullPath] || developingComponent
}

// Names of dynamically added routes (for cleanup on re-add / logout)
let dynamicRouteNames = []

// Generate and add dynamic routes from backend menu data
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
      // Directory (type=0): add redirect route
      if (item.type === 0 && item.path) {
        const firstChild = item.children && item.children.find((c) => c.type === 1 && c.path)
        const routeName = item.name || String(item.id)
        if (!router.hasRoute(routeName)) {
          // Only add redirect if has valid child menu path
          if (firstChild && firstChild.path && firstChild.path !== item.path) {
            router.addRoute('Main', {
              path: item.path.replace(/^\//, ''),
              name: routeName,
              redirect: firstChild.path.replace(/^\//, ''),
            })
            dynamicRouteNames.push(routeName)
          }
        }
      }
      // Menu (type=1) OR Directory (type=0) with component: add page route
      // Directories with component like workbench/homepage should be treated as page
      if ((item.type === 1 || (item.type === 0 && item.component)) && item.path) {
        const routeName = item.name || String(item.id)
        // Auto resolve component from backend component path
        const component = resolveComponent(item.component)
        if (!router.hasRoute(routeName)) {
          router.addRoute('Main', {
            path: item.path.replace(/^\//, ''),
            name: routeName,
            component: component,
            meta: {
              title: item.title,
              permission: item.permission,
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

// Clear dynamic routes (called on logout)
export function resetRoutes(router) {
  for (const name of dynamicRouteNames) {
    if (router.hasRoute(name)) {
      router.removeRoute(name)
    }
  }
  dynamicRouteNames = []
}
