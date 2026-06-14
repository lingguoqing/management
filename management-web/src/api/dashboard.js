import request from '@/utils/request'

/** 仪表盘 API */
export const dashboardApi = {
  /** 获取统计信息 */
  stats() {
    return request.get('/dashboard/stats')
  },
}