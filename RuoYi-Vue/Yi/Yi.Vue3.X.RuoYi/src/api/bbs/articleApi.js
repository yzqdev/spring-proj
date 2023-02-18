import request from '@/utils/request'

// 分页查询
export function listData(query) {
  return request({
    url: '/article/pageList',
    method: 'get',
    params: query
  })
}

// id查询
export function getData(code) {
  return request({
    url: '/article/getById/' + code,
    method: 'get'
  })
}

// 新增
export function addData(data) {
  return request({
    url: '/article/add',
    method: 'post',
    data: data
  })
}

// 修改
export function updateData(data) {
  return request({
    url: '/article/update',
    method: 'put',
    data: data
  })
}

// 删除
export function delData(code) {
  return request({
    url: '/article/delList',
    method: 'delete',
    data:"string"==typeof(code)?[code]:code
  })
}
