package cn.hellohao.service

interface IRedisService {
    /**
     * 加入元素
     * @param key
     * @param value
     */
    fun setValue(key: String, value: Map<String?, Any?>)

    /**
     * 设置值
     *
     * @param key   关键
     * @param value 价值
     */
    fun setValue(key: String, value: String)
    fun setValue(key: String, value: Any)

    /**
     * 获得地图价值
     *
     * @param key 关键
     * @return [Object]
     */
    fun getMapValue(key: String?): Any

    /**
     * 获得价值
     *
     * @param key 关键
     * @return [Object]
     */
    fun getValue(key: String?): Any
}