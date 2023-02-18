using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.Template.Abstract
{
    public interface ITemplateProvider
    {
        /// <summary>
        /// 构建生成路径
        /// </summary>
        string? BuildPath { get; set; }

        /// <summary>
        /// 模板文件路径
        /// </summary>
        string? TemplatePath { get; set; }

        /// <summary>
        /// 备份文件路径
        /// </summary>
        string? BakPath { get; set; }


        /// <summary>
        /// 开始构建
        /// </summary>
        /// <returns></returns>
        void Build();

        /// <summary>
        /// 生成备份
        /// </summary>
        /// <returns></returns>
        void Bak();


    }
}
