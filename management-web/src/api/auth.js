import request from '@/utils/request'

/** 认证相关 API */
export const authApi = {
  /** 登录 */
  login(data) {
    return request.post('/auth/login', data)
  },

  /** 登出 */
  logout() {
    return request.post('/auth/logout')
  },

  /** 获取当前用户信息（含菜单/权限） */
  getUserInfo() {
    return request.get('/auth/user-info')
  },

  /** 修改密码 */
  updatePassword(data) {
    return request.put('/auth/password', data)
  },
}