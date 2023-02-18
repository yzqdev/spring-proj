using Microsoft.Extensions.Logging;
using Quartz;
using Quartz.Impl.Matchers;
using Quartz.Spi;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Common.Helper;
using Yi.Framework.Job;

namespace Yi.Framework.Core.Quartz
{
    public class QuartzInvoker
    {
        private readonly ISchedulerFactory _schedulerFactory;
        private IScheduler _scheduler;
        private ILogger<QuartzInvoker> _logger;
        private IJobFactory _jobFactory;

        private const string JobDllName = "Yi.Framework.Job";
        public QuartzInvoker(ISchedulerFactory schedulerFactory, ILogger<QuartzInvoker> logger, IJobFactory jobFactory)
        {
            _schedulerFactory = schedulerFactory;
            _logger = logger;
            _jobFactory = jobFactory;
        }

        /// <summary>
        /// 开始任务
        /// </summary>
        /// <param name="cron"></param>
        /// <param name="jobClass"></param>
        /// <param name="jobName"></param>
        /// <param name="jobGroup"></param>
        /// <param name="second"></param>
        /// <param name="data"></param>
        /// <returns></returns>
        public async Task StartAsync(string cron, string jobClass, string jobName = "", string jobGroup = "default", long startAtSecondTime = 0, IDictionary<string, object> data = null)
        {
            jobName = jobName == "" ? jobClass : jobName;
            if (data == null)
            {
                data = new Dictionary<string, object>();
            }
            JobKey jobKey = new JobKey(jobName, jobGroup);
            var myClass = AssemblyHelper.GetClass(JobDllName, jobClass).FirstOrDefault();

            _scheduler = await _schedulerFactory.GetScheduler();
            _scheduler.JobFactory = _jobFactory;

            //创建一个触发器
            var trigger = TriggerBuilder.Create()
                             .StartAt(DateTimeOffset.Now.AddSeconds(startAtSecondTime))
                            .WithCronSchedule(cron)
                            .Build();
            //创建任务
            var jobDetail = JobBuilder.Create(myClass)
                            .UsingJobData(new JobDataMap(data))
                            .WithIdentity(jobKey.Name, jobKey.Group)
                            .Build();

            //await _scheduler.AddJob(jobDetail,false);

            //await _scheduler.ScheduleJob(trigger);
            //将触发器和任务器绑定到调度器中
            await _scheduler.ScheduleJob(jobDetail, trigger);

            //开启调度器
            await _scheduler.Start();

            _logger.LogWarning($"开始任务:{jobKey.Name},组别：{jobKey.Group}");
        }

        /// <summary>
        /// 开始任务
        /// </summary>
        /// <param name="cron"></param>
        /// <param name="jobClass"></param>
        /// <param name="jobName"></param>
        /// <param name="jobGroup"></param>
        /// <param name="second"></param>
        /// <param name="data"></param>
        /// <returns></returns>
        public async Task StartAsync(int milliSecondTime, string jobClass, string jobName = "", string jobGroup = "default", long startAtSecondTime = 0, IDictionary<string, object> data = null)
        {

            jobName = jobName == "" ? jobClass : jobName;

            if (data == null)
            {
                data = new Dictionary<string, object>();
            }
            JobKey jobKey = new JobKey(jobName, jobGroup);
            var myClass = AssemblyHelper.GetClass(JobDllName, jobClass).FirstOrDefault();

            _scheduler = await _schedulerFactory.GetScheduler();
            _scheduler.JobFactory = _jobFactory;

            //创建一个触发器
            var trigger = TriggerBuilder.Create()
                             .StartAt(DateTimeOffset.Now.AddSeconds(startAtSecondTime))
                           .WithSimpleSchedule(option =>
                           {
                               option.WithInterval(TimeSpan.FromMilliseconds(milliSecondTime)).RepeatForever();
                           })

                            .Build();
            //创建任务
            var jobDetail = JobBuilder.Create(myClass)
                            .UsingJobData(new JobDataMap(data))
                            .WithIdentity(jobKey.Name, jobKey.Group)
                            .Build();

            //await _scheduler.AddJob(jobDetail,false);

            //await _scheduler.ScheduleJob(trigger);
            //将触发器和任务器绑定到调度器中
            await _scheduler.ScheduleJob(jobDetail, trigger);

            //开启调度器
            await _scheduler.Start();

            _logger.LogWarning($"开始任务:{jobKey.Name},组别：{jobKey.Group}");
        }


        /// <summary>
        /// 暂停任务
        /// </summary>
        /// <param name="jobKey"></param>
        /// <returns></returns>
        public async Task StopAsync(JobKey jobKey)
        {
            var _scheduler = await _schedulerFactory.GetScheduler();
            //LogUtil.Debug($"暂停任务{jobKey.Group},{jobKey.Name}");
            await _scheduler.PauseJob(jobKey);
            _logger.LogWarning($"暂停任务:{jobKey.Name},组别：{jobKey.Group}");
        }


        public async Task DeleteAsync(JobKey jobKey)
        {
            var _scheduler = await _schedulerFactory.GetScheduler();
            //LogUtil.Debug($"暂停任务{jobKey.Group},{jobKey.Name}");
            await _scheduler.DeleteJob(jobKey);
            _logger.LogWarning($"删除任务:{jobKey.Name},组别：{jobKey.Group}");
        }

        public async Task ResumeAsync(JobKey jobKey)
        {
            var _scheduler = await _schedulerFactory.GetScheduler();
            //LogUtil.Debug($"恢复任务{jobKey.Group},{jobKey.Name}");
            await _scheduler.ResumeJob(jobKey);
            _logger.LogWarning($"恢复任务:{jobKey.Name},组别：{jobKey.Group}");
        }


        /// <summary>
        /// 得到可运行的job列表
        /// </summary>
        /// <returns></returns>
        public List<string> GetJobClassList()
        {
            var myClassList = AssemblyHelper.GetClass("ETX.Job");
            List<string> data = new List<string>();
            myClassList.ForEach(k => data.Add(k.Name));
            return data;
        }

        /// <summary>
        /// 得到现在正在运行的任务列表
        /// </summary>
        /// <returns></returns>
        public async Task<List<JobKey>> getRunJobList()
        {
            _scheduler = await _schedulerFactory.GetScheduler();
            var groups = await _scheduler.GetJobGroupNames();
            var data = new List<JobKey>();
            foreach (var groupName in groups)
            {
                foreach (var jobKey in await _scheduler.GetJobKeys(GroupMatcher<JobKey>.GroupEquals(groupName)))
                {
                    string jobName = jobKey.Name;
                    string jobGroup = jobKey.Group;
                    data.Add(jobKey);
                    var triggers = await _scheduler.GetTriggersOfJob(jobKey);
                    foreach (ITrigger trigger in triggers)
                    {
                        ///下一次的执行时间
                        var utcTime = trigger.GetNextFireTimeUtc();
                        string str = utcTime.ToString();
                        //TimeZone.CurrentTimeZone.ToLocalTime(Convert.ToDateTime(str));


                    }
                }
            }

            return data;
        }
    }



    public class JobKeyModel
    {
        public JobKey jobKey { get; set; }
        public DateTime? nextTime { get; set; }
    }
}

