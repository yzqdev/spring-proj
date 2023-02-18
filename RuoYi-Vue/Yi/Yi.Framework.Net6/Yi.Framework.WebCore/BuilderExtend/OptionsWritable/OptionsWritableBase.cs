
using Microsoft.Extensions.Options;
using System;

namespace Yi.Framework.WebCore.BuilderExtend.OptionsWritable;

public abstract class OptionsWritableBase<TOptions> : IOptionsWritable<TOptions> where TOptions : class, new()
{
    public OptionsWritableBase(
        IOptionsMonitor<TOptions> options,
        string section)
    {
        this.Monitor = options;
        this.Section = section;
    }

    public IOptionsMonitor<TOptions> Monitor { get; }

    public string Section { get; }

    public TOptions Value => this.Monitor.CurrentValue ?? new TOptions();

    public TOptions Get(string name)
    {
        return this.Monitor.Get(name);
    }

    public abstract void Update(Action<TOptions> configuration);
}