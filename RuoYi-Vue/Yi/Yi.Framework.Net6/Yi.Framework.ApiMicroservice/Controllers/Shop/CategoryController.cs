using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Yi.Framework.Common.Models;
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
    public class CategoryController : BaseCrudController<CategoryEntity>
    {
        private ICategoryService _iCategoryService;
        public CategoryController(ILogger<CategoryEntity> logger, ICategoryService iCategoryService) : base(logger, iCategoryService)
        {
            _iCategoryService = iCategoryService;
        }
    }
}
