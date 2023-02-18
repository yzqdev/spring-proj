using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Common.Models;
using Yi.Framework.Repository;
using Yi.Framework.WebCore.CommonExtend;

namespace Yi.Framework.WebCore.AspNetCoreExtensions
{
    public static class SqlsugarExtension
    {
        public static void AddSqlsugarServer(this IServiceCollection services, Action<SqlSugarClient>? action = null)
        {
            DbType dbType;
            var slavaConFig = new List<SlaveConnectionConfig>();
            if (Appsettings.appBool("MutiDB_Enabled"))
            {
                var readCon = Appsettings.app<List<string>>("DbConn", "ReadUrl");

                readCon.ForEach(s =>
                {
                    //如果是动态saas分库，这里的连接串都不能写死，需要动态添加，这里只配置共享库的连接
                    slavaConFig.Add(new SlaveConnectionConfig() { ConnectionString = s });
                });
            }

            switch (Appsettings.app("DbSelect"))
            {
                case "Mysql": dbType = DbType.MySql; break;
                case "Sqlite": dbType = DbType.Sqlite; break;
                case "Sqlserver": dbType = DbType.SqlServer; break;
                case "Oracle": dbType = DbType.Oracle; break;
                default: throw new Exception("DbSelect配置写的TM是个什么东西？");
            }
            SqlSugarScope sqlSugar = new SqlSugarScope(new ConnectionConfig()
            {
                //准备添加分表分库
                DbType = dbType,
                ConnectionString = Appsettings.app("DbConn", "WriteUrl"),
                IsAutoCloseConnection = true,
                MoreSettings = new ConnMoreSettings()
                {
                    DisableNvarchar = true
                },
                SlaveConnectionConfigs = slavaConFig,
                //设置codefirst非空值判断
                ConfigureExternalServices = new ConfigureExternalServices
                {
                    EntityService = (c, p) =>
                    {
                        //// int?  decimal?这种 isnullable=true
                        //if (c.PropertyType.IsGenericType &&
                        //c.PropertyType.GetGenericTypeDefinition() == typeof(Nullable<>))
                        //{
                        //    p.IsNullable = true;
                        //}

                        //高版C#写法 支持string?和string  
                        if (new NullabilityInfoContext()
                        .Create(c).WriteState is NullabilityState.Nullable)
                        {
                            p.IsNullable = true;
                        }
                    }
                }
            },
         db =>
         {
             if (action is not null)
             {
                 action(db);
             }

             db.Aop.DataExecuting = (oldValue, entityInfo) =>
             {
                 //var httpcontext = ServiceLocator.Instance.GetService<IHttpContextAccessor>().HttpContext;
                 switch (entityInfo.OperationType)
                 {
                     case DataFilterType.InsertByObject:
                         if (entityInfo.PropertyName == "CreateUser")
                         {
                             //entityInfo.SetValue(new Guid(httpcontext.Request.Headers["Id"].ToString()));
                         }
                         if (entityInfo.PropertyName == "TenantId")
                         {
                             //entityInfo.SetValue(new Guid(httpcontext.Request.Headers["TenantId"].ToString()));
                         }
                         break;
                     case DataFilterType.UpdateByObject:
                         if (entityInfo.PropertyName == "ModifyTime")
                         {
                             entityInfo.SetValue(DateTime.Now);
                         }
                         if (entityInfo.PropertyName == "ModifyUser")
                         {
                             //entityInfo.SetValue(new Guid(httpcontext.Request.Headers["Id"].ToString()));
                         }
                         break;
                 }

             };
             db.Aop.OnLogExecuting = (s, p) =>
             {
                 if (GobalModel.SqlLogEnable)
                 {
                     var _logger = ServiceLocator.Instance?.GetRequiredService<ILogger<SqlSugarClient>>();

                     StringBuilder sb = new StringBuilder();
                     sb.Append("执行SQL:" + s.ToString());
                     foreach (var i in p)
                     {
                         sb.Append($"\r\n参数:{i.ParameterName},参数值:{i.Value}");
                     }
                     sb.Append( $"\r\n 完整SQL：{UtilMethods.GetSqlString(DbType.MySql, s, p)}");
                     _logger?.LogInformation(sb.ToString());
                 }


             };

         });
            services.AddSingleton<ISqlSugarClient>(sqlSugar);//这边是SqlSugarScope用AddSingleton

            //添加工作单元
            services.AddSingleton<ISugarUnitOfWork<UnitOfWork>>(new SugarUnitOfWork<UnitOfWork>(sqlSugar));
        }
    }

}