using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Yi.Framework.Common.Enum;
using Yi.Framework.Common.Models;
using Yi.Framework.Interface;
using Yi.Framework.Interface.SHOP;
using Yi.Framework.Model.SHOP.Entitys;
using Yi.Framework.Repository;
using Yi.Framework.WebCore;
using Yi.Framework.WebCore.AttributeExtend;
using Yi.Framework.WebCore.AuthorizationPolicy;

namespace Yi.Framework.ApiMicroservice.Controllers
{
    [ApiController]
    [Route("api/[controller]/[action]")]
    public class SpuController : BaseSimpleCrudController<SpuEntity>
    {
        private ISpuService _iSpuService;
        public SpuController(ILogger<SpuEntity> logger, ISpuService iSpuService) : base(logger, iSpuService)
        {
            _iSpuService = iSpuService;
        }

        /// <summary>
        /// 动态条件分页查询
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public async Task<Result> PageList([FromQuery] SpuEntity eneity, [FromQuery] PageParModel page)
        {
            return Result.Success().SetData(await _iSpuService.SelctPageList(eneity, page));
        }

        [Route("{id}")]
        [HttpGet]
        public override async Task<Result> GetById([FromRoute] long id)
        {
            return Result.Success().SetData(await _repository._DbQueryable.Includes(u=>u.Skus).SingleAsync(u=>u.Id.Equals(id)));
        }




        //数据测试
        [HttpGet]
        public async Task<Result> Test(OperEnum operEnum)
        {
            switch (operEnum)
            {
                case OperEnum.Insert:
                    List<SpuEntity> spus = new();
                    var spu1 = new SpuEntity()
                    {
                        Id = 1,
                        SpuName = "华为mate40 5G手机",
                        IsDeleted = false,
                        Details = "华为手机就是牛",
                        Price = "1000-2000",
                        SpecsSpuAllInfo = new List<SpecsSpuAllInfoModel> {
                  new SpecsSpuAllInfoModel { SpecsGroupName="内存",SpecsNames=new List<string> { "1GB","2GB","3GB"} } ,
              new SpecsSpuAllInfoModel { SpecsGroupName="颜色",SpecsNames=new List<string> { "红","蓝","绿"} } },
                    };
                    var spu2 = new SpuEntity()
                    {
                        Id = 2,
                        SpuName = "小米888 8G手机",
                        IsDeleted = false,
                        Details = "小米手机就是牛",
                        Price = "2000-3000",
                        SpecsSpuAllInfo = new List<SpecsSpuAllInfoModel> {
                  new SpecsSpuAllInfoModel { SpecsGroupName="内存",SpecsNames=new List<string> { "1GB","2GB","3GB"} } ,
              new SpecsSpuAllInfoModel { SpecsGroupName="颜色",SpecsNames=new List<string> { "红","蓝","绿"} } },
                    };
                    spus.Add(spu1);
                    spus.Add(spu2);
                    await _iSpuService._repository.InsertRangeAsync(spus);
                    break;

                case OperEnum.Delete:
                    await _iSpuService._repository.DeleteAsync((u) => true);
                    break;
                default:
                    break;
            }


            return Result.Success();
        }
    }
}
