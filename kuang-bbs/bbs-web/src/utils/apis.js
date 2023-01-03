import http from '@/utils/http'


export const loginPost=(username,password) => {
  return http.post(`/login?username=${username}&password=${password}`)
}
