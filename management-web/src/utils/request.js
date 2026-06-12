import axios from 'axios'
import { message } from 'ant-design-vue'
import { useUserStore } from '@/stores/user'

/**
 * Axios 封装 —— 统一拦截请求与响应
 */
const request = axios.create({
  baseURL: '/api',
  timeout: 30000,
})

// 请求拦截器：自动附带 Sa-Token
request.interceptors.request.use(
  (config) => {
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers['Authorization'] = userStore.token
    }
    return config
  },
  (error) => Promise.reject(error),
)

// 响应拦截器：统一处理错误
request.interceptors.response.use(
  (response) => {
    const { data } = response
    // 如果是文件下载，直接返回
    if (response.config.responseType === 'blob') {
      return response
    }
    // 业务状态码判断
    if (data.code !== 200) {
      message.error(data.message || '请求失败')
      return Promise.reject(new Error(data.message))
    }
    return data
  },
  (error) => {
    if (error.response) {
      const { status } = error.response
      switch (status) {
        case 401:
          message.error('登录已过期，请重新登录')
          // 清除 token 并跳转到登录页
          localStorage.clear()
          window.location.href = '/login'
          break
        case 403:
          message.error('无权限访问')
          break
        case 500:
          message.error('服务器内部错误')
          break
        default:
          message.error(error.message || '网络错误')
      }
    } else {
      message.error('网络连接失败')
    }
    return Promise.reject(error)
  },
)

export default request