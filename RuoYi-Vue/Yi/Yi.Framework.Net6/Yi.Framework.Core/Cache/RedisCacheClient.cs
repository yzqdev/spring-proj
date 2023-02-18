using Microsoft.Extensions.Options;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;
using Yi.Framework.Common.IOCOptions;
using CSRedis;
using static CSRedis.CSRedisClient;

namespace Yi.Framework.Core.Cache
{
    public class RedisCacheClient : CacheInvoker
    {
        private readonly RedisConnOptions _RedisOptions;

        private CSRedisClient _client;

        public RedisCacheClient(IOptionsMonitor<RedisConnOptions> redisConnOptions)
        {
            this._RedisOptions = redisConnOptions.CurrentValue;
            _client = new CSRedisClient($"{_RedisOptions.Host}:{_RedisOptions.Prot},password={_RedisOptions.Password},defaultDatabase ={ _RedisOptions.DB }");
        }
        public override bool Exits(string key)
        {
            return _client.Exists(key);
        }
        public override T Get<T>(string key)
        {
            return _client.Get<T>(key);
        }

        public override bool Set<T>(string key, T data, TimeSpan time)
        {
            return _client.Set(key, data, time);
        }

        public override bool Set<T>(string key, T data)
        {
            return _client.Set(key, data);
        }

        public override long Del(string key)
        {
            return _client.Del(key);
        }

        public override bool HSet(string key, string fieId, object data)
        {
            return _client.HSet(key, fieId, data);
        }

        public override bool HSet(string key, string fieId, object data, TimeSpan time)
        {
            var res = _client.HSet(key, fieId, data);
            var res2 = _client.Expire(key, time);
            return res && res2;
        }

        public override T HGet<T>(string key, string field)
        {
            return _client.HGet<T>(key, field);
        }


        public override long HDel(string key, params string[] par)
        {
            return _client.HDel(key, par);
        }

        public override long HLen(string key)
        {
            return _client.HLen(key);
        }

        public override Dictionary<string, string> HGetAll(string key)
        {
            return _client.HGetAll(key);
        }

        /// <summary>
        /// 简单发布
        /// </summary>
        /// <param name="channel"></param>
        /// <param name="message"></param>
        /// <returns></returns>
        public override long Publish(string channel, string message)
        {
          return   _client.Publish(channel, message);
        }

        /// <summary>
        /// 简单订阅：广播，无持久化，需要Publish写入队列
        /// </summary>
        /// <param name="channels"></param>
        /// <returns></returns>
        public override SubscribeObject Subscribe(params (string, Action<SubscribeMessageEventArgs>)[] channels)
        {
            return _client.Subscribe(channels);
        }

        /// <summary>
        /// 多端争抢模式订阅，需要Lpush写入队列
        /// </summary>
        /// <param name="listKey"></param>
        /// <param name="onMessage"></param>
        /// <returns></returns>
        public override SubscribeListObject SubscribeList(string listKey, Action<string> onMessage)
        {
            return _client.SubscribeList(listKey, onMessage);
        }

        /// <summary>
        /// 多端非争抢模式订阅，需要Lpush写入队列
        /// </summary>
        /// <param name="listKey"></param>
        /// <param name="clientId"></param>
        /// <param name="onMessage"></param>
        /// <returns></returns>
        public override SubscribeListBroadcastObject SubscribeListBroadcast(string listKey, string clientId, Action<string> onMessage)
        {
            return _client.SubscribeListBroadcast(listKey, clientId, onMessage);
        }

        public override bool LSet(string key, long index, object value)
        {
          return  _client.LSet(key, index, value);
        }



        /// <summary>
        /// 列表插入头部
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="key"></param>
        /// <param name="value"></param>
        /// <returns></returns>
        public override long LPush<T>(string key, params T[] value)
        {
            return _client.LPush<T>(key,value);
        }

        /// <summary>
        /// 列表弹出头部
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="key"></param>
        /// <returns></returns>
        public override T LPop<T>(string key)
        {
            return _client.LPop<T>(key);
        }

        public override string[] Keys(string pattern)
        {
            return _client.Keys(pattern); 
        }

    }
}
