using Microsoft.AspNetCore.Mvc;
using Yi.Framework.Common.Models;
using Yi.Framework.DtoModel.ERP.Supplier;
using Yi.Framework.Interface.ERP;

namespace Yi.Framework.ApiMicroservice.Controllers.ERP
{
    [ApiController]
    [Route("api/[controller]/[action]")]
    public class SupplierController : ControllerBase
    {
        private readonly ILogger<SupplierController> _logger;
        private readonly ISupplierService _supplierService;
        public SupplierController(ILogger<SupplierController> logger, ISupplierService supplierService)
        {
            _logger = logger;
            _supplierService = supplierService;
        }

        /// <summary>
        /// 分页查
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public async Task<Result> PageList([FromQuery] SupplierCreateUpdateInput input, [FromQuery] PageParModel page)
        {
            var result = await _supplierService.PageListAsync(input, page);
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
            var result = await _supplierService.GetByIdAsync(id);
            return Result.Success().SetData(result);
        }

        /// <summary>
        /// 增
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        [HttpPost]
        public async Task<Result> Create(SupplierCreateUpdateInput input)
        {
            var result = await _supplierService.CreateAsync(input);
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
        public async Task<Result> Update(long id, SupplierCreateUpdateInput input)
        {
            var result = await _supplierService.UpdateAsync(id, input);
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
            await _supplierService.DeleteAsync(ids);
            return Result.Success();
        }
    }
}
