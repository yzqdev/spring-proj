using Microsoft.AspNetCore.Mvc;
using Yi.Framework.Common.Models;
using Yi.Framework.DtoModel.ERP.Material;
using Yi.Framework.Interface.ERP;

namespace Yi.Framework.ApiMicroservice.Controllers.ERP
{
    [ApiController]
    [Route("api/[controller]/[action]")]
    public class MaterialController : ControllerBase
    {
        private readonly ILogger<MaterialController> _logger;
        private readonly IMaterialService _materialService;
        public MaterialController(ILogger<MaterialController> logger, IMaterialService materialService)
        {
            _logger = logger;
            _materialService = materialService;
        }

        /// <summary>
        /// 分页查
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public async Task<Result> PageList([FromQuery] MaterialCreateUpdateInput input, [FromQuery] PageParModel page)
        {
            var result = await _materialService.PageListAsync(input, page);
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
            var result = await _materialService.GetByIdAsync(id);
            return Result.Success().SetData(result);
        }

        /// <summary>
        /// 增
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        [HttpPost]
        public async Task<Result> Create(MaterialCreateUpdateInput input)
        {
            var result = await _materialService.CreateAsync(input);
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
        public async Task<Result> Update(long id, MaterialCreateUpdateInput input)
        {
            var result = await _materialService.UpdateAsync(id, input);
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
            await _materialService.DeleteAsync(ids);
            return Result.Success();
        }
    }
}
