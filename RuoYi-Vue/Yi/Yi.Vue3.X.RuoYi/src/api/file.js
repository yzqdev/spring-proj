import request from '@/utils/request'

export function
   upload(type,data){
    return request({
        url: `/file/upload/${type}`,
        headers:{"Content-Type": "application/x-www-form-urlencoded; charset=UTF-8"},
        method: 'POST',
        data:data
      });
} 
