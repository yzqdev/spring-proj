package im.zhaojun.zfile.repository

import im.zhaojun.zfile.model.entity.FilterConfig
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * @author zhaojun
 */
@Repository
interface FilterConfigRepository : JpaRepository<FilterConfig?, Int?> {
    /**
     * 获取驱动器下的所有规则
     * @param       driveId
     * 驱动器 ID
     */
    fun findByDriveId(driveId: String): List<FilterConfig>

    /**
     * 根据驱动器 ID 删除其所有的规则
     * @param       driveId
     * 驱动器 ID
     */
    fun deleteByDriveId(driveId: String?)

    /**
     * 更新驱动器 ID 对应的参数设置为新的驱动器 ID
     *
     * @param   updateId
     * 驱动器原 ID
     *
     * @param   newId
     * 驱动器新 ID
     */
    @Modifying
    @Query(value = "update FILTER_CONFIG set driveId = :newId where driveId = :updateId")
    fun updateDriveId(updateId: String?, newId: String?)
}