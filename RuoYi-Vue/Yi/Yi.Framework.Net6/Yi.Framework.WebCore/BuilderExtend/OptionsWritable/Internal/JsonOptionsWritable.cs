using Microsoft.Extensions.Options;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IO;

namespace Yi.Framework.WebCore.BuilderExtend.OptionsWritable.Internal;

internal class JsonOptionsWritable<TOptions> : FileOptionsWritableBase<TOptions>, IOptionsWritable<TOptions> where TOptions : class, new()
{
    public JsonOptionsWritable(IOptionsMonitor<TOptions> options, string section, string fileName) : base(options, section, fileName)
    {
    }

    public override void Update(Action<TOptions> configuration)
    {
        JObject? jObject = JsonConvert.DeserializeObject<JObject>(File.ReadAllText(this.FileName));
        if (jObject != null)
        {
            TOptions option = this.Monitor.CurrentValue ?? new TOptions();

            if (jObject.TryGetValue(this.Section, out JToken? jtoken))
            {
                option = JsonConvert.DeserializeObject<TOptions>(jtoken.ToString()) ?? new TOptions();
                configuration?.Invoke(option);
                jObject[this.Section] = JObject.Parse(JsonConvert.SerializeObject(option));
            }
            else
            {
                configuration?.Invoke(option);
                jObject.TryAdd(this.Section, JObject.Parse(JsonConvert.SerializeObject(option)));
            }
            File.WriteAllText(this.FileName, JsonConvert.SerializeObject(jObject, Formatting.Indented));
        }
    }
}