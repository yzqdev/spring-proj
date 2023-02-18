using Microsoft.Extensions.Caching.Memory;
using Microsoft.Extensions.Options;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Yi.Framework.Common.IOCOptions;

namespace Yi.Framework.Core.Cache
{
    public class MemoryCacheClient : CacheInvoker
    {
        private IMemoryCache _client;
        public MemoryCacheClient()
        {
            _client = new MemoryCache(new MemoryCacheOptions());
        }
        public override bool Exits(string key)
        {
            return _client.TryGetValue(key, out var _);
        }
        public override T Get<T>(string key)
        {
            return _client.Get<T>(key);
        }
        public override bool Set<T>(string key, T item)
        {
            return _client.Set(key, item) is not null;
        }

        public override bool Set<T>(string key, T item, TimeSpan time)
        {
            return _client.Set(key, item, time) is not null;
        }

        public override long Del(string key)
        {
            _client.Remove(key);
            return 1;
        }
    }
}
