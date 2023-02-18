
using Yi.Framework.Common.Models;

namespace Yi.Framework.OcelotGateway.Builder
{
    public class TenantBuilder : AbstractBuilder
    {
        public override void Invoke(DataContext data)
        {
           
            if (data!.TenantPathList!.Contains(""/*data.UserRoleMenuEntity!.tenant.TenantName*/))
            {
                data.Result = Result.Success();
            }
            else
            {
                base.Next(data);
            }
           
        }
    }
}
