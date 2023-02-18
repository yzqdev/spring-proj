
using Microsoft.Extensions.Options;
using System;

namespace Yi.Framework.WebCore.BuilderExtend.OptionsWritable;

public interface IOptionsWritable<out TOptions> : IOptionsSnapshot<TOptions> where TOptions : class, new()
{
    void Update(Action<TOptions> configuration);
}