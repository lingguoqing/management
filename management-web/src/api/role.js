import request from '@/utils/request'

/** 角色管理 API */
export const roleApi = {
  /** 分页查询 */
  page(params) {
    return request.post('/role/page', params)
  },

  /** 查询所有角色（下拉用） */
  list() {
    return request.get('/role/list')
  },

  /** 新增角色 */
  create(data) {
    return request.post('/role', data)
  },

  /** 修改角色 */
  update(data) {
    return request.put('/role', data)
  },

  /** 删除角色 */
  delete(id) {
    return request.delete(`/role/${id}`)
  },

  /** 获取角色的权限ID列表 */
  getPermIds(roleId) {
    return request.get(`/role/${roleId}/perm-ids`)
  },

  /** 分配角色权限 */
  assignPerms(roleId, permIds) {
    return request.put(`/role/${roleId}/permissions`, permIds)
  },
}