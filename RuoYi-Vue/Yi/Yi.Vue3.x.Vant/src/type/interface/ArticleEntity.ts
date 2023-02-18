export interface ArticleEntity{
    title: string;
    content: string;
    images:string[];
    isDeleted:boolean;
    createTime:string;
}
// import { ArticleEntity } from '@/type/interface/ArticleEntity'