using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Template.Abstract;
using Yi.Framework.Template.Provider;

namespace Yi.Framework.Template
{
    public class TemplateFactory
    {
        private List<ITemplateProvider> _templateProviders=new List<ITemplateProvider>();

        public void CreateTemplateProviders(Action<List<ITemplateProvider>> action)
        {
            action(_templateProviders);
        }

        public void BuildTemplate()
        {
            foreach (var provider in _templateProviders)
            {
                provider.Build();
            }
        }

        public void BakTemplate()
        {
            foreach (var provider in _templateProviders)
            {
                provider.Bak();
            }
        }

    }
}
