import myaxios from '@/util/myaxios'
export default {
    UploadImage(file) {
        return myaxios({
            url: '/Upload/image',
            method: 'post',
            headers: { "Content-Type": "multipart/form-data" },
            data: file
        })
    }
}