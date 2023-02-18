import myaxios from '@/util/myaxios'
import {objctToDic} from '@/util/objctHandle'
export default {
    getList() {
        return myaxios({
            url: '/Role/GetList',
            method: 'post',
            data: objctToDic()
        })
    },
    giveRoleSetMenu(roleList, menuList) {
        return myaxios({
            url: '/Role/GiveRoleSetMenu',
            method: 'put',
            data: { RoleIds: roleList, menuIds: menuList }
        })
    },

    getInMenuByRoleId(roleId) {
        return myaxios({
            url: `/Role/GetInMenuByRoleId?roleId=${roleId}`,
            method: 'get'
        })
    }

}