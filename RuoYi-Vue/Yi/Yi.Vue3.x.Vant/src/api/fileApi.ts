import myaxios from '@/utils/myaxios'

export default{
   upload(type:string,data:any){
    return myaxios({
        url: `/file/upload/${type}`,
        headers:{"Content-Type": "application/x-www-form-urlencoded; charset=UTF-8"},
        method: 'POST',
        data:data
      });
} 
}