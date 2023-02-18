import myaxios from '@/util/myaxios'
export default {
    GetPanFiles(dirName) {
        return myaxios({
            url: '/Pan/GetPanFiles',
            method: 'post',
            data: { dirName}
        })
    },
   Download(dirName,allName)
   {
    return myaxios({
        url: '/Pan/Download',
        method: 'post',
        data: { dirName,allName}
    })

   }
}