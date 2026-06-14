import request from '@/utils/request'

/** 日志管理 API */
export const logApi = {
  // ---- 操作日志 ----
  pageOperation(params) {
    return request.post('/log/operation/page', params)
  },
  clearOperation() {
    return request.delete('/log/operation')
  },

  // ---- 登录日志 ----
  pageLogin(params) {
    return request.post('/log/login/page', params)
  },
  clearLogin() {
    return request.delete('/log/login')
  },
}