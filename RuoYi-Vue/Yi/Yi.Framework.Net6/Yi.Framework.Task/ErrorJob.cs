using Microsoft.Extensions.Logging;
using Quartz;
using Quartz.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Common.Models;

namespace Yi.Framework.Job
{
    public class ErrorJob : IJob
    {
        private ILogger<ErrorJob> _logger;
        public ErrorJob(ILogger<ErrorJob> logger)
        {
            _logger = logger;
        }

        public Task Execute(IJobExecutionContext context)
        {
            try
            {
                Random random = new Random();
                var p = random.Next(0, 2);
                //这里可能会抛出异常
                var o = 1 / p;
            }
            catch (Exception ex)
            {

                JobExecutionException exception = new JobExecutionException(ex);
                exception.Source = context.JobDetail.Key.Name;
                exception.UnscheduleFiringTrigger = true;
                _logger.LogError(exception, $"{exception.Source}错误");
                throw exception;
            }
            return Task.CompletedTask;
        }
    }
}
