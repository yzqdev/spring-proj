using Microsoft.Extensions.Options;
using Microsoft.IdentityModel.JsonWebTokens;
using Microsoft.IdentityModel.Tokens;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Common.Const;
using Yi.Framework.Common.IOCOptions;
using Yi.Framework.Model.RABC.Entitys;
using JwtRegisteredClaimNames = Microsoft.IdentityModel.JsonWebTokens.JwtRegisteredClaimNames;

namespace Yi.Framework.Core
{
    public class JwtInvoker
    {
        private readonly JWTTokenOptions _JWTTokenOptions;
        public JwtInvoker(IOptionsMonitor<JWTTokenOptions> jwtTokenOptions)
        {
            this._JWTTokenOptions = jwtTokenOptions.CurrentValue;
        }
        public string GetRefreshToken(UserEntity user)
        {
            return this.GetToken(_JWTTokenOptions.ReExpiration, user, null, true);
        }

        public TimeSpan GetTokenExpiration()
        {
            return new TimeSpan(0, _JWTTokenOptions.Expiration, 0); 
        }

        public string GetAccessToken(UserEntity user, HashSet<MenuEntity> menus)
        {
            return this.GetToken(_JWTTokenOptions.Expiration, user, menus);
        }

        private string GetToken(int minutes, UserEntity user, HashSet<MenuEntity> menus, bool isRefresh = false)
        {
            List<Claim> claims = new List<Claim>();
            claims.Add(new Claim(JwtRegisteredClaimNames.Nbf, $"{new DateTimeOffset(DateTime.Now).ToUnixTimeSeconds()}"));
            claims.Add(new Claim(JwtRegisteredClaimNames.Exp, $"{new DateTimeOffset(DateTime.Now.AddMinutes(minutes)).ToUnixTimeSeconds()}"));
            claims.Add(new Claim(JwtRegisteredClaimNames.Sid, user.Id.ToString()));
            claims.Add(new Claim(SystemConst.UserName, user.UserName));
            claims.Add(new Claim(SystemConst.DeptId, user.DeptId.ToString()));
            //-----------------------------以下从user的权限表中添加权限-----------------------例如：

            foreach (var m in menus)
            {
                if (!string.IsNullOrEmpty(m.PermissionCode))
                {
                    claims.Add(new Claim(SystemConst.PermissionClaim, m.PermissionCode.ToString()));
                }
            }
            if (SystemConst.Admin.Equals(user.UserName))
            {
                claims.Add(new Claim(SystemConst.PermissionClaim, SystemConst.AdminPermissionCode));
            }

            var creds = new SigningCredentials(new RsaSecurityKey(Common.Helper.RSAFileHelper.GetKey()), SecurityAlgorithms.RsaSha256);
            var token = new JwtSecurityToken(
                issuer: _JWTTokenOptions.Issuer,
                audience: _JWTTokenOptions.Audience,
                claims: claims,
                expires: DateTime.Now.AddMinutes(minutes),
                signingCredentials: creds);
            var tokenData = new JwtSecurityTokenHandler().WriteToken(token);

            return tokenData;

        }

    }
}
