
namespace Yi.Framework.OcelotGateway.Builder
{
    public class TokenBuilder : AbstractBuilder
    {
        public override void Invoke(DataContext data)
        {
            //先鉴权
            //var userRoleMenuEntity = data!.Context.GetCurrentUserInfo();




            ////鉴权失败表示没有带token
            //if (userRoleMenuEntity.IsNull())
            //{
            //    //访问的路径是刷新令牌的，失败了直接返回令牌刷新失败
            //    if (data.Path == data.RefreshPath)
            //    {
            //        data.Result = Result.Expire(ResultCode.RefreshTokenExpire);
            //    }

            //}
            //else//鉴权成功，访问含有token
            //{
            //    //将数据存入上下文对象中
            //    data.UserRoleMenuEntity = userRoleMenuEntity;
            //    if (userRoleMenuEntity.RefreshToken == "true")
            //    {
            //        data.IsRe = true;
            //    }
            //    data.Context!.Request.Headers.Add("Account", userRoleMenuEntity.user.Account);
            //    data.Context!.Request.Headers.Add("Id", userRoleMenuEntity.user.Id.ToString());
            //    data.Context!.Request.Headers.Add("Name", userRoleMenuEntity.user.Name);
            //    data.Context!.Request.Headers.Add("TenantId", userRoleMenuEntity.user.TenantId.ToString());
            //    data.Context!.Request.Headers.Add("TenantLevel", userRoleMenuEntity.tenant.TenantLevel.ToString());
                base.Next(data);

            //}
        }
    }
}
