using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Template.Abstract;
using Yi.Framework.Template.Const;

namespace Yi.Framework.Template.Provider.Server
{
    public class GetListOutputTemplateProvider : ModelTemplateProvider
    {
        public GetListOutputTemplateProvider(string modelName, string entityName) : base(modelName, entityName)
        {
            BuildPath = $@"..\..\..\..\Yi.Framework.DtoModel\{TemplateConst.ModelName}\{TemplateConst.EntityName}\{TemplateConst.EntityName}GetListOutput.cs";
            TemplatePath = $@"..\..\..\Template\Server\GetListOutputTemplate.txt";
            EntityPath = $@"..\..\..\..\Yi.Framework.Model\{TemplateConst.ModelName}\Entitys\{TemplateConst.EntityName}Entity.cs";
        }
    }
}
