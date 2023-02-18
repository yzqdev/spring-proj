using Microsoft.Extensions.DependencyInjection;
using Newtonsoft.Json.Serialization;
using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.Json.Serialization;
using System.Threading.Tasks;

namespace Yi.Framework.WebCore.BuilderExtend
{
    public static class JsonExtension
    {
        public static void AddJsonFileService(this IMvcBuilder builder)
        {
            builder.AddJsonOptions(options =>
            options.JsonSerializerOptions.Converters.Add(new JsonStringEnumConverter()));

            builder.AddNewtonsoftJson(options =>
             {
                 options.SerializerSettings.ReferenceLoopHandling = Newtonsoft.Json.ReferenceLoopHandling.Ignore;
                 options.SerializerSettings.DateFormatString = "yyyy-MM-dd HH:mm:ss";

                 //options.SerializerSettings.Converters.Add(new ValueToStringConverter());
             });

        }
    }
}
