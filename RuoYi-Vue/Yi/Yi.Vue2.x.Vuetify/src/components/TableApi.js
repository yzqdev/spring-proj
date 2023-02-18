import myaxios from '@/util/myaxios'
import {objctToDic} from '@/util/objctHandle'
export default {
    getItem(url) {
        return myaxios({
            url: url,
            method: 'post',
            data: objctToDic()
        })
    },
    addItem(url, data) {
        return myaxios({
            url: url,
            method: 'post',
            data: data
        })
    },
    updateItem(url, data) {
        return myaxios({
            url: url,
            method: 'put',
            data: data
        })
    },
    delItemList(url, Ids) {
        return myaxios({
            url: url,
            method: 'delete',
            data: Ids
        })
    },
}