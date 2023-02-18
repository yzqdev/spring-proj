import myaxios from '@/utils/myaxios'
import { ArticleEntity } from '@/type/interface/ArticleEntity'

export default {
    add(data:any) {
        return myaxios({
            url: `/article/add`,
            method: 'post',
            data: data
        })
    },
    pageList(data:any) {
        return myaxios({
          url: '/article/pageList',
          method: 'get',
          params: data
        })
      }
}