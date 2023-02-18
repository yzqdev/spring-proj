import myaxios from '@/utils/myaxios'

export default {
    add(data:any) {
        return myaxios({
            url: `/comment/add`,
            method: 'post',
            data: data
        })
    },
    getListByArticleId(articleId:any) {
        return myaxios({
          url: `/comment/GetListByArticleId/${articleId}`,
          method: 'get',
        })
      }
}