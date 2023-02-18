
using Yi.Framework.Common.Models;

namespace Yi.Framework.OcelotGateway.Builder
{
    public class AccoutBuilder : AbstractBuilder
    {

        public override void Invoke(DataContext data)
        {
            //直接放行，并需要鉴权
            if (data!.AccountPathList!.Contains(data.Path!))
            {
                data.Result = Result.Success();
            }
            else//剩下的这个，就是最后真正的业务判断
            {
               base.Next(data);
            }
        }
    }
}
