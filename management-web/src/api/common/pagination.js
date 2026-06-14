import request from '@/utils/request'

/**
 * POST 分页请求
 * @param {string} url - 请求路径
 * @param {object} query - 分页查询参数
 * @returns {Promise}
 */
export function postPage(url, query) {
  return request.post(url, query)
}

/**
 * POST 分页请求（带 Loading）
 * @param {string} url - 请求路径
 * @param {object} query - 分页查询参数
 * @param {ref} loadingRef - loading 状态引用
 * @returns {Promise}
 */
export async function postPageWithLoading(url, query, loadingRef) {
  if (loadingRef) loadingRef.value = true
  try {
    return await request.post(url, query)
  } finally {
    if (loadingRef) loadingRef.value = false
  }
}