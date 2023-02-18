using Microsoft.AspNetCore.Mvc;
using Yi.Framework.Common.Models;
using Yi.Framework.DtoModel.ERP.PurchaseDetails;
using Yi.Framework.Interface.ERP;

namespace Yi.Framework.ApiMicroservice.Controllers.ERP
{
    [ApiController]
    [Route("api/[controller]/[action]")]
    public class PurchaseDetailsController : ControllerBase
    {
        private readonly ILogger<PurchaseDetailsController> _logger;
        private readonly IPurchaseDetailsService _purchaseDetailsService;
        public PurchaseDetailsController(ILogger<PurchaseDetailsController> logger, IPurchaseDetailsService purchaseDetailsService)
        {
            _logger = logger;
            _purchaseDetailsService = purchaseDetailsService;
        }

        /// <summary>
        /// 分页查
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public async Task<Result> PageList([FromQuery] PurchaseDetailsCreateUpdateInput input, [FromQuery] PageParModel page)
        {
            var result = await _purchaseDetailsService.PageListAsync(input, page);
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
            var result = await _purchaseDetailsService.GetByIdAsync(id);
            return Result.Success().SetData(result);
        }

        /// <summary>
        /// 增
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        [HttpPost]
        public async Task<Result> Create(PurchaseDetailsCreateUpdateInput input)
        {
            var result = await _purchaseDetailsService.CreateAsync(input);
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
        public async Task<Result> Update(long id, PurchaseDetailsCreateUpdateInput input)
        {
            var result = await _purchaseDetailsService.UpdateAsync(id, input);
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
            await _purchaseDetailsService.DeleteAsync(ids);
            return Result.Success();
        }
    }
}
