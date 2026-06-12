/**
 * 通用工具函数
 */

/** 生成树形结构（根据 parentId 递归） */
export function buildTree(list, parentId = 0, idKey = 'id', parentKey = 'parentId', childrenKey = 'children') {
  return list
    .filter((item) => item[parentKey] === parentId)
    .map((item) => ({
      ...item,
      [childrenKey]: buildTree(list, item[idKey], idKey, parentKey, childrenKey),
    }))
}

/** 格式化日期时间 */
export function formatDateTime(date) {
  if (!date) return '-'
  const d = new Date(date)
  const pad = (n) => n.toString().padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
}

/** 复制到剪贴板 */
export async function copyToClipboard(text) {
  try {
    await navigator.clipboard.writeText(text)
    return true
  } catch {
    // 降级方案
    const textarea = document.createElement('textarea')
    textarea.value = text
    textarea.style.position = 'fixed'
    textarea.style.opacity = '0'
    document.body.appendChild(textarea)
    textarea.select()
    document.execCommand('copy')
    document.body.removeChild(textarea)
    return true
  }
}