import { axios } from '@/utils/request'

export function testNiceDay() {
  return axios({
    url: '/test/niceDay',
    method: 'get',
  })
}
