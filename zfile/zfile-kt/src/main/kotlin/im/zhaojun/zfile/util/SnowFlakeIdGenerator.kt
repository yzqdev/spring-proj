package im.zhaojun.zfile.util

import org.hibernate.HibernateException
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.interceptor.TransactionAspectSupport
import java.io.Serializable
import javax.annotation.PostConstruct

/**
 * 雪花算法ID生成器
 * @author mr ying
 */
@Component
class SnowFlakeIdGenerator : IdentifierGenerator {
    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * 起始的时间戳
     */
    private val twepoch = 1557825652094L

    /**
     * 每一部分占用的位数
     */
    private val workerIdBits = 5L
    private val datacenterIdBits = 5L
    private val sequenceBits = 12L

    /**
     * 每一部分的最大值
     */
    private val maxWorkerId = -1L xor (-1L shl workerIdBits.toInt())
    private val maxDatacenterId = -1L xor (-1L shl datacenterIdBits.toInt())
    private val maxSequence = -1L xor (-1L shl sequenceBits.toInt())

    /**
     * 每一部分向左的位移
     */
    private val workerIdShift = sequenceBits
    private val datacenterIdShift = sequenceBits + workerIdBits
    private val timestampShift = sequenceBits + workerIdBits + datacenterIdBits

    @Value("\${snowflake.datacenter-id:1}")
    private val datacenterId // 数据中心ID
            : Long = 0

    @Value("\${snowflake.worker-id:0}")
    private val workerId // 机器ID
            : Long = 0
    private var sequence = 0L // 序列号
    private var lastTimestamp = -1L // 上一次时间戳
    @PostConstruct
    fun init() {
        var msg: String
        if (workerId > maxWorkerId || workerId < 0) {
            msg = String.format("worker Id can't be greater than %d or less than 0", maxWorkerId)
            logger.error(msg)
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            msg = String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId)
            logger.error(msg)
        }
    }

    @Transactional
    @Synchronized
    fun nextId(): Long {
        var timestamp = timeGen()
        if (timestamp < lastTimestamp) {
            try {
                throw Exception(
                    String.format(
                        "Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()
            }
        }
        if (timestamp == lastTimestamp) {
            sequence = sequence + 1 and maxSequence
            if (sequence == 0L) {
                timestamp = tilNextMillis()
            }
        } else {
            sequence = 0L
        }
        lastTimestamp = timestamp
        return timestamp - twepoch shl timestampShift.toInt() // 时间戳部分
        or(
            datacenterId shl datacenterIdShift.toInt() // 数据中心部分
        ) or (workerId shl workerIdShift.toInt() // 机器标识部分
                ) or sequence // 序列号部分
    }

    private fun tilNextMillis(): Long {
        var timestamp = timeGen()
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen()
        }
        return timestamp
    }

    private fun timeGen(): Long {
        return System.currentTimeMillis()
    }

    //重写IdentifierGenerator的方法
    @Throws(HibernateException::class)
    override fun generate(session: SharedSessionContractImplementor, o: Any): Serializable {
        return nextId().toString()
    }
}