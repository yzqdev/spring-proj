package cn.hellohao.service;


import java.util.Map;

public interface IRedisService   {

    /**
     * 加入元素
     * @param key
     * @param value
     */
    void setValue(String key, Map<String, Object> value);

    /**
     * 设置值
     *
     * @param key   关键
     * @param value 价值
     */
    void setValue(String key, String value);

    void setValue(String key, Object value);

    /**
     * 获得地图价值
     *
     * @param key 关键
     * @return {@link Object}
     */
    Object getMapValue(String key);

    /**
     * 获得价值
     *
     * @param key 关键
     * @return {@link Object}
     */
    Object getValue(String key);
}