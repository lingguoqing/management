import request from '@/utils/request'

/** 部门管理 API */
export const deptApi = {
  /** 获取部门树 */
  tree() {
    return request.get('/dept/tree')
  },

  /** 新增 */
  create(data) {
    return request.post('/dept', data)
  },

  /** 修改 */
  update(data) {
    return request.put('/dept', data)
  },

  /** 删除 */
  delete(id) {
    return request.delete(`/dept/${id}`)
  },
}