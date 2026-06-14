import request from '@/utils/request'

/** 字典管理 API */
export const dictApi = {
  // ---- 字典类型 ----
  pageTypes(params) {
    return request.post('/dict/type/page', params)
  },
  createType(data) {
    return request.post('/dict/type', data)
  },
  updateType(data) {
    return request.put('/dict/type', data)
  },
  deleteType(id) {
    return request.delete(`/dict/type/${id}`)
  },

  // ---- 字典数据 ----
  pageData(params) {
    return request.post('/dict/data/page', params)
  },
  listByType(dictType) {
    return request.get(`/dict/data/${dictType}`)
  },
  createData(data) {
    return request.post('/dict/data', data)
  },
  updateData(data) {
    return request.put('/dict/data', data)
  },
  deleteData(id) {
    return request.delete(`/dict/data/${id}`)
  },
}