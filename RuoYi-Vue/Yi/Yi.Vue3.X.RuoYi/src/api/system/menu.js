import request from '@/utils/request'

// 查询菜单列表
export function listMenu(query) {
  return request({
    url: '/menu/selctGetList',
    method: 'get',
    params: query
  })
}

// 查询菜单详细
export function getMenu(menuId) {
  return request({
    url: '/menu/getById/' + menuId,
    method: 'get'
  })
}

// 查询菜单下拉树结构
export function treeselect() {
  return request({
    url: '/system/menu/treeselect',
    method: 'get'
  })
}

// 根据角色ID查询菜单下拉树结构
export function roleMenuTreeselect(roleId) {
  return request({
    url: '/menu/getListByRoleId/' + roleId,
    method: 'get'
  })
}

// 新增菜单
export function addMenu(data) {
  return request({
    url: '/menu/add',
    method: 'post',
    data: data
  })
}

// 修改菜单
export function updateMenu(data) {
  return request({
    url: '/menu/update',
    method: 'put',
    data: data
  })
}

// 删除菜单
export function delMenu(menuId) {
  if("string"==typeof(menuId))
  {
    menuId=[menuId];
  }
  return request({
    url: '/menu/delList',
    method: 'delete',
    data:menuId
  })
}