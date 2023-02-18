using IPTools.Core;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.SignalR;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Common.Enum;
using Yi.Framework.WebCore.CommonExtend;

namespace Yi.Framework.WebCore.SignalRHub
{
    public class MainHub : Hub
    {
        public static readonly List<OnlineUser> clientUsers = new();


        private HttpContext? _httpContext;
        private ILogger<MainHub> _logger;
        public MainHub(IHttpContextAccessor httpContextAccessor,ILogger<MainHub> logger)
        {
            _httpContext = httpContextAccessor.HttpContext;
            _logger = logger;
        }

     

        /// <summary>
        /// 成功连接
        /// </summary>
        /// <returns></returns>
        public override Task OnConnectedAsync()
        {
            var name = _httpContext?.GetUserNameInfo();
            var loginUser = _httpContext?.GetLoginLogInfo();
            var user = clientUsers.Any(u => u.ConnnectionId == Context.ConnectionId);
            //判断用户是否存在，否则添加集合
            if (!user &&  (Context.User?.Identity?.IsAuthenticated??false))
            {
                OnlineUser users = new(Context.ConnectionId)
                {
                    Browser= loginUser?.Browser,
                    LoginLocation = loginUser?.LoginLocation,
                    Ipaddr= loginUser?.LoginIp,
                    LoginTime=DateTime.Now,
                    Os=loginUser?.Os,
                    UserName= name??""
                };
                clientUsers.Add(users);
                _logger.LogInformation($"{DateTime.Now}：{name},{Context.ConnectionId}连接服务端success，当前已连接{clientUsers.Count}个");
     
                //Clients.All.SendAsync(HubsConstant.MoreNotice, SendNotice());
            }
            //当有人加入，向全部客户端发送当前总数
            Clients.All.SendAsync(HubTypeEnum.onlineNum.ToString(), clientUsers.Count);
            //Clients.All.SendAsync(HubsConstant.OnlineUser, clientUsers);
            return base.OnConnectedAsync();
        }

        /// <summary>
        /// 断开连接
        /// </summary>
        /// <param name="exception"></param>
        /// <returns></returns>
        public override Task OnDisconnectedAsync(Exception? exception)
        {
            var user = clientUsers.Where(p => p.ConnnectionId == Context.ConnectionId).FirstOrDefault();
            //判断用户是否存在，否则添加集合
            if (user != null)
            {
                clientUsers.Remove(user);
                Clients.All.SendAsync(HubTypeEnum.onlineNum.ToString(), clientUsers.Count);
                //Clients.All.SendAsync(HubsConstant.OnlineUser, clientUsers);
                _logger.LogInformation($"用户{user?.UserName}离开了，当前已连接{clientUsers.Count}个");
            }
            return base.OnDisconnectedAsync(exception);
        }

        public async Task SendAllTest(string test)
        {
            await Clients.All.SendAsync("ReceiveAllInfo", test);
        }
    }
}
