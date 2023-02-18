
namespace Yi.Framework.OcelotGateway.Builder
{
    public class UserWhitelistBuilder : AbstractBuilder
    {
        public override void Invoke(DataContext data)
        {
            //if (data!.UserWhitePathList!.Contains(data.UserRoleMenuEntity!.user.Account))
            //{
            //    data.Result = Result.Success();
            //}
            //else
            {
                base.Next(data);
            }
        }
    }
}
