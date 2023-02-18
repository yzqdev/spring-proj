import request from '@/utils/request.js'
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询用户列表
export function listUser(query) {
  return request({
    url: '/user/pageList',
    method: 'get',
    params: query
  })
}

// 查询用户详细
export function getUser(userId) {
  return request({
    url: '/user/getById/' + parseStrEmpty(userId),
    method: 'get'
  })
}

// 新增用户
export function addUser(data) {
  return request({
    url: '/user/add',
    method: 'post',
    data: data
  })
}

// 修改用户
export function updateUser(data) {
  return request({
    url: '/user/update',
    method: 'put',
    data: data
  })
}

// 删除用户
export function delUser(userId) {
  if("string"==typeof(userId))
  {
    userId=[userId];
  }
  return request({
    url: '/user/delList',
    method: 'delete',
    data:userId
  })
}

// 用户密码重置
export function resetUserPwd(id, password) {
  const data = {
    id,
    password
  }


  return request({
    url: '/user/restPassword',
    method: 'put',
    data: data
  })
}

// 用户状态修改
export function changeUserStatus(userId, isDel) {
  return request({
    url: `/user/updateStatus?userId=${userId}&isDel=${isDel}`,
    method: 'put'
  })
}

// 查询用户个人信息
export function getUserProfile() {
  return request({
    url: '/account/getUserAllInfo',
    method: 'get'
  })
}

// 修改用户个人信息
export function updateUserProfile(data) {
  return request({
    url: '/user/UpdateProfile',
    method: 'put',
    data: {user:data}
  })
}

// 用户密码重置
export function updateUserPwd(oldPassword, newPassword) {
  const data = {
    oldPassword,
    newPassword
  }
  return request({
    url: '/account/UpdatePassword',
    method: 'put',
    data: data
  })
}

// 用户头像上传
export function uploadAvatar(data) {
  return request({
    url: '/system/user/profile/avatar',
    method: 'post',
    data: data
  })
}

// 查询授权角色
export function getAuthRole(userId) {
  return request({
    url: '/system/user/authRole/' + userId,
    method: 'get'
  })
}

// 保存授权角色
export function updateAuthRole(data) {
  return request({
    url: '/system/user/authRole',
    method: 'put',
    params: data
  })
}

// // 查询部门下拉树结构
// export function deptTreeSelect() {
//   return request({
//     url: '/system/user/deptTree',
//     method: 'get'
//   })
// }
