using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Template.Abstract;
using Yi.Framework.Template.Const;

namespace Yi.Framework.Template.Provider.Server
{
    public class GetListInputTemplateProvider : ModelTemplateProvider
    {
        public GetListInputTemplateProvider(string modelName, string entityName) : base(modelName, entityName)
        {
            BuildPath = $@"..\..\..\..\Yi.Framework.DtoModel\{TemplateConst.ModelName}\{TemplateConst.EntityName}\{TemplateConst.EntityName}GetListInput.cs";
            TemplatePath = $@"..\..\..\Template\Server\GetListInputTemplate.txt";
            EntityPath = $@"..\..\..\..\Yi.Framework.Model\{TemplateConst.ModelName}\Entitys\{TemplateConst.EntityName}Entity.cs";
        }
    }
}
