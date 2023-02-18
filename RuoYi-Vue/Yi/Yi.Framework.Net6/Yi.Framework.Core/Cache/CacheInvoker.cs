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
    public abstract class CacheInvoker
    {

        public virtual bool Exits(string key)
        {
            throw new NotImplementedException();
        }
        public virtual T Get<T>(string key)
        {
            throw new NotImplementedException();
        }

        public virtual bool Set<T>(string key, T data, TimeSpan time)
        {
            throw new NotImplementedException();
        }

        public virtual bool Set<T>(string key, T data)
        {
            throw new NotImplementedException();
        }

        public virtual long Del(string key)
        {
            throw new NotImplementedException();
        }

        public virtual bool HSet(string key, string fieId, object data)
        {
            throw new NotImplementedException();
        }

        public virtual bool HSet(string key, string fieId, object data, TimeSpan time)
        {
            throw new NotImplementedException();
        }

        public virtual T HGet<T>(string key, string field)
        {
            throw new NotImplementedException();
        }


        public virtual long HDel(string key, params string[] par)
        {
            throw new NotImplementedException();
        }

        public virtual long HLen(string key)
        {
            throw new NotImplementedException();
        }

        public virtual Dictionary<string, string> HGetAll(string key)
        {
            throw new NotImplementedException();
        }

        /// <summary>
        /// 简单发布
        /// </summary>
        /// <param name="channel"></param>
        /// <param name="message"></param>
        /// <returns></returns>
        public virtual long Publish(string channel, string message)
        {
            throw new NotImplementedException();
        }

        /// <summary>
        /// 简单订阅：广播，无持久化，需要Publish写入队列
        /// </summary>
        /// <param name="channels"></param>
        /// <returns></returns>
        public virtual SubscribeObject Subscribe(params (string, Action<SubscribeMessageEventArgs>)[] channels)
        {
            throw new NotImplementedException();
        }

        /// <summary>
        /// 多端争抢模式订阅，需要Lpush写入队列
        /// </summary>
        /// <param name="listKey"></param>
        /// <param name="onMessage"></param>
        /// <returns></returns>
        public virtual SubscribeListObject SubscribeList(string listKey, Action<string> onMessage)
        {
            throw new NotImplementedException();
        }

        /// <summary>
        /// 多端非争抢模式订阅，需要Lpush写入队列
        /// </summary>
        /// <param name="listKey"></param>
        /// <param name="clientId"></param>
        /// <param name="onMessage"></param>
        /// <returns></returns>
        public virtual SubscribeListBroadcastObject SubscribeListBroadcast(string listKey, string clientId, Action<string> onMessage)
        {
            throw new NotImplementedException();
        }

        public virtual bool LSet(string key, long index, object value)
        {
            throw new NotImplementedException();
        }



        /// <summary>
        /// 列表插入头部
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="key"></param>
        /// <param name="value"></param>
        /// <returns></returns>
        public virtual long LPush<T>(string key, params T[] value)
        {
            throw new NotImplementedException();
        }

        /// <summary>
        /// 列表弹出头部
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="key"></param>
        /// <returns></returns>
        public virtual T LPop<T>(string key)
        {
            throw new NotImplementedException();
        }

        public virtual string[] Keys(string pattern)
        {
            throw new NotImplementedException();
        }
    }
}
