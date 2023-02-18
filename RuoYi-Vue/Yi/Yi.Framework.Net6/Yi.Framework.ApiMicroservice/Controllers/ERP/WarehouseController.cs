using Microsoft.AspNetCore.Mvc;
using Yi.Framework.Common.Models;
using Yi.Framework.DtoModel.ERP.Warehouse;
using Yi.Framework.Interface.ERP;

namespace Yi.Framework.ApiMicroservice.Controllers.ERP
{
    [ApiController]
    [Route("api/[controller]/[action]")]
    public class WarehouseController : ControllerBase
    {
        private readonly ILogger<WarehouseController> _logger;
        private readonly IWarehouseService _warehouseService;
        public WarehouseController(ILogger<WarehouseController> logger, IWarehouseService warehouseService)
        {
            _logger = logger;
            _warehouseService = warehouseService;
        }

        /// <summary>
        /// 分页查
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public async Task<Result> PageList([FromQuery] WarehouseCreateUpdateInput input, [FromQuery] PageParModel page)
        {
            var result = await _warehouseService.PageListAsync(input, page);
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
            var result = await _warehouseService.GetByIdAsync(id);
            return Result.Success().SetData(result);
        }

        /// <summary>
        /// 增
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        [HttpPost]
        public async Task<Result> Create(WarehouseCreateUpdateInput input)
        {
            var result = await _warehouseService.CreateAsync(input);
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
        public async Task<Result> Update(long id, WarehouseCreateUpdateInput input)
        {
            var result = await _warehouseService.UpdateAsync(id, input);
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
            await _warehouseService.DeleteAsync(ids);
            return Result.Success();
        }
    }
}
