import myaxios from '@/utils/myaxios'

export default {
    add(data:any) {
        return myaxios({
            url: `/sku/add`,
            method: 'post',
            data: data
        })
    },
    pageList(data:any) {
        return myaxios({
          url: '/sku/pageList',
          method: 'get',
          params: data
        })
      }
}