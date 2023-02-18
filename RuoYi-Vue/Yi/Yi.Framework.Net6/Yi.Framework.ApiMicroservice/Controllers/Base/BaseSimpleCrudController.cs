using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Localization;
using Yi.Framework.Common.Helper;
using Yi.Framework.Common.Models;
using Yi.Framework.Interface.Base;
using Yi.Framework.Language;
using Yi.Framework.Repository;
using Yi.Framework.WebCore.AttributeExtend;

namespace Yi.Framework.ApiMicroservice.Controllers
{
    /// <summary>
    /// Json To Sql 类比模式，通用模型
    /// </summary>
    /// <typeparam name="T"></typeparam>
    [ApiController]
    public class BaseSimpleCrudController<T> : BaseExcelController<T> where T : class, new()
    {
        protected readonly ILogger<T> _logger;
        protected IBaseService<T> _baseService;
        public BaseSimpleCrudController(ILogger<T> logger, IBaseService<T> iBaseService):base(iBaseService._repository)
        {
            _logger = logger;
            _baseService = iBaseService;
        }

        /// <summary>
        /// 主键查询
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        [Route("{id}")]
        [HttpGet]
        public virtual async Task<Result> GetById([FromRoute]long id)
        {
            return Result.Success().SetData(await _repository.GetByIdAsync(id));
        }

        /// <summary>
        /// 全部列表查询
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public virtual async Task<Result> GetList()
        {
            return Result.Success().SetData(await _repository.GetListAsync());
        }

        /// <summary>
        /// 添加
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        [HttpPost]
        public  virtual async Task<Result> Add(T entity)
        {
            return Result.Success().SetData(await _repository.InsertReturnSnowflakeIdAsync(entity));
        }
        

        /// <summary>
        /// 修改
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        [HttpPut]
        public virtual async Task<Result> Update(T entity)
        {
            return Result.Success().SetStatus(await _repository.UpdateIgnoreNullAsync(entity));
        }

        /// <summary>
        /// 列表删除
        /// </summary>
        /// <param name="ids"></param>
        /// <returns></returns>
        [HttpDelete]
        public virtual async Task<Result> DelList(List<long> ids)
        {
            return Result.Success().SetStatus(await _repository.DeleteByIdsAsync(ids.ToDynamicArray()));
        }
    }
}
