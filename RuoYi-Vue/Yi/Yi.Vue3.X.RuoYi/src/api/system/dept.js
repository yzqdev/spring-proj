import request from '@/utils/request'

// 查询部门列表
export function listDept(query) {
  return request({
    url: '/dept/SelctGetList',
    method: 'get',
    params: query
  })
}

// // 查询部门列表（排除节点）
// export function listDeptExcludeChild(deptId) {
//   return request({
//     url: '/system/dept/list/exclude/' + deptId,
//     method: 'get'
//   })
// }

// 查询部门详细
export function getDept(deptId) {
  return request({
    url: '/dept/getById/' + deptId,
    method: 'get'
  })
}

// 新增部门
export function addDept(data) {
  return request({
    url: '/dept/add',
    method: 'post',
    data: data
  })
}

// 修改部门
export function updateDept(data) {
  return request({
    url: '/dept/update',
    method: 'put',
    data: data
  })
}

// 删除部门
export function delDept(deptId) {
  if("string"==typeof(deptId))
  {
    deptId=[deptId];
  }
  return request({
    url: '/dept/delList',
    method: 'delete',
    data:deptId
  })
}


// 根据角色ID查询菜单下拉树结构
export function roleDeptTreeselect(roleId) {
  return request({
    url: '/dept/getListByRoleId/' + roleId,
    method: 'get'
  })
}