
using Microsoft.Extensions.Options;

namespace Yi.Framework.WebCore.BuilderExtend.OptionsWritable;

public abstract class FileOptionsWritableBase<TOptions> : OptionsWritableBase<TOptions>, IOptionsWritable<TOptions> where TOptions : class, new()
{
    public FileOptionsWritableBase(IOptionsMonitor<TOptions> options, string section, string fileName) : base(options, section)
    {
        this.FileName = fileName;
    }

    public string FileName { get; }
}