using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Template.Const;

namespace Yi.Framework.Template.Abstract
{
    public abstract class ModelTemplateProvider : ProgramTemplateProvider
    {

        public ModelTemplateProvider(string modelName, string entityName) : base(modelName, entityName)
        {
            AddIgnoreEntityField("Id", "TenantId");
        }

        private string entityPath=string.Empty;

        /// <summary>
        /// 实体路径，该类生成需要实体与模板两个同时构建成
        /// </summary>
        public string EntityPath
        {
            get => this.entityPath;
            set
            {
                value = value!.Replace(TemplateConst.EntityName, EntityName);
                value = value.Replace(TemplateConst.ModelName, ModelName);
                this.entityPath = value;
            }
        }


        /// <summary>
        /// 生成模板忽略实体字段
        /// </summary>
        private List<string> IgnoreEntityFields { get; set; } = new();

        public override void Build()
        {
            if (BuildPath is null)
            {
                throw new ArgumentNullException(nameof(BuildPath));
            }
            //模板信息
            var templateData = GetTemplateData();

            //实体信息
            var enetityDatas = GetEntityData().ToList();

            //获取全部属性字段
            for (var i = enetityDatas.Count() - 1; i >= 0; i--)
            {
                //不是字段属性直接删除跳过   
                if (!enetityDatas[i].Contains("{ get; set; }"))
                {
                    enetityDatas.RemoveAt(i);
                    continue;
                }
                //是字段属性，同时还包含忽略字段
                foreach (var IgnoreEntityField in IgnoreEntityFields)
                {
                    if (enetityDatas[i].Contains(IgnoreEntityField))
                    {
                        enetityDatas.RemoveAt(i);
                        continue;
                    }
                }
                //以}结尾，不包含get不是属性，代表类结尾
                if (enetityDatas[i].EndsWith("}") && !enetityDatas[i].Contains("get"))
                {
                    break;
                }
            }

            //拼接实体字段
            var entityFieldsbuild = string.Join("\r\n", enetityDatas);


            //模板替换属性字段
            templateData = templateData.Replace(TemplateConst.EntityField, entityFieldsbuild);

            templateData = base.ReplaceTemplateDic(templateData);

            if (!Directory.Exists(Path.GetDirectoryName(BuildPath)))
            {
                Directory.CreateDirectory(Path.GetDirectoryName(BuildPath)!);
            }
            File.WriteAllText(BuildPath, templateData);
        }

        /// <summary>
        /// 获取实体信息
        /// </summary>
        /// <returns></returns>
        /// <exception cref="ArgumentNullException"></exception>
        public virtual string[] GetEntityData()
        {
            if (TemplatePath is null)
            {
                throw new ArgumentNullException(nameof(entityPath));
            }
            if (!File.Exists(entityPath))
            {
                throw new FileNotFoundException($"请检查路径：{entityPath}\r\n未包含实体：{EntityName}");
            }

            return File.ReadAllLines(entityPath);
        }

        /// <summary>
        /// 添加忽略实体字段
        /// </summary>
        /// <param name="field"></param>
        public void AddIgnoreEntityField(params string[] field)
        {
            IgnoreEntityFields.AddRange(field);
        }
    }
}
