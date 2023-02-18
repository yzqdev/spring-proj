using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Options;
using System;
using System.Configuration;
using Yi.Framework.WebCore.BuilderExtend.OptionsWritable.Internal;

namespace Yi.Framework.WebCore.BuilderExtend.OptionsWritable;

public static class OptionsWritableConfigurationServiceCollectionExtensions
{
    public static void ConfigureJson<TOption>(this IServiceCollection services, Microsoft.Extensions.Configuration.ConfigurationManager configuration, string jsonFilePath)
        where TOption : class, new()
    {
        services.ConfigureJson<TOption>(configuration.GetSection(typeof(TOption).Name), jsonFilePath);
    }

    public static void ConfigureJson<TOption>(this IServiceCollection services, IConfigurationSection section, string jsonFilePath)
        where TOption : class, new()
    {
        services.Configure<TOption>(section);
        services.AddTransient<IOptionsWritable<TOption>>(provider =>
        new JsonOptionsWritable<TOption>(provider.GetRequiredService<IOptionsMonitor<TOption>>(), section.Key, jsonFilePath));
    }

    public static void ConfigureJson<TOption>(this IServiceCollection services, Microsoft.Extensions.Configuration.ConfigurationManager configuration, Func<IServiceProvider, string> jsonFilePathFunc)
        where TOption : class, new()
    {
        services.ConfigureJson<TOption>(configuration.GetSection(typeof(TOption).Name), jsonFilePathFunc);
    }

    public static void ConfigureJson<TOption>(this IServiceCollection services, IConfigurationSection section, Func<IServiceProvider, string> jsonFilePathFunc)
        where TOption : class, new()
    {
        services.Configure<TOption>(section);

        services.AddTransient<IOptionsWritable<TOption>>(provider =>
        new JsonOptionsWritable<TOption>(provider.GetRequiredService<IOptionsMonitor<TOption>>(), section.Key, jsonFilePathFunc.Invoke(provider)));
    }
}