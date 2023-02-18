using Consul;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Options;
using System;
using System.Threading;
using System.Threading.Tasks;
using Yi.Framework.Common.IOCOptions;
using Yi.Framework.WebCore.CommonExtend;

namespace Yi.Framework.WebCore.AspNetCoreExtensions
{
    /// <summary>
    /// 注册ConsulRegisterService 这个servcie在app启动的时候会自动注册服务信息
    /// </summary>
    public class ConsulRegisterHostExtend : IHostedService
    {
        private readonly ConsulRegisterOption _consulRegisterOptions;
        private readonly ConsulClientOption _consulClientOptions;
        public ConsulRegisterHostExtend()
        {
            _consulRegisterOptions = Appsettings.app<ConsulRegisterOption>("ConsulRegisterOption");

            _consulClientOptions = Appsettings.app<ConsulClientOption>("ConsulClientOption");
        }
        public async Task StartAsync(CancellationToken cancellationToken)
        {
            var httpPort = _consulRegisterOptions.Port;
            //var grpcPort = Convert.ToInt32(Appsettings.app("GrpcPort"));
            //------------------Http------------------
            using (ConsulClient client = new ConsulClient(c =>
            {
                c.Address = new Uri($"http://{_consulClientOptions.IP}:{_consulClientOptions.Port}/");
                c.Datacenter = _consulClientOptions.Datacenter;
            }))
            {
                var serviceId = $"{_consulRegisterOptions.IP}:{httpPort}-{_consulRegisterOptions.GroupName}";

                await client.Agent.ServiceDeregister(serviceId, cancellationToken);

                Console.WriteLine($"开始向Consul注册Http[{serviceId}]服务  ...");

                await client.Agent.ServiceRegister(new AgentServiceRegistration()
                {
                    ID = serviceId,//唯一Id
                    Name = _consulRegisterOptions.GroupName,//组名称-Group
                    Address = _consulRegisterOptions.IP,
                    Port = httpPort,
                    Tags = new string[] { "Http" },
                    Check = new AgentServiceCheck()
                    {
                        Interval = TimeSpan.FromSeconds(_consulRegisterOptions.Interval),
                        HTTP = $"http://{_consulRegisterOptions.IP}:{httpPort}/Health",
                        //GRPC = $"{this._consulRegisterOptions.IP}:{grpcPort}",//gRPC特有
                        GRPCUseTLS = false,//支持http
                        Timeout = TimeSpan.FromSeconds(_consulRegisterOptions.Timeout),
                        DeregisterCriticalServiceAfter = TimeSpan.FromSeconds(_consulRegisterOptions.DeregisterCriticalServiceAfter),

                    }
                });
            }


            //------------------Grpc------------------
            //using (ConsulClient client = new ConsulClient(c =>
            //{
            //    c.Address = new Uri($"http://{this._consulClientOptions.IP}:{this._consulClientOptions.Port}/");
            //    c.Datacenter = this._consulClientOptions.Datacenter;
            //}))
            //{
            //    var serviceId = $"{this._consulRegisterOptions.IP}:{grpcPort}-{this._consulRegisterOptions.GrpcGroupName}";

            //    await client.Agent.ServiceDeregister(serviceId, cancellationToken);

            //    Console.WriteLine($"开始向Consul注册Grpc[{serviceId}]服务  ...");

            //    await client.Agent.ServiceRegister(new AgentServiceRegistration()
            //    {
            //        ID = serviceId,//唯一Id
            //        Name = this._consulRegisterOptions.GrpcGroupName,//组名称-Group
            //        Address = this._consulRegisterOptions.IP,
            //        Port = grpcPort,
            //        Tags = new string[] { "Grpc" },
            //        Check = new AgentServiceCheck()
            //        {
            //            Interval = TimeSpan.FromSeconds(this._consulRegisterOptions.Interval),
            //            //HTTP = this._consulRegisterOption.HealthCheckUrl,
            //            GRPC = $"{this._consulRegisterOptions.IP}:{grpcPort}",//gRPC特有
            //            GRPCUseTLS = false,//支持http
            //            Timeout = TimeSpan.FromSeconds(this._consulRegisterOptions.Timeout),
            //            DeregisterCriticalServiceAfter = TimeSpan.FromSeconds(this._consulRegisterOptions.DeregisterCriticalServiceAfter),

            //        }
            //    });
            //}
        }

        /// <summary>
        /// 正常注销调用
        /// </summary>
        /// <param name="cancellationToken"></param>
        /// <returns></returns>
        public async Task StopAsync(CancellationToken cancellationToken)
        {
            var httpPort = _consulRegisterOptions.Port;
            //var grpcPort = Appsettings.app<int>("GrpcPort");


            using (ConsulClient client = new ConsulClient(c =>
            {
                c.Address = new Uri($"http://{_consulClientOptions.IP}:{_consulClientOptions.Port}/");
                c.Datacenter = _consulClientOptions.Datacenter;
            }))
            {


                var serviceId = $"{_consulRegisterOptions.GroupName}:{_consulRegisterOptions.IP}-{httpPort}";

                //var grpcServiceId = $"{this._consulRegisterOptions.GrpcGroupName}:{this._consulRegisterOptions.IP}-{grpcPort}";

                await client.Agent.ServiceDeregister(serviceId, cancellationToken);
                //await client.Agent.ServiceDeregister(grpcServiceId, cancellationToken);
                Console.WriteLine($"开始Consul注销[{serviceId}]服务  ...");

            }

        }
    }
}
