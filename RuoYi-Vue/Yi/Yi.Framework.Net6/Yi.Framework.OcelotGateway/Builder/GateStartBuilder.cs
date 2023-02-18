using System.Linq;
using System.Text;

namespace Yi.Framework.OcelotGateway.Builder
{
    public static class GateStartBuilder
    {
        public static void Run(DataContext dataContext)
        {
            Handler(dataContext);
            //基础
            AbstractBuilder whitelistBuilder = new WhiteListBuilder();
            AbstractBuilder tokenBuilder = new TokenBuilder();
            AbstractBuilder refreshBuilder = new RefreshBuilder();
            AbstractBuilder accoutBuilder = new AccoutBuilder();

            //额外
            AbstractBuilder tenantBuilder = new TenantBuilder();
            AbstractBuilder userWhitelist = new UserWhitelistBuilder();

            //最终
            AbstractBuilder menuBuilder = new MenuBuilder();
      

            whitelistBuilder.SetNext(tokenBuilder);
            tokenBuilder.SetNext(refreshBuilder);
            refreshBuilder.SetNext(accoutBuilder);
            accoutBuilder.SetNext(tenantBuilder);
            tenantBuilder.SetNext(userWhitelist);
            userWhitelist.SetNext(menuBuilder);
            whitelistBuilder.Invoke(dataContext);
        }

        public static void Handler(DataContext dataContext)
        {
            dataContext.Path = dataContext.Path!.ToUpper();
            dataContext.RefreshPath = dataContext.RefreshPath!.ToUpper();
            dataContext.WhitePathList = dataContext.WhitePathList!.Select(white => white.ToUpper()).ToList();
            dataContext.AccountPathList = dataContext.AccountPathList!.Select(white => white.ToUpper()).ToList();
            dataContext.TenantPathList = dataContext.TenantPathList!.Select(white => white.ToUpper()).ToList();
        }
    }
}
