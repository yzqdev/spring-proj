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
    public class SkuController : BaseSimpleCrudController<SkuEntity>
    {
        private ISkuService _iSkuService;
        public SkuController(ILogger<SkuEntity> logger, ISkuService iSkuService) : base(logger, iSkuService)
        {
            _iSkuService = iSkuService;
        }

        /// <summary>
        /// 动态条件分页查询
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public async Task<Result> PageList([FromQuery] SkuEntity eneity, [FromQuery] PageParModel page)
        {
            return Result.Success().SetData(await _iSkuService.SelctPageList(eneity, page));
        }




        //数据测试
        [HttpGet]
        public async Task<Result> Test(OperEnum operEnum)
        {
            switch (operEnum)
            {
                case OperEnum.Insert:
                    List<SkuEntity> spus = new();
                    var sku1 = new SkuEntity()
                    {
                        Id = 1,
                        Stock = 100,
                        IsDeleted = false,
                        SpuId = 1,
                        Price = 1000,
                        SpecsSkuAllInfo = new List<SpecsSkuAllInfoModel> {
                  new SpecsSkuAllInfoModel { SpecsGroupName="内存",SpecsName="1GB" } ,
              new SpecsSkuAllInfoModel { SpecsGroupName="颜色",SpecsName= "红" } },
                    };
                    var sku2 = new SkuEntity()
                    {
                        Id = 2,
                        Stock = 100,
                        IsDeleted = false,
                        SpuId = 1,
                        Price = 4000,
                        SpecsSkuAllInfo = new List<SpecsSkuAllInfoModel> {
                  new SpecsSkuAllInfoModel { SpecsGroupName="内存",SpecsName="2GB" } ,
              new SpecsSkuAllInfoModel { SpecsGroupName="颜色",SpecsName= "绿" } },
                    };


                    var sku3 = new SkuEntity()
                    {
                        Id = 3,
                        Stock = 100,
                        IsDeleted = false,
                        SpuId = 2,
                        Price = 2000,
                        SpecsSkuAllInfo = new List<SpecsSkuAllInfoModel> {
                  new SpecsSkuAllInfoModel { SpecsGroupName="内存",SpecsName="3GB" } ,
              new SpecsSkuAllInfoModel { SpecsGroupName="颜色",SpecsName= "红" } },
                    };
                    var sku4 = new SkuEntity()
                    {
                        Id = 4,
                        Stock = 100,
                        IsDeleted = false,
                        SpuId = 2,
                        Price = 1000,
                        SpecsSkuAllInfo = new List<SpecsSkuAllInfoModel> {
                  new SpecsSkuAllInfoModel { SpecsGroupName="内存",SpecsName="2GB" } ,
              new SpecsSkuAllInfoModel { SpecsGroupName="颜色",SpecsName= "蓝" } },
                    }; 
                    spus.Add(sku1);
                    spus.Add(sku2);
                    spus.Add(sku3);
                    spus.Add(sku4);
                    await _iSkuService._repository.InsertRangeAsync(spus);
                    break;

                case OperEnum.Delete:
                    await _iSkuService._repository.DeleteAsync((u)=>true);
                    break;
                default:
                    break;
            }


            return Result.Success();
        }
    }
}
