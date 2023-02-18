using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Template.Abstract;
using Yi.Framework.Template.Const;

namespace Yi.Framework.Template.Provider.Server
{
    public class ControllerTemplateProvider : ProgramTemplateProvider
    {
        public ControllerTemplateProvider(string modelName, string entityName) : base(modelName, entityName)
        {
            BuildPath = $@"..\..\..\..\Yi.Framework.ApiMicroservice\Controllers\{TemplateConst.ModelName}\{TemplateConst.EntityName}Controller.cs";
            TemplatePath = $@"..\..\..\Template\Server\ControllerTemplate.txt";
        }
    }
}
