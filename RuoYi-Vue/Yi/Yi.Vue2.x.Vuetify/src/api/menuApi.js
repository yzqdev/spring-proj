import myaxios from '@/util/myaxios'
export default {
    getMenuTree() {
        return myaxios({
            url: '/Menu/getMenuTree',
            method: 'get'
        })
    },
    Update(data) {
        return myaxios({
            url: '/Menu/Update',
            method: 'put',
            data: data
        })
    },
    DeleteList(ids) {
        return myaxios({
            url: '/Menu/DeleteList',
            method: 'delete',
            data: ids
        })
    },
    Add(data) {
        return myaxios({
            url: '/Menu/Add',
            method: 'post',
            data: data
        })
    }
}