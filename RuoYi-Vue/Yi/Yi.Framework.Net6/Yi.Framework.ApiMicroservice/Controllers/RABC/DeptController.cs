using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Yi.Framework.Common.Models;
using Yi.Framework.Interface;
using Yi.Framework.Interface.RABC;
using Yi.Framework.Model.RABC.Entitys;
using Yi.Framework.Repository;
using Yi.Framework.WebCore;
using Yi.Framework.WebCore.AttributeExtend;
using Yi.Framework.WebCore.AuthorizationPolicy;

namespace Yi.Framework.ApiMicroservice.Controllers
{
    [ApiController]
    [Route("api/[controller]/[action]")]
    public class DeptController : BaseSimpleCrudController<DeptEntity>
    {
        private IDeptService _iDeptService;
        public DeptController(ILogger<DeptEntity> logger, IDeptService iDeptService) : base(logger, iDeptService)
        {
            _iDeptService = iDeptService;
        }

        /// <summary>
        /// 动态条件查询
        /// </summary>
        /// <param name="dept"></param>
        /// <returns></returns>
        [HttpGet]
        public async Task<Result> SelctGetList([FromQuery] DeptEntity dept)
        {
            return Result.Success().SetData(await _iDeptService.SelctGetList(dept));
        }


        /// <summary>
        /// 添加
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public override async Task<Result> Add(DeptEntity entity)
        {
            return await base.Add(entity);
        }

        /// <summary>
        /// 更新
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public override async Task<Result> Update(DeptEntity entity)
        {
            return await base.Update(entity);
        }

        /// <summary>
        /// 根据角色id获取该角色下全部部门
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        [HttpGet]
        [Route("{id}")]
        public async Task<Result> GetListByRoleId(long id)
        {
            return Result.Success().SetData(await _iDeptService.GetListByRoleId(id));
        }
    }
}
