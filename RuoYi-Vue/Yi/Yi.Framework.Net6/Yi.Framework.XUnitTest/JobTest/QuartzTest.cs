using Yi.Framework.Common.Const;
using Yi.Framework.Core.Quartz;

namespace Yi.Framework.XUnitTest
{
    public class QuartzTest
    {
        private QuartzInvoker _quartzInvoker;
        public QuartzTest(QuartzInvoker quartzInvoker) =>
            (_quartzInvoker) =
            (quartzInvoker);

        /// <summary>
        /// ��������
        /// </summary>
        /// <returns></returns>
        [Fact]
        public async Task StartJob()
        {
            Dictionary<string, object> data = new Dictionary<string, object>()
            {
                {JobConst.method,"get" },
                {JobConst.url,"https://www.baidu.com" }
            };
            await _quartzInvoker.StartAsync("*/5 * * * * ?", "HttpJob", jobName: "test", jobGroup: "my", data: data);
        }

        /// <summary>
        /// ֹͣ����
        /// </summary>
        /// <returns></returns>
        [Fact]
        public async Task StopJob()
        {
            await StartJob();
            await _quartzInvoker.StopAsync(new Quartz.JobKey("test", "my"));
        }
    }
}