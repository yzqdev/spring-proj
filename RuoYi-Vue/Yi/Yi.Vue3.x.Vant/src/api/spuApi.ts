import myaxios from '@/utils/myaxios'

export default {
  add(data: any) {
    return myaxios({
      url: `/spu/add`,
      method: 'post',
      data: data
    })
  },
  pageList(data: any) {
    return myaxios({
      url: '/spu/pageList',
      method: 'get',
      params: data
    })
  },
  getById(id: any) {
    return myaxios({
      url: `/spu/GetById/${id}`,
      method: 'get',
    })
  },

}