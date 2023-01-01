package im.zhaojun.zfile.controller.home

import cn.hutool.core.util.StrUtil
import com.alibaba.fastjson.JSON
import im.zhaojun.zfile.context.DriveContext
import im.zhaojun.zfile.exception.NotEnabledDriveException
import im.zhaojun.zfile.exception.PasswordVerifyException
import im.zhaojun.zfile.model.constant.ZFileConstant
import im.zhaojun.zfile.model.dto.DriveListDTO
import im.zhaojun.zfile.model.dto.FileItemDTO
import im.zhaojun.zfile.model.dto.FileListDTO
import im.zhaojun.zfile.model.dto.SystemFrontConfigDTO
import im.zhaojun.zfile.model.entity.DriveConfig
import im.zhaojun.zfile.model.enums.StorageTypeEnum
import im.zhaojun.zfile.model.support.ResultBean
import im.zhaojun.zfile.model.support.VerifyResult
import im.zhaojun.zfile.service.DriveConfigService
import im.zhaojun.zfile.service.FilterConfigService
import im.zhaojun.zfile.service.base.AbstractBaseFileService
import im.zhaojun.zfile.serviceimport.SystemConfigService
import im.zhaojun.zfile.util.FileComparator
import im.zhaojun.zfile.util.HttpUtil
import im.zhaojun.zfile.util.StringUtils
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException
import java.util.function.Consumer
import java.util.function.Predicate
import javax.annotation.Resource

/**
 * 前台文件管理
 * @author zhaojun
 */
@Slf4j
@RequestMapping("/api")
@RestController
class FileController(private val systemConfigService: SystemConfigService, private val driveContext: DriveContext, private val driveConfigService: DriveConfigService, private val filterConfigService: FilterConfigService) {
    @Value("\${zfile.debug}")
    private val debug: Boolean? = null


    /**
     * 获取所有已启用的驱动器
     *
     * @return  所有已启用驱动器
     */
    @GetMapping("/drive/list")
    fun drives(): ResultBean {
        val driveList: List<DriveConfig> = driveConfigService.listOnlyEnable()
        val isInstall: Boolean = systemConfigService.isInstall
        val driveListDTO = DriveListDTO(driveList, isInstall)
        return ResultBean.success(driveListDTO)
    }

    /**
     * 获取某个驱动器下, 指定路径的数据
     *
     * @param   driveId
     * 驱动器 ID
     *
     * @param   path
     * 路径
     *
     * @param   password
     * 文件夹密码, 某些文件夹需要密码才能访问, 当不需要密码时, 此参数可以为空
     *
     * @return  当前路径下所有文件及文件夹
     */
    @GetMapping("/list/{driveId}")
    @Throws(Exception::class)
    fun list(
        @PathVariable(name = "driveId") driveId: String,
        @RequestParam(defaultValue = "/") path: String,
        @RequestParam(required = false) password: String,
        @RequestParam(required = false) orderBy: String?,
        @RequestParam(required = false, defaultValue = "asc") orderDirection: String?
    ): ResultBean {
        val fileService: AbstractBaseFileService = driveContext.get(driveId)
        val fileItemList: List<FileItemDTO> =
            fileService.fileList(StringUtils.removeDuplicateSeparator(ZFileConstant.PATH_SEPARATOR + path + ZFileConstant.PATH_SEPARATOR))

        // 创建副本, 防止排序和过滤对原数据产生影响
        val copyList: MutableList<FileItemDTO> = ArrayList<FileItemDTO>(fileItemList)

        // 校验密码, 如果校验不通过, 则返回错误消息
        val verifyResult = verifyPassword(copyList, driveId, path, password)
        if (!verifyResult.isPassed()) {
            return ResultBean.error(verifyResult.getMsg(), verifyResult.getCode())
        }

        // 过滤掉驱动器配置的表达式中要隐藏的数据
        filterFileList(copyList, driveId)

        // 按照自然排序
        copyList.sort(FileComparator(orderBy, orderDirection))


        // 开始获取参数信息
        val systemConfig: SystemFrontConfigDTO = systemConfigService.getSystemFrontConfig(driveId)
        val driveConfig: DriveConfig = driveConfigService.findById(driveId)
        val enable: Boolean = driveConfig.getEnable()
        if (!enable) {
            throw NotEnabledDriveException()
        }
        systemConfig.setDebugMode(debug)
        systemConfig.setDefaultSwitchToImgMode(driveConfig.getDefaultSwitchToImgMode())
        systemConfig.setDirectLinkPrefix(ZFileConstant.DIRECT_LINK_PREFIX)

        // 如果不是 FTP 模式，则尝试获取当前文件夹中的 README 文件，有则读取，没有则停止
        if (driveConfig.getType() != StorageTypeEnum.FTP) {
            fileItemList.stream()
                .filter(Predicate<FileItemDTO> { fileItemDTO: FileItemDTO -> ZFileConstant.README_FILE_NAME == fileItemDTO.getName() })
                .findFirst()
                .ifPresent(Consumer<FileItemDTO> { fileItemDTO: FileItemDTO ->
                    val readme = HttpUtil.getTextContent(fileItemDTO.getSrc())
                    systemConfig.setReadme(readme)
                })
        }
        return ResultBean.successData(FileListDTO(copyList, systemConfig))
    }

    /**
     * 校验密码
     * @param   fileItemList
     * 文件列表
     * @param   driveId
     * 驱动器 ID
     * @param   path
     * 请求路径
     * @param   inputPassword
     * 用户输入的密码
     * @return  是否校验通过
     */
    private fun verifyPassword(
        fileItemList: List<FileItemDTO>,
        driveId: String,
        path: String,
        inputPassword: String
    ): VerifyResult {
        val fileService: AbstractBaseFileService = driveContext.get(driveId)
        for (fileItemDTO in fileItemList) {
            if (ZFileConstant.PASSWORD_FILE_NAME == fileItemDTO.name) {
                val expectedPasswordContent: String
                expectedPasswordContent = try {
                    HttpUtil.getTextContent(fileItemDTO.src)
                } catch (httpClientErrorException: HttpClientErrorException) {
                    FileController.log.trace(
                        "尝试重新获取密码文件缓存中链接后仍失败, driveId: {}, path: {}, inputPassword: {}, passwordFile:{} ",
                        driveId, path, inputPassword, JSON.toJSONString(fileItemDTO), httpClientErrorException
                    )
                    try {
                        val pwdFileFullPath =
                            StringUtils.removeDuplicateSeparator(fileItemDTO.path + ZFileConstant.PATH_SEPARATOR + fileItemDTO.name)
                        val pwdFileItem: FileItemDTO = fileService.getFileItem(pwdFileFullPath)
                        HttpUtil.getTextContent(pwdFileItem.src)
                    } catch (e: Exception) {
                        throw PasswordVerifyException(
                            "此文件夹为加密文件夹, 但密码检查异常, 请联系管理员检查密码设置",
                            e
                        )
                    }
                } catch (e: Exception) {
                    throw PasswordVerifyException("此文件夹为加密文件夹, 但密码检查异常, 请联系管理员检查密码设置", e)
                }
                if (matchPassword(expectedPasswordContent, inputPassword)) {
                    break
                }
                return if (StrUtil.isEmpty(inputPassword)) {
                    VerifyResult.fail("此文件夹需要密码.", ResultBean.REQUIRED_PASSWORD)
                } else VerifyResult.fail("密码错误.", ResultBean.INVALID_PASSWORD)
            }
        }
        return VerifyResult.success()
    }

    /**
     * 校验两个密码是否相同, 忽略空白字符
     *
     * @param   expectedPasswordContent
     * 预期密码
     *
     * @param   password
     * 实际输入密码
     *
     * @return  是否匹配
     */
    private fun matchPassword(expectedPasswordContent: String, password: String): Boolean {
        var expectedPasswordContent: String? = expectedPasswordContent
        var password: String? = password
        if (expectedPasswordContent == password) {
            return true
        }
        if (expectedPasswordContent == null) {
            return false
        }
        if (password == null) {
            return false
        }
        expectedPasswordContent = expectedPasswordContent.replace("\n", "").trim { it <= ' ' }
        password = password.replace("\n", "").trim { it <= ' ' }
        return expectedPasswordContent == password
    }

    /**
     * 过滤文件列表, 去除密码, 文档文件和此驱动器通过规则过滤的文件.
     *
     * @param   fileItemList
     * 文件列表
     * @param   driveId
     * 驱动器 ID
     */
    private fun filterFileList(fileItemList: MutableList<FileItemDTO>?, driveId: String) {
        if (fileItemList == null) {
            return
        }
        fileItemList.removeIf(
            Predicate<FileItemDTO> { fileItem: FileItemDTO ->
                ZFileConstant.PASSWORD_FILE_NAME == fileItem.name || ZFileConstant.README_FILE_NAME == fileItem.name || filterConfigService.filterResultIsHidden(
                    driveId,
                    StringUtils.concatUrl(fileItem.path, fileItem.name)
                )
            }
        )
    }
}