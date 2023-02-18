using Yi.Framework.Template;
using Yi.Framework.Template.Provider.Server;
using Yi.Framework.Template.Provider.Site;

TemplateFactory templateFactory = new();

//选择需要生成的模板提供者

string modelName = "ERP";
List<string> entityNames =new (){ "_" };

foreach (var entityName in entityNames)
{
    templateFactory.CreateTemplateProviders((option) =>
    {
        option.Add(new ServceTemplateProvider(modelName, entityName));
        option.Add(new IServceTemplateProvider(modelName, entityName));
        option.Add(new CreateUpdateInputTemplateProvider(modelName, entityName));
        option.Add(new GetListOutputTemplateProvider(modelName, entityName));
        option.Add(new GetListInputTemplateProvider(modelName, entityName));
        option.Add(new ConstTemplateProvider(modelName, entityName));
        option.Add(new ProfileTemplateProvider(modelName, entityName));
        option.Add(new ControllerTemplateProvider(modelName, entityName));
        option.Add(new ApiTemplateProvider(modelName, entityName));
    });
    //开始构建模板
    templateFactory.BuildTemplate();
    Console.WriteLine($"Yi.Framework.Template:{entityName}构建完成！");
}

Console.WriteLine("Yi.Framework.Template:模板全部生成完成！");
Console.ReadKey();