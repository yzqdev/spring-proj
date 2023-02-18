
using Yi.Framework.Common.Models;

namespace Yi.Framework.OcelotGateway.Builder
{
    public class WhiteListBuilder : AbstractBuilder
    {
        public override void Invoke(DataContext data)
        {
            //如果在白名单，直接通行
            if (data!.WhitePathList!.Contains(data.Path!))
            {
          
              data.Result = Result.Success();
            }
            //访问的是swagger
            else if (data.Path!.Split("/")[1].ToUpper() == "swagger".ToUpper())
            {
              data.Result = Result.Success();
            }
            else//否则进入下一个管道处理
            {

                base.Next(data);
            }

        }
    }
}
