package cn.hellohao.service.impl

/**
 * redis服务
 *
 * @author yanni
 * @date 2022/06/06
 */
@Service
class RedisServiceImpl : IRedisService {
    @Resource
    private val stringRedisTemplate: StringRedisTemplate? = null

    @Resource
    private val redisTemplate: RedisTemplate<*, *>? = null
    override fun setValue(key: String, value: Map<String?, Any?>) {
        val vo: ValueOperations<String, Any> = redisTemplate!!.opsForValue()
        vo[key] = value
        // 这里指的是1小时后失效
        redisTemplate.expire(key, 1, TimeUnit.HOURS)
    }

    override fun getValue(key: String?): Any {
        val vo: ValueOperations<String, String> = redisTemplate!!.opsForValue()
        return vo[key]
    }

    override fun setValue(key: String, value: String) {
        val vo: ValueOperations<String, Any> = redisTemplate!!.opsForValue()
        vo[key] = value
        redisTemplate.expire(key, 3, TimeUnit.MINUTES) // 这里指的是1小时后失效 时HOURS  分 MINUTES
    }

    override fun setValue(key: String, value: Any) {
        val vo: ValueOperations<String, Any> = redisTemplate!!.opsForValue()
        vo[key] = value
        redisTemplate.expire(key, 1, TimeUnit.HOURS) // 这里指的是1小时后失效
    }

    override fun getMapValue(key: String?): Any {
        val vo: ValueOperations<String, String> = redisTemplate!!.opsForValue()
        return vo[key]
    }
}