using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Template.Const;

namespace Yi.Framework.Template.Abstract
{

    public abstract class ProgramTemplateProvider : AbstractTemplateProvider
    {
        public ProgramTemplateProvider(string modelName, string entityName)
        {
            ModelName = modelName;
            EntityName = entityName;
            base.AddTemplateDic(TemplateConst.EntityName, EntityName);
            base.AddTemplateDic(TemplateConst.ModelName, ModelName);
            base.AddTemplateDic(TemplateConst.LowerEntityName, EntityName.Substring(0, 1).ToLower() + EntityName.Substring(1));
            base.AddTemplateDic(TemplateConst.LowerModelName, ModelName.ToLower());
        }
        /// <summary>
        /// 实体名称
        /// </summary>
        public string EntityName { get; set; }
        /// <summary>
        /// 模块名称
        /// </summary>
        public string ModelName { get; set; }

        /// <summary>
        /// 重写构建路径，替换实体名称与模块名称
        /// </summary>
        public override string? BuildPath
        {
            get => base.BuildPath;
            set
            {
                value = ReplaceTemplateDic(value!);

                base.BuildPath = value;
            }
        }

        public string ReplaceTemplateDic(string str)
        {
            foreach (var ky in TemplateDic)
            {
                str = str.Replace(ky.Key, ky.Value);
            }
            return str;
        }


        public override void Build()
        {
            if (BuildPath is null)
            {
                throw new ArgumentNullException(nameof(BuildPath));
            }
            var templateData = GetTemplateData();
            templateData = ReplaceTemplateDic(templateData);
            if (!Directory.Exists(Path.GetDirectoryName(BuildPath)))
            {
                Directory.CreateDirectory(Path.GetDirectoryName(BuildPath)!);
            }
            File.WriteAllText(BuildPath, templateData);
        }

        public override void Bak()
        {
            throw new NotImplementedException();
        }
    }
}
