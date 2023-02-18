
using Yi.Framework.Common.Models;

namespace Yi.Framework.OcelotGateway.Builder
{
    public class RefreshBuilder : AbstractBuilder
    {
        public override void Invoke(DataContext data)
        {
            //如果是刷新令牌
            if ((bool)data!.IsRe!)
            {
                //且访问路径还是正确的
                if (data.Path == data.RefreshPath)
                {
                    data.Result = Result.Success();
                }
            }
            else//表示不是刷新的token，就要去redis里面判断了
            {

               base.Next(data);
            }
        }
    }
}
