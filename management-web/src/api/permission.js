import request from '@/utils/request'

/** 菜单/权限管理 API */
export const permissionApi = {
  /** 获取权限树 */
  tree() {
    return request.get('/permission/tree')
  },

  /** 获取单个权限详情 */
  get(id) {
    return request.get(`/permission/${id}`)
  },

  /** 新增 */
  create(data) {
    return request.post('/permission', data)
  },

  /** 修改 */
  update(data) {
    return request.put('/permission', data)
  },

  /** 删除 */
  delete(id) {
    return request.delete(`/permission/${id}`)
  },
}