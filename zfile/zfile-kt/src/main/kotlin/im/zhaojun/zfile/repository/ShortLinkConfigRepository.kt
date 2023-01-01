package im.zhaojun.zfile.repository

import im.zhaojun.zfile.model.entity.ShortLinkConfig
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

/**
 * @author zhaojun
 */
@Repository
interface ShortLinkConfigRepository : JpaRepository<ShortLinkConfig?, Int?>,
    JpaSpecificationExecutor<ShortLinkConfig?> {
    /**
     * 获取驱动器下的所有规则
     *
     * @param       key
     * 短链 Key
     */
    fun findFirstByKey(key: String): ShortLinkConfig

    @Query(
        nativeQuery = true,
        value = " select * from SHORT_LINK where " +
                " key like concat('%', :key,'%') " +
                " and url like concat('%', :url,'%') " +
                " and (:dateFrom is null or create_date >= :dateFrom" +
                " and (:dateTo is null or create_date <= :dateTo) ",
        countQuery = " select count(1) from SHORT_LINK where " +
                " key like concat('%', :key,'%') " +
                " and url like concat('%', :url,'%') " +
                " and (:dateFrom is null or create_date >= :dateFrom" +
                " and (:dateTo is null or create_date <= :dateTo) "
    )
    fun  // @Query(nativeQuery = true,
    //         value = " select * from SHORT_LINK where " +
    //                 " key like concat('%', :key,'%') " +
    //                 " and url like concat('%', :url,'%') " +
    //                 " and (:dateFrom is null or date_format(create_date, '%Y-%m-%d') >= date_format(:dateFrom, '%Y-%m-%d'))" +
    //                 " and (:dateTo is null or date_format(create_date, '%Y-%m-%d') <= date_format(:dateTo, '%Y-%m-%d')) ) ",
    //         countQuery =  " select count(1) from SHORT_LINK where " +
    //                 " key like concat('%', :key,'%') " +
    //                 " and url like concat('%', :url,'%') " +
    //                 " and (:dateFrom  is null or date_format(create_date, '%Y-%m-%d') >= date_format(:dateFrom, '%Y-%m-%d'))" +
    //                 " and (:dateTo is null or date_format(create_date, '%Y-%m-%d') <= date_format(:dateTo, '%Y-%m-%d')) ) "
    // )
            findByPage(
        key: String?,
        url: String?,
        dateFrom: Date?,
        dateTo: Date?,
        pageable: Pageable?
    ): Page<ShortLinkConfig?>?

    /**
     * 获取驱动器下的所有规则
     *
     * @param       url 短链 URL
     */
    fun findFirstByUrl(url: String): ShortLinkConfig

    /**
     * 更新驱动器 ID
     *
     * @param   updateSubPath
     * 原路径部分名称
     *
     * @param   newSubPath
     * 修改后路径部分名称
     */
    @Modifying
    @Query(value = "update SHORT_LINK set url = replace(url, :updateSubPath, :newSubPath)")
    fun updateUrlDriveId(updateSubPath: String?, newSubPath: String?)
}