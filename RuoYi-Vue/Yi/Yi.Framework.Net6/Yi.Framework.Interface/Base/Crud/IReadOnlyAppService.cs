using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.Interface.Base.Crud
{

    public interface IReadOnlyAppService<TListDto, in TKey> : IReadOnlyAppService<TListDto, TListDto, TKey>
    {
    }

    public interface IReadOnlyAppService<TDetail, TListDto, in TKey> : IApplicationService
    {
        /// <summary>
        /// 根据Id获取数据
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        Task<TDetail> GetByIdAsync(TKey id);

        /// <summary>
        /// 获取全部
        /// </summary>
        /// <returns></returns>
        Task<List<TListDto>> GetListAsync();

        /// <summary>
        /// 根据url参数查询
        /// </summary>
        /// <param name="urlParams"></param>
        /// <returns></returns>
        //Task<PageData<TListDto>> GetByUrl(List<UrlParams> input = null);
    }

}

