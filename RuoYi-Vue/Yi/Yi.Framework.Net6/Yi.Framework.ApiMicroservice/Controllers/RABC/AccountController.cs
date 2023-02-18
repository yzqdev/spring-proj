using Hei.Captcha;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Yi.Framework.Common.Const;
using Yi.Framework.Common.Enum;
using Yi.Framework.Common.Helper;
using Yi.Framework.Common.Models;
using Yi.Framework.Core;
using Yi.Framework.Core.Cache;
using Yi.Framework.DtoModel.Base.Dto;
using Yi.Framework.Interface;
using Yi.Framework.Interface.RABC;
using Yi.Framework.Model.RABC.Entitys;
using Yi.Framework.Repository;
using Yi.Framework.WebCore.AttributeExtend;
using Yi.Framework.WebCore.AuthorizationPolicy;
using Yi.Framework.WebCore.CommonExtend;

namespace Yi.Framework.ApiMicroservice.Controllers
{
    /// <summary>
    /// 账户管理
    /// </summary>
    [ApiController]
    [Authorize]
    [Route("api/[controller]/[action]")]
    public class AccountController : ControllerBase
    {
        private IUserService _iUserService;
        private JwtInvoker _jwtInvoker;
        private ILogger _logger;
        private SecurityCodeHelper _securityCode;
        private IRepository<UserEntity> _repository;
        private CacheInvoker _cacheDb;
        public AccountController(ILogger<UserEntity> logger,
            IUserService iUserService,
            JwtInvoker jwtInvoker,
            SecurityCodeHelper securityCode,
            CacheInvoker cacheInvoker)
        {
            _iUserService = iUserService;
            _jwtInvoker = jwtInvoker;
            _logger = logger;
            _securityCode = securityCode;
            _repository = iUserService._repository;
            _cacheDb = cacheInvoker;
        }

        /// <summary>
        ///  重置管理员CC的密码
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        [AllowAnonymous]
        public async Task<Result> RestCC()
        {
            var user = await _iUserService._repository.GetFirstAsync(u => u.UserName == "cc");
            user.Password = "123456";
            user.BuildPassword();
            await _iUserService._repository.UpdateIgnoreNullAsync(user);
            return Result.Success();
        }

        /// <summary>
        /// 没啥说，登录
        /// </summary>
        /// <param name="loginDto"></param>
        /// <returns></returns>
        [AllowAnonymous]
        [HttpPost]
        public async Task<Result> Login(LoginDto loginDto)
        {
            //跳过，需要redis缓存获取uuid与code的关系，进行比较即可
            //先效验验证码和UUID
            //登录还需要进行登录日志的落库

            //先进行验证码的效验

            var code = _cacheDb.Get<string>($"Yi:Captcha:{loginDto.Uuid}");
            //判断是否开启二维码效验
            if (GobalModel.LoginCodeEnable)
            {
                if (code != loginDto.Code)
                {
                    return Result.Error("验证码错误！");
                }
            }


            var loginInfo = HttpContext.GetLoginLogInfo();

            loginInfo.LoginUser = loginDto.UserName;
            loginInfo.LogMsg = "登录成功！";


            var loginLogRepository = _repository.ChangeRepository<Repository<LoginLogEntity>>();
            UserEntity user = new();

            //这里其实可以返回Dto
            if (await _iUserService.Login(loginDto.UserName, loginDto.Password, o => user = o))
            {
                //根据用户id获取改用户的完整信息
                var userRoleMenu = await _iUserService.GetUserAllInfo(user.Id);

                //如果该用户没有任何一个菜单，或者没有任何一个角色，无意义的登录
                if (userRoleMenu.PermissionCodes.Count == 0)
                {
                    return Result.Error("登录禁用！该用户分配无任何权限，无意义登录！");
                }


                //将该用户的完整信息缓存一份至缓存，后续需要完整用户信息，只需通过token中的id从缓存中获取即可

                //先制作token
                var token = _jwtInvoker.GetAccessToken(userRoleMenu.User, userRoleMenu.Menus);

                //需要注意，缓存用户信息时间应大于或等于token过期时间
                _cacheDb.Set($"Yi:UserInfo:{user.Id}", userRoleMenu, _jwtInvoker.GetTokenExpiration());


                await loginLogRepository.InsertReturnSnowflakeIdAsync(loginInfo);
                return Result.Success(loginInfo.LogMsg).SetData(new { token });
            }
            loginInfo.LogMsg = "登录失败！用户名或者密码错误！";
            await loginLogRepository.InsertReturnSnowflakeIdAsync(loginInfo);
            return Result.Error(loginInfo.LogMsg);
        }



        /// <summary>
        /// 没啥说，注册
        /// </summary>
        /// <param name="registerDto"></param>
        /// <returns></returns>
        [AllowAnonymous]
        [HttpPost]
        public async Task<Result> Register(RegisterDto registerDto)
        {
            UserEntity user = new();
            if (await _iUserService.Register(WebCore.Mapper.MapperHelper.Map<UserEntity, RegisterDto>(registerDto), o => user = o))
            {
                return Result.Success("注册成功！").SetData(user);
            }
            return Result.SuccessError("注册失败！用户名已存在！");
        }

        /// <summary>
        /// 没啥说，登出
        /// </summary>
        /// <returns></returns>
        [HttpPost]
        [AllowAnonymous]
        public Result Logout()
        {
            return Result.Success("安全登出成功！");
        }

        /// <summary>
        /// 通过已登录的用户获取用户信息
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public Result GetUserAllInfo()
        {
            //通过鉴权jwt获取到用户的id
            var userId = HttpContext.GetUserIdInfo();
            //此处从缓存中获取即可
            var data = _cacheDb.Get<UserRoleMenuDto>($"Yi:UserInfo:{userId}");
            //var data = await _iUserService.GetUserAllInfo(userId);
            //系统用户数据被重置，老前端访问重新授权
            if (data is null)
            {
                return Result.UnAuthorize();
            }

            data.Menus.Clear();
            return Result.Success().SetData(data);
        }

        /// <summary>
        /// 获取当前登录用户的前端路由
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public async Task<Result> GetRouterInfo()
        {
            var userId = HttpContext.GetUserIdInfo();
            var data = await _iUserService.GetUserAllInfo(userId);
            var menus = data.Menus.ToList();

            //为超级管理员直接给全部路由
            if (SystemConst.Admin.Equals(data.User.UserName))
            {
                menus = await _iUserService._repository.ChangeRepository<Repository<MenuEntity>>().GetListAsync();
            }
            //将后端菜单转换成前端路由，组件级别需要过滤
            List<VueRouterModel> routers = MenuEntity.RouterBuild(menus);
            return Result.Success().SetData(routers);
        }


        /// <summary>
        /// 自己更新密码
        /// </summary>
        /// <param name="dto"></param>
        /// <returns></returns>
        [HttpPut]
        public async Task<Result> UpdatePassword(UpdatePasswordDto dto)
        {
            long userId = HttpContext.GetUserIdInfo();

            if (await _iUserService.UpdatePassword(dto, userId))
            {
                return Result.Success();
            }
            return Result.Error("更新失败！");
        }

        /// <summary>
        /// 验证码
        /// </summary>
        /// <returns></returns>
        [AllowAnonymous]
        [HttpGet]
        public Result CaptchaImage()
        {
            var uuid = Guid.NewGuid();
            var code = _securityCode.GetRandomEnDigitalText(4);
            //将uuid与code，Redis缓存中心化保存起来，登录根据uuid比对即可
            //10分钟过期
            _cacheDb.Set($"Yi:Captcha:{uuid}", code, new TimeSpan(0, 10, 0));
            var imgbyte = _securityCode.GetEnDigitalCodeByte(code);
            return Result.Success().SetData(new { uuid = uuid, img = imgbyte });
        }
    }
}
