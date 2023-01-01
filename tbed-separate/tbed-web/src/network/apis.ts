import http from "@/network/initHttp";

export function checkStatus(){
    return http.post('/checkStatus')
}
