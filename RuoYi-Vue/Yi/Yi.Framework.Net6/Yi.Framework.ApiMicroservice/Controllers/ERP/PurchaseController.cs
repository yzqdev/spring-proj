using Microsoft.AspNetCore.Mvc;
using Yi.Framework.Common.Abstract;
using Yi.Framework.Common.Attribute;
using Yi.Framework.Common.Models;
using Yi.Framework.DtoModel.ERP.Purchase;
using Yi.Framework.Interface.ERP;

namespace Yi.Framework.ApiMicroservice.Controllers.ERP
{
    [ApiController]
    [Route("api/[controller]/[action]")]
    public class PurchaseController : ControllerBase
    {
        private readonly ILogger<PurchaseController> _logger;
        private readonly IPurchaseService _purchaseService;
        public PurchaseController(ILogger<PurchaseController> logger, IPurchaseService purchaseService)
        {
            _logger = logger;
            _purchaseService = purchaseService;
        }

        /// <summary>
        /// 分页查
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public async Task<Result> PageList([FromQuery] PurchaseGetListInput input, [FromQuery] PageParModel page)
        {
            var result = await _purchaseService.PageListAsync(input, page);
            return Result.Success().SetData(result);
        }

        /// <summary>
        /// 单查
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        [Route("{id}")]
        public async Task<Result> GetById(long id)
        {
            var result = await _purchaseService.GetByIdAsync(id);
            return Result.Success().SetData(result);
        }

        /// <summary>
        /// 增
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        [HttpPost]
        public async Task<Result> Create(PurchaseCreateInput input)
        {
            var result = await _purchaseService.CreateAsync(input);
            return Result.Success().SetData(result);
        }

        /// <summary>
        /// 更
        /// </summary>
        /// <param name="id"></param>
        /// <param name="input"></param>
        /// <returns></returns>
        [HttpPut]
        [Route("{id}")]
        public async Task<Result> Update(long id, PurchaseUpdateInput input)
        {
            var result = await _purchaseService.UpdateAsync(id, input);
            return Result.Success().SetData(result);
        }

        /// <summary>
        /// 删
        /// </summary>
        /// <param name="ids"></param>
        /// <returns></returns>
        [HttpDelete]
        public async Task<Result> Del(List<long> ids)
        {
            await _purchaseService.DeleteAsync(ids);
            return Result.Success();
        }
    }
}
