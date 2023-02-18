import request from '@/utils/request'

// 查询参数列表
export function listConfig(query) {
  return request({
    url: '/config/pageList',
    method: 'get',
    params: query
  })
}

// 查询参数详细
export function getConfig(configId) {
  return request({
    url: '/config/getById/' + configId,
    method: 'get'
  })
}

// 根据参数键名查询参数值
export function getConfigKey(configKey) {
  return request({
    url: '/system/config/configKey/' + configKey,
    method: 'get'
  })
}

// 新增参数配置
export function addConfig(data) {
  return request({
    url: '/config/add',
    method: 'post',
    data: data
  })
}

// 修改参数配置
export function updateConfig(data) {
  return request({
    url: '/config/update',
    method: 'put',
    data: data
  })
}

// 删除参数配置
export function delConfig(configId) {

if("string"==typeof(configId))
{
  configId=[configId];
}
  return request({
    url: '/config/delList',
    method: 'delete',
    data:configId
  })
}

// 刷新参数缓存
export function refreshCache() {
  return request({
    url: '/system/config/refreshCache',
    method: 'delete'
  })
}
