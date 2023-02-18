import myaxios from '@/utils/myaxios'

export default {
    operate(data:any) {
        return myaxios({
            url: `/agree/operate`,
            method: 'get',
            params: {articleId:data}
        })
    },
}