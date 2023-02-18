using Microsoft.AspNetCore.Mvc;
using Yi.Framework.Common.Models;
using Yi.Framework.DtoModel.ERP.Unit;
using Yi.Framework.Interface.ERP;

namespace Yi.Framework.ApiMicroservice.Controllers.ERP
{
    [ApiController]
    [Route("api/[controller]/[action]")]
    public class UnitController : ControllerBase
    {
        private readonly ILogger<UnitController> _logger;
        private readonly IUnitService _unitService;
        public UnitController(ILogger<UnitController> logger, IUnitService unitService)
        {
            _logger = logger;
            _unitService = unitService;
        }

        /// <summary>
        /// 分页查
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public async Task<Result> PageList([FromQuery] UnitCreateUpdateInput input, [FromQuery] PageParModel page)
        {
            var result = await _unitService.PageListAsync(input, page);
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
            var result = await _unitService.GetByIdAsync(id);
            return Result.Success().SetData(result);
        }

        /// <summary>
        /// 全查
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public async Task<Result> GetList()
        {
            var result = await _unitService.GetListAsync();
            return Result.Success().SetData(result);
        }

        /// <summary>
        /// 增
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        [HttpPost]
        public async Task<Result> Create(UnitCreateUpdateInput input)
        {
            var result = await _unitService.CreateAsync(input);
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
        public async Task<Result> Update(long id, UnitCreateUpdateInput input)
        {
            var result = await _unitService.UpdateAsync(id, input);
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
            await _unitService.DeleteAsync(ids);
            return Result.Success();
        }
    }
}
