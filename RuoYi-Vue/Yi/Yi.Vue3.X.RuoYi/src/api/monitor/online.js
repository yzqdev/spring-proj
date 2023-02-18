import request from '@/utils/request'

// 查询在线用户列表
export function list(query) {
  return request({
    url: '/online/pageList',
    method: 'get',
    params: query
  })
}

// 强退用户
export function forceLogout(tokenId) {
  return request({
    url: '/online/ForceOut/' + tokenId,
    method: 'delete'
  })
}
