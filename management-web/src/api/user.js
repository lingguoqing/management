import request from '@/utils/request'

/** 用户管理 API */
export const userApi = {
  /** 分页查询 */
  page(params) {
    return request.get('/user/page', { params })
  },

  /** 获取用户详情 */
  detail(id) {
    return request.get(`/user/${id}`)
  },

  /** 新增用户 */
  create(data) {
    return request.post('/user', data)
  },

  /** 修改用户 */
  update(data) {
    return request.put('/user', data)
  },

  /** 删除用户 */
  delete(id) {
    return request.delete(`/user/${id}`)
  },
}