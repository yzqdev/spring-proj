namespace Yi.Framework.WeChatPay.Options
{
    public class PayOptions
    {
        public string AppID { get; set; }
        public string MchID { get; set; }
        public string Key { get; set; }
        public string NotifyUrl { get; set; }

        public bool IsFileConfig { get; set; } = false;

        public string ConfigPath { get; set; }
    }
}