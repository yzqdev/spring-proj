
using Microsoft.Extensions.Logging;
using Quartz;
using System;
using System.IO;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Common.Models;

namespace Yi.Framework.Job
{
    public class VisitJob : IJob
    {
        private ILogger<VisitJob> _logger;
        public VisitJob(ILogger<VisitJob> logger)
        {
            _logger = logger;
        }

        /// <summary>
        /// 应该将拜访清零，并且写入数据库中的拜访表中
        /// </summary>
        /// <param name="context"></param>
        /// <returns></returns>
        public Task Execute(IJobExecutionContext context)
        {
            return Task.Run(() =>
            {
                _logger.LogWarning("定时任务开始调度：" + nameof(VisitJob) + ":" + DateTime.Now.ToString("yyyy-MM-dd HH-mm-ss") + $"：访问总数为:{JobModel.visitNum}");
                JobModel.visitNum = 0;
            }
            );
        }
    }
}

