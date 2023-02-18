using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Localization;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Common.Models;
using Yi.Framework.Language;

namespace Yi.Framework.WebCore.AspNetCoreExtensions
{
    public static class LocalizerExtend
    {
        public static IServiceCollection AddLocalizerService(this IServiceCollection services)
        {

            services.AddLocalization();
            return services;
        }

        public static void UseLocalizerService(this IApplicationBuilder app)
        {
            Result._local = app.ApplicationServices.GetRequiredService<IStringLocalizer<LocalLanguage>>();

            var support = new[] { "zh", "en" };
            var local = new RequestLocalizationOptions().SetDefaultCulture(support[0])
                .AddSupportedCultures(support)
                .AddSupportedUICultures(support);
            app.UseRequestLocalization(local);

        }
    }
}
