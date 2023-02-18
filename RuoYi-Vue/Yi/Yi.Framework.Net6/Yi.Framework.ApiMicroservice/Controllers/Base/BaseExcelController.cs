using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Localization;
using Yi.Framework.Common.Const;
using Yi.Framework.Common.Enum;
using Yi.Framework.Common.Helper;
using Yi.Framework.Common.Models;
using Yi.Framework.Interface;
using Yi.Framework.Language;
using Yi.Framework.Repository;
using Yi.Framework.WebCore.AttributeExtend;

namespace Yi.Framework.ApiMicroservice.Controllers
{
    [ApiController]
    public class BaseExcelController<T> : ControllerBase where T : class, new()
    {
        protected IRepository<T> _repository;
        public BaseExcelController(IRepository<T> repository)
        {
            _repository = repository;
        }
        /// <summary>
        /// 下载模板
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        [AllowAnonymous]
        public IActionResult Template()
        {
            List<T> users = new();
            var fileName = typeof(T).Name + PathConst.DataTemplate;
            var path = ExcelHelper.DownloadImportTemplate(users, fileName, Path.Combine(PathConst.wwwroot, PathEnum.Excel.ToString()));
            var file = System.IO.File.OpenRead(path);
            return File(file, "text/plain", $"{DateTime.Now.ToString("yyyyMMddHHmmssffff") + fileName }.xlsx");
        }


        /// <summary>
        /// 导出数据
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        [AllowAnonymous]
        public async Task<IActionResult> Export()
        {
            var users = await _repository.GetListAsync();
            var fileName = DateTime.Now.ToString("yyyyMMddHHmmssffff") + nameof(T) + PathConst.DataExport;
            var path = ExcelHelper.ExportExcel(users, fileName, Path.Combine(PathConst.wwwroot, PathEnum.Temp.ToString()));
            var file = System.IO.File.OpenRead(path);
            return File(file, "text/plain", $"{ fileName }.xlsx");
        }


        /// <summary>
        /// 导入数据
        /// </summary>
        /// <returns></returns>
        [HttpPost]
        [AllowAnonymous]
        public async Task<Result> Import([FromForm(Name = "file")]IFormFile formFile)
        {
            List<T> datas = ExcelHelper.ImportData<T>(formFile.OpenReadStream());

            //全量删除在重新插入
            var res = await _repository.UseTranAsync(async () =>
            {
                await _repository.DeleteAsync(u => true);
                await _repository.InsertRangeAsync(datas);
            });
            return Result.Success().SetStatus(res);
        }
    }
}
