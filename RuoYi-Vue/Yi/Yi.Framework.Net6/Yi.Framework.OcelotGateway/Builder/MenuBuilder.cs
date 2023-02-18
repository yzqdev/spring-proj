using Yi.Framework.Common.Models;

namespace Yi.Framework.OcelotGateway.Builder
{
    public class MenuBuilder : AbstractBuilder
    {
        public override void Invoke(DataContext data)
        {
            //var redisData = data!.DB!.Get<UserRoleMenuEntity>(RedisConst.GetStr(RedisConst.UserRoleMenu, data.UserRoleMenuEntity!.user.Account));
            //if (redisData.IsNotNull())
            //{
            //    var menus = redisData.menus;
            //    if (menus.Where(u=> u.TypeCode == (short)MenuTypeEnum.Hide).Select(u => u.UrlControl.ToUpper()).Contains(data.Path))
            //    {

            //        data.Result = Result.Success();
            //    }
            //    else
            //    {
            //        data.Result = Result.SuccessError("当前令牌无接口权限");
            //    }
            //}
            //else
            //{
            //    data.Result = Result.UnAuthorize("用户信息已经过期");
            //}

        }
    }
}
