using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Template.Abstract;
using Yi.Framework.Template.Const;

namespace Yi.Framework.Template.Provider.Server
{
    public class IServceTemplateProvider : ProgramTemplateProvider
    {
        public IServceTemplateProvider(string modelName, string entityName) : base(modelName, entityName)
        {
            BuildPath = $@"..\..\..\..\Yi.Framework.Interface\{TemplateConst.ModelName}\I{TemplateConst.EntityName}Service.cs";
            TemplatePath = $@"..\..\..\Template\Server\IServiceTemplate.txt";
        }
    }
}
