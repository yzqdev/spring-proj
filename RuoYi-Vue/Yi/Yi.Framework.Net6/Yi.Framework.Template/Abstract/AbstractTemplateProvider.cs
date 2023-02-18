using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Yi.Framework.Template.Abstract
{
    public abstract class AbstractTemplateProvider : ITemplateProvider
    {
        public virtual string? BuildPath { get; set; }
        public string? TemplatePath { get; set; }
        public string? BakPath { get; set; }
        protected Dictionary<string, string> TemplateDic { get; set; } = new Dictionary<string, string>();

        public abstract void Bak();

        public abstract void Build();


        protected virtual string GetTemplateData()
        {
            if (TemplatePath is null)
            {
                throw new ArgumentNullException(nameof(TemplatePath));
            }
            return File.ReadAllText(TemplatePath);
        }

        protected void AddTemplateDic(string oldStr, string newStr)
        {

            TemplateDic.Add(oldStr, newStr);
        }
    }
}
