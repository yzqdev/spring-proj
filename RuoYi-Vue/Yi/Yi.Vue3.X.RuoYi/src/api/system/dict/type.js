import request from '@/utils/request'

// 查询字典类型列表
export function listType(query) {
  return request({
    url: '/dictionary/pageList',
    method: 'get',
    params: query
  })
}

// 查询字典类型详细
export function getType(dictId) {
  return request({
    url: '/dictionary/getById/' + dictId,
    method: 'get'
  })
}

// 新增字典类型
export function addType(data) {
  return request({
    url: '/dictionary/add',
    method: 'post',
    data: data
  })
}

// 修改字典类型
export function updateType(data) {
  return request({
    url: '/dictionary/update',
    method: 'put',
    data: data
  })
}

// 删除字典类型
export function delType(dictId) {
  if("string"==typeof(dictId))
{
  dictId=[dictId];
}
  return request({
    url: '/dictionary/delList',
    method: 'delete',
    data:dictId
  })
}

// 刷新字典缓存
export function refreshCache() {
  return request({
    url: '/system/dict/type/refreshCache',
    method: 'delete'
  })
}

// 获取字典选择框列表
export function optionselect() {
  return request({
    url: '/dictionary/getList',
    method: 'get'
  })
}
