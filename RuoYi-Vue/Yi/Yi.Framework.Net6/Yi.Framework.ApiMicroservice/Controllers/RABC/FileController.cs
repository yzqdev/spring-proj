using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Hosting;
using SqlSugar;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using Yi.Framework.Common.Const;
using Yi.Framework.Common.Enum;
using Yi.Framework.Common.Helper;
using Yi.Framework.Common.Models;
using Yi.Framework.Core;
using Yi.Framework.Interface.RABC;
using Yi.Framework.Model.RABC.Entitys;
using Yi.Framework.WebCore;

namespace Yi.Framework.ApiMicroservice.Controllers
{
    /// <summary>
    /// 文件
    /// </summary>
    [Route("api/[controller]/[action]")]
    [ApiController]
    public class FileController : ControllerBase
    {
        private IFileService _iFileService;
        private readonly IHostEnvironment _env;
        private ThumbnailSharpInvoer _thumbnailSharpInvoer;

        /// <summary>
        /// 文件上传下载
        /// </summary>
        /// <param name="iFileService"></param>
        /// <param name="env"></param>
        /// <param name="thumbnailSharpInvoer"></param>
        public FileController(IFileService iFileService, IHostEnvironment env, ThumbnailSharpInvoer thumbnailSharpInvoer)
        {
            _iFileService = iFileService;
            _env = env;
            _thumbnailSharpInvoer = thumbnailSharpInvoer;
        }

        /// <summary>
        /// 文件下载,只需用文件code即可,可选择是否为缩略图
        /// </summary>
        /// <param name="code"></param>
        /// <param name="isThumbnail"></param>
        /// <returns></returns>
        [Route("/api/file/{code}/{isThumbnail?}")]
        [HttpGet]
        public async Task<IActionResult> Get(long code, bool? isThumbnail)
        {
            var file = await _iFileService._repository.GetByIdAsync(code);
            if (file is null)
            {
                return new NotFoundResult();
            }
            try
            {
                //如果为缩略图
                if (isThumbnail is true)
                {
                    file.FilePath = PathEnum.Thumbnail.ToString();
                }
                //路径为： 文件路径/文件id+文件扩展名
                var path = Path.Combine($"{PathConst.wwwroot}/{file.FilePath}", file.Id.ToString() + Path.GetExtension(file.FileName));
                var stream = System.IO.File.OpenRead(path);
                var MimeType = Common.Helper.MimeHelper.GetMimeMapping(file.FileName!);
                return File(stream, MimeType, file.FileName);
            }
            catch
            {
                return new NotFoundResult();
            }
        }

        /// <summary>
        /// 多文件上传,type可空，默认上传至File文件夹下，swagger返回雪花id精度是有问题的，同时如果时图片类型，还需要进行缩略图制作
        /// </summary>
        /// <param name="type">文件类型，可空</param>
        /// <param name="file">多文件表单</param>
        /// <param name="remark">描述</param>
        /// <returns></returns>
        [Route("/api/file/Upload/{type?}")]
        [HttpPost]
        public async Task<Result> Upload([FromRoute] string? type, [FromForm] IFormFileCollection file, [FromQuery] string? remark)
        {
            type = type ?? PathEnum.File.ToString();

            type = CultureInfo.CurrentCulture.TextInfo.ToTitleCase(type.ToLower());
            if (!Enum.IsDefined(typeof(PathEnum), type))
            {
                //后续类型可从字典表中获取
                return Result.Error("上传失败！文件类型不支持！");
            }


            if (file.Count() == 0)
            {
                return Result.Error("未选择文件");
            }
            //批量插入
            List<FileEntity> datas = new();

            //返回的codes
            List<long> codes = new();
            try
            {
                foreach (var f in file)
                {
                    FileEntity data = new();
                    data.Id = SnowFlakeSingle.Instance.NextId();
                    data.FileSize = ((decimal)f.Length) / 1024;
                    data.FileName = f.FileName;
                    data.FileType = Common.Helper.MimeHelper.GetMimeMapping(f.FileName);
                    data.FilePath = type;
                    data.Remark = remark;
                    data.IsDeleted = false;

                    //落盘文件，文件名为雪花id+自己的扩展名
                    string filename = data.Id.ToString() + Path.GetExtension(f.FileName);
                    string typePath = $"{PathConst.wwwroot}/{type}";
                    if (!Directory.Exists(typePath))
                    {
                        Directory.CreateDirectory(typePath);
                    }

                    //生成文件
                    using (var stream = new FileStream(Path.Combine(typePath, filename), FileMode.CreateNew, FileAccess.ReadWrite))
                    {
                        await f.CopyToAsync(stream);

                        //如果是图片类型，还需要生成缩略图,当然，如果图片很小，直接复制过去即可
                        if (PathEnum.Image.ToString().Equals(type))
                        {
                            string thumbnailPath = $"{PathConst.wwwroot}/{PathEnum.Thumbnail}";
                            if (!Directory.Exists(thumbnailPath))
                            {
                                Directory.CreateDirectory(thumbnailPath);
                            }
                            //保存至缩略图路径
                            byte[] result=null!;
                            try
                            {
                                result = _thumbnailSharpInvoer.CreateThumbnailBytes(thumbnailSize: 300,
                            imageStream: stream,
                            imageFormat: Format.Jpeg);
                            }
                            catch
                            {
                                result = new byte[stream.Length];
                                stream.Read(result, 0, result.Length);
                                // 设置当前流的位置为流的开始
                                stream.Seek(0, SeekOrigin.Begin);
                            }
                            finally
                            {
                     
                                await System.IO.File.WriteAllBytesAsync(Path.Combine(thumbnailPath, filename), result);
                            }
                        }


                    };

                    //将文件信息添加到数据库
                    datas.Add(data);
                    codes.Add(data.Id);
                }
                return Result.Success().SetData(codes).SetStatus(await _iFileService._repository.InsertRangeAsync(datas));
            }
            catch
            {
                return Result.Error();
            }
        }

        /// <summary>
        /// 一键同步图片到缩略图
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public async Task<Result> ThumbnailSync()
        {
            string typePath = $"{PathConst.wwwroot}/{PathEnum.Image}";
            string thumbnailPath = $"{PathConst.wwwroot}/{PathEnum.Thumbnail}";
            List<string> fileNames = FileHelper.GetAllFileNames(typePath);
            foreach (var filename in fileNames)
            {
                if (System.IO.File.Exists(Path.Combine(thumbnailPath, filename)))
                {
                    //如果缩略图存在，直接跳过
                    continue;
                }
                if (!Directory.Exists(typePath))
                {
                    Directory.CreateDirectory(typePath);
                }


                using (var stream = new FileStream(Path.Combine(typePath, filename), FileMode.Open, FileAccess.ReadWrite))
                {
                    byte[] result=null!;
                    try
                    {

                        //保存至缩略图路径
                        result = _thumbnailSharpInvoer.CreateThumbnailBytes(thumbnailSize: 300,
                          imageStream: stream,
                          imageFormat: Format.Jpeg);

                        if (!Directory.Exists(thumbnailPath))
                        {
                            Directory.CreateDirectory(thumbnailPath);
                        }


                    }
                    catch
                    {
                        result = new byte[stream.Length];
                        stream.Read(result, 0, result.Length);
                        // 设置当前流的位置为流的开始
                        stream.Seek(0, SeekOrigin.Begin);

                        ////如果当前文件同步失败，就跳转到下一个
                    }
                    finally {

                        await System.IO.File.WriteAllBytesAsync(Path.Combine(thumbnailPath, filename), result);
                    }
                }
               
            }
            return Result.Success();
        }
    }
}
