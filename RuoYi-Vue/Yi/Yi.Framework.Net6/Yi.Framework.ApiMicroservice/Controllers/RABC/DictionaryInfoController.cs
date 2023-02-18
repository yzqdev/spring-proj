using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Yi.Framework.Common.Helper;
using Yi.Framework.Common.Models;
using Yi.Framework.Interface;
using Yi.Framework.Interface.RABC;
using Yi.Framework.Model.RABC.Entitys;
using Yi.Framework.Repository;
using Yi.Framework.Service;
using Yi.Framework.WebCore;
using Yi.Framework.WebCore.AttributeExtend;
using Yi.Framework.WebCore.AuthorizationPolicy;

namespace Yi.Framework.ApiMicroservice.Controllers
{
    [ApiController]
    [Route("api/[controller]/[action]")]
    public class DictionaryInfoController:BaseSimpleCrudController<DictionaryInfoEntity>
    {
        private IDictionaryInfoService _iDictionaryInfoService;
        public DictionaryInfoController(ILogger<DictionaryInfoEntity> logger, IDictionaryInfoService iDictionaryInfoService):base(logger, iDictionaryInfoService)
        {
            _iDictionaryInfoService = iDictionaryInfoService;
        }

        /// <summary>
        /// 动态条件分页查询
        /// </summary>
        /// <param name="dicInfo"></param>
        /// <param name="page"></param>
        /// <returns></returns>
        [HttpGet]
        public async Task<Result> PageList([FromQuery] DictionaryInfoEntity dicInfo, [FromQuery] PageParModel page)
        {
            return Result.Success().SetData(await _iDictionaryInfoService.SelctPageList(dicInfo, page));
        }

        /// <summary>
        /// 根据字典类别获取字典信息
        /// </summary>
        /// <param name="type"></param>
        /// <returns></returns>
        [HttpGet]
        [Route("{type}")]
        public async Task<Result> GetListByType([FromRoute] string type)
        {
            return Result.Success().SetData(await _iDictionaryInfoService._repository.GetListAsync(u=>u.DictType==type&&u.IsDeleted==false));
        }
    }
}
