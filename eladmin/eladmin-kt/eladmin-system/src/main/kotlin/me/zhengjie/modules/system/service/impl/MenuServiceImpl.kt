/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.system.service.implimport

import cn.hutool.core.collection.CollectionUtil
import cn.hutool.core.util.ObjectUtil
import lombok.RequiredArgsConstructor
import me.zhengjie.exception.BadRequestException
import me.zhengjie.exception.EntityExistException
import me.zhengjie.modules.system.domain.*
import me.zhengjie.modules.system.domain.vo.MenuMetaVo
import me.zhengjie.modules.system.domain.vo.MenuVo
import me.zhengjie.modules.system.repository.MenuRepository
import me.zhengjie.modules.system.repository.UserRepository
import me.zhengjie.modules.system.service.MenuService
import me.zhengjie.modules.system.service.RoleService
import me.zhengjie.modules.system.service.dto.MenuDto
import me.zhengjie.modules.system.service.dto.MenuQueryCriteria
import me.zhengjie.modules.system.service.dto.RoleSmallDto
import me.zhengjie.modules.system.service.mapstruct.MenuMapper
import me.zhengjie.utils.*
import me.zhengjie.utils.FileUtil.downloadExcel
import me.zhengjie.utils.QueryHelp.getAllFields
import me.zhengjie.utils.QueryHelp.getPredicate
import me.zhengjie.utils.ValidationUtil.isNull
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
import javax.servlet.http.HttpServletResponse

me.zhengjie.utils.PageUtil.toPage
import me.zhengjie.base.BaseMapper.toEntity
import me.zhengjie.base.BaseEntity.toString
import me.zhengjie.utils.RsaUtils.decryptByPrivateKey
import me.zhengjie.utils.enums.CodeBiEnum.Companion.find
import me.zhengjie.utils.QueryHelp.getPredicate
import me.zhengjie.base.BaseMapper.toDto
import me.zhengjie.utils.ValidationUtil.isNull
import me.zhengjie.utils.RedisUtils.delByKeys
import me.zhengjie.utils.FileUtil.downloadExcel
import me.zhengjie.utils.enums.DataScopeEnum.Companion.find
import me.zhengjie.utils.QueryHelp.getAllFields
import me.zhengjie.utils.RedisUtils.del
import me.zhengjie.modules.security.service.UserCacheClean.cleanUserCache
import me.zhengjie.modules.security.service.OnlineUserService.kickOutForUsername
import me.zhengjie.utils.FileUtil.checkSize
import me.zhengjie.utils.FileUtil.getExtensionName
import me.zhengjie.utils.FileUtil.upload
import me.zhengjie.config.FileProperties.path
import me.zhengjie.utils.RedisUtils.get
import me.zhengjie.utils.RedisUtils.set
import me.zhengjie.utils.FileUtil.getSize
import me.zhengjie.utils.StringUtils.localIp
import org.springframework.web.bind.annotation.RestController
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.RequestMapping
import me.zhengjie.modules.system.service.JobService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.security.access.prepost.PreAuthorize
import javax.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.validation.annotation.Validated
import me.zhengjie.exception.BadRequestException
import me.zhengjie.modules.system.rest.JobController
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping
import me.zhengjie.modules.system.service.DeptService
import me.zhengjie.modules.system.service.dto.DeptQueryCriteria
import me.zhengjie.modules.system.service.dto.DeptDto
import me.zhengjie.modules.system.rest.DeptController
import cn.hutool.core.collection.CollectionUtil
import me.zhengjie.modules.system.service.DictService
import me.zhengjie.modules.system.service.dto.DictQueryCriteria
import me.zhengjie.modules.system.rest.DictController
import me.zhengjie.modules.system.service.MenuService
import me.zhengjie.modules.system.service.mapstruct.MenuMapper
import me.zhengjie.modules.system.service.dto.MenuQueryCriteria
import me.zhengjie.modules.system.service.dto.MenuDto
import org.springframework.web.bind.annotation.RequestParam
import me.zhengjie.modules.system.rest.MenuController
import me.zhengjie.modules.system.service.RoleService
import org.springframework.web.bind.annotation.PathVariable
import me.zhengjie.modules.system.service.dto.RoleQueryCriteria
import me.zhengjie.modules.system.rest.RoleController
import me.zhengjie.modules.system.service.dto.RoleDto
import me.zhengjie.modules.system.service.dto.RoleSmallDto
import org.springframework.security.crypto.password.PasswordEncoder
import me.zhengjie.modules.system.service.UserService
import me.zhengjie.modules.system.service.DataService
import me.zhengjie.modules.system.service.VerifyService
import me.zhengjie.modules.system.service.dto.UserQueryCriteria
import me.zhengjie.modules.system.domain.vo.UserPassVo
import me.zhengjie.config.RsaProperties
import me.zhengjie.modules.system.service.dto.UserDto
import org.springframework.web.multipart.MultipartFile
import me.zhengjie.utils.enums.CodeEnum
import me.zhengjie.annotation.rest.AnonymousGetMapping
import me.zhengjie.modules.system.rest.LimitController
import me.zhengjie.domain.vo.EmailVo
import me.zhengjie.utils.enums.CodeBiEnum
import me.zhengjie.modules.system.service.MonitorService
import me.zhengjie.modules.system.service.DictDetailService
import me.zhengjie.modules.system.service.dto.DictDetailQueryCriteria
import org.springframework.data.web.PageableDefault
import me.zhengjie.modules.system.service.dto.DictDetailDto
import me.zhengjie.modules.system.rest.DictDetailController
import me.zhengjie.modules.system.domain.vo.MenuMetaVo
import me.zhengjie.modules.system.domain.vo.MenuVo
import lombok.AllArgsConstructor
import me.zhengjie.base.BaseEntity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import com.alibaba.fastjson.annotation.JSONField
import javax.persistence.ManyToMany
import javax.persistence.JoinTable
import javax.persistence.JoinColumn
import me.zhengjie.utils.enums.DataScopeEnum
import javax.persistence.FetchType
import lombok.NoArgsConstructor
import me.zhengjie.base.BaseDTO
import me.zhengjie.modules.system.service.dto.JobSmallDto
import me.zhengjie.modules.system.service.dto.DeptSmallDto
import me.zhengjie.modules.system.service.dto.DictSmallDto
import me.zhengjie.annotation.DataPermission
import org.springframework.cache.annotation.CacheConfig
import me.zhengjie.modules.system.repository.JobRepository
import me.zhengjie.modules.system.service.mapstruct.JobMapper
import me.zhengjie.modules.system.repository.UserRepository
import javax.persistence.criteria.CriteriaBuilder
import me.zhengjie.modules.system.service.dto.JobDto
import me.zhengjie.exception.EntityExistException
import org.springframework.cache.annotation.CacheEvict
import me.zhengjie.modules.system.repository.DeptRepository
import me.zhengjie.modules.system.service.mapstruct.DeptMapper
import me.zhengjie.modules.system.repository.RoleRepository
import me.zhengjie.modules.system.repository.DictRepository
import me.zhengjie.modules.system.service.mapstruct.DictMapper
import me.zhengjie.modules.system.service.dto.DictDto
import me.zhengjie.modules.system.repository.MenuRepository
import me.zhengjie.modules.system.service.mapstruct.RoleMapper
import me.zhengjie.modules.system.service.mapstruct.RoleSmallMapper
import me.zhengjie.modules.security.service.UserCacheClean
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import me.zhengjie.modules.system.service.mapstruct.UserMapper
import me.zhengjie.config.FileProperties
import me.zhengjie.modules.security.service.OnlineUserService
import cn.hutool.extra.template.TemplateUtil
import cn.hutool.extra.template.TemplateConfig
import cn.hutool.core.util.RandomUtil
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OSFileStore
import oshi.hardware.GlobalMemory
import oshi.hardware.VirtualMemory
import oshi.hardware.CentralProcessor
import cn.hutool.core.date.BetweenFormatter
import me.zhengjie.modules.system.repository.DictDetailRepository
import me.zhengjie.modules.system.service.mapstruct.DictDetailMapper
import org.mapstruct.ReportingPolicy
import me.zhengjie.base.BaseMapper
import me.zhengjie.modules.system.service.mapstruct.DictSmallMapper
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying

/**
 * @author Zheng Jie
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = ["menu"])
class MenuServiceImpl : MenuService {
    private val menuRepository: MenuRepository? = null
    private val userRepository: UserRepository? = null
    private val menuMapper: MenuMapper? = null
    private val roleService: RoleService? = null
    private val redisUtils: RedisUtils? = null
    @Throws(Exception::class)
    override fun queryAll(criteria: MenuQueryCriteria, isQuery: Boolean): List<MenuDto?>? {
        val sort = Sort.by(Sort.Direction.ASC, "menuSort")
        if (java.lang.Boolean.TRUE == isQuery) {
            criteria.pidIsNull = true
            val fields = getAllFields(criteria.javaClass, ArrayList())
            for (field in fields) {
                //设置对象的访问权限，保证对private的属性的访问
                field.isAccessible = true
                val `val` = field[criteria]
                if ("pidIsNull" == field.name) {
                    continue
                }
                if (ObjectUtil.isNotNull(`val`)) {
                    criteria.pidIsNull = null
                    break
                }
            }
        }
        return menuMapper!!.toDto(menuRepository!!.findAll({ root: Root<Menu>?, criteriaQuery: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder? ->
            getPredicate(
                root,
                criteria,
                criteriaBuilder!!
            )
        }, sort))
    }

    @Cacheable(key = "'id:' + #p0")
    override fun findById(id: Long): MenuDto {
        val menu = menuRepository!!.findById(id).orElseGet { Menu() }
        isNull(menu.id, "Menu", "id", id)
        return menuMapper!!.toDto(menu)
    }

    /**
     * 用户角色改变时需清理缓存
     *
     * @param currentUserId /
     * @return /
     */
    @Cacheable(key = "'user:' + #p0")
    override fun findByUser(currentUserId: Long?): List<MenuDto> {
        val roles = roleService!!.findByUsersId(currentUserId)
        val roleIds = roles.stream().map { obj: RoleSmallDto -> obj.id }.collect(Collectors.toSet())
        val menus = menuRepository!!.findByRoleIdsAndTypeNot(roleIds, 2)
        return menus.stream().map { entity: Menu -> menuMapper!!.toDto(entity) }
            .collect(Collectors.toList())
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun create(resources: Menu) {
        if (menuRepository!!.findByTitle(resources.title) != null) {
            throw EntityExistException(Menu::class.java, "title", resources.title)
        }
        if (StringUtils.isNotBlank(resources.componentName)) {
            if (menuRepository.findByComponentName(resources.componentName) != null) {
                throw EntityExistException(Menu::class.java, "componentName", resources.componentName)
            }
        }
        if (resources.pid == 0L) {
            resources.pid = null
        }
        if (resources.iFrame) {
            val http = "http://"
            val https = "https://"
            if (!(resources.path.lowercase(Locale.getDefault())
                    .startsWith(http) || resources.path.lowercase(Locale.getDefault()).startsWith(https))
            ) {
                throw BadRequestException("外链必须以http://或者https://开头")
            }
        }
        menuRepository.save(resources)
        // 计算子节点数目
        resources.subCount = 0
        // 更新父节点菜单数目
        updateSubCnt(resources.pid)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun update(resources: Menu) {
        if (resources.id == resources.pid) {
            throw BadRequestException("上级不能为自己")
        }
        val menu = menuRepository!!.findById(resources.id).orElseGet { Menu() }
        isNull(menu.id, "Permission", "id", resources.id)
        if (resources.iFrame) {
            val http = "http://"
            val https = "https://"
            if (!(resources.path.lowercase(Locale.getDefault())
                    .startsWith(http) || resources.path.lowercase(Locale.getDefault()).startsWith(https))
            ) {
                throw BadRequestException("外链必须以http://或者https://开头")
            }
        }
        var menu1 = menuRepository.findByTitle(resources.title)
        if (menu1 != null && menu1.id != menu.id) {
            throw EntityExistException(Menu::class.java, "title", resources.title)
        }
        if (resources.pid == 0L) {
            resources.pid = null
        }

        // 记录的父节点ID
        val oldPid = menu.pid
        val newPid = resources.pid
        if (StringUtils.isNotBlank(resources.componentName)) {
            menu1 = menuRepository.findByComponentName(resources.componentName)
            if (menu1 != null && menu1.id != menu.id) {
                throw EntityExistException(Menu::class.java, "componentName", resources.componentName)
            }
        }
        menu.title = resources.title
        menu.component = resources.component
        menu.path = resources.path
        menu.icon = resources.icon
        menu.iFrame = resources.iFrame
        menu.pid = resources.pid
        menu.menuSort = resources.menuSort
        menu.cache = resources.cache
        menu.hidden = resources.hidden
        menu.componentName = resources.componentName
        menu.permission = resources.permission
        menu.type = resources.type
        menuRepository.save(menu)
        // 计算父级菜单节点数目
        updateSubCnt(oldPid)
        updateSubCnt(newPid)
        // 清理缓存
        delCaches(resources.id)
    }

    override fun getChildMenus(menuList: List<Menu>, menuSet: MutableSet<Menu?>): Set<Menu?> {
        for (menu in menuList) {
            menuSet.add(menu)
            val menus = menuRepository!!.findByPid(menu.id)
            if (menus != null && menus.size != 0) {
                getChildMenus(menus, menuSet)
            }
        }
        return menuSet
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun delete(menuSet: Set<Menu>) {
        for (menu in menuSet) {
            // 清理缓存
            delCaches(menu.id)
            roleService!!.untiedMenu(menu.id)
            menuRepository!!.deleteById(menu.id)
            updateSubCnt(menu.pid)
        }
    }

    override fun getMenus(pid: Long?): List<MenuDto?>? {
        val menus: List<Menu>
        menus = if (pid != null && pid != 0L) {
            menuRepository!!.findByPid(pid)
        } else {
            menuRepository!!.findByPidIsNull()
        }
        return menuMapper!!.toDto(menus)
    }

    override fun getSuperior(menuDto: MenuDto, menus: MutableList<Menu?>): List<MenuDto>? {
        if (menuDto.pid == null) {
            menus.addAll(menuRepository!!.findByPidIsNull())
            return menuMapper!!.toDto(menus)
        }
        menus.addAll(menuRepository!!.findByPid(menuDto.pid))
        return getSuperior(findById(menuDto.pid), menus)
    }

    override fun buildTree(menuDtos: List<MenuDto>): List<MenuDto> {
        var trees: MutableList<MenuDto> = ArrayList()
        val ids: MutableSet<Long> = HashSet()
        for (menuDTO in menuDtos) {
            if (menuDTO.pid == null) {
                trees.add(menuDTO)
            }
            for (it in menuDtos) {
                if (menuDTO.id == it.pid) {
                    if (menuDTO.children == null) {
                        menuDTO.children = ArrayList()
                    }
                    menuDTO.children.add(it)
                    ids.add(it.id)
                }
            }
        }
        if (trees.size == 0) {
            trees = menuDtos.stream().filter { s: MenuDto -> !ids.contains(s.id) }.collect(Collectors.toList())
        }
        return trees
    }

    override fun buildMenus(menuDtos: List<MenuDto?>): List<MenuVo> {
        val list: MutableList<MenuVo> = LinkedList()
        menuDtos.forEach(
            Consumer { menuDTO: MenuDto? ->
                if (menuDTO != null) {
                    val menuDtoList = menuDTO.children
                    val menuVo = MenuVo()
                    menuVo.name =
                        if (ObjectUtil.isNotEmpty(menuDTO.componentName)) menuDTO.componentName else menuDTO.title
                    // 一级目录需要加斜杠，不然会报警告
                    menuVo.path = if (menuDTO.pid == null) "/" + menuDTO.path else menuDTO.path
                    menuVo.hidden = menuDTO.hidden
                    // 如果不是外链
                    if (!menuDTO.iFrame) {
                        if (menuDTO.pid == null) {
                            menuVo.component =
                                if (StringUtils.isEmpty(menuDTO.component)) "Layout" else menuDTO.component
                            // 如果不是一级菜单，并且菜单类型为目录，则代表是多级菜单
                        } else if (menuDTO.type == 0) {
                            menuVo.component =
                                if (StringUtils.isEmpty(menuDTO.component)) "ParentView" else menuDTO.component
                        } else if (StringUtils.isNoneBlank(menuDTO.component)) {
                            menuVo.component = menuDTO.component
                        }
                    }
                    menuVo.meta = MenuMetaVo(menuDTO.title, menuDTO.icon, !menuDTO.cache)
                    if (CollectionUtil.isNotEmpty(menuDtoList)) {
                        menuVo.alwaysShow = true
                        menuVo.redirect = "noredirect"
                        menuVo.children = buildMenus(menuDtoList)
                        // 处理是一级菜单并且没有子菜单的情况
                    } else if (menuDTO.pid == null) {
                        val menuVo1 = MenuVo()
                        menuVo1.meta = menuVo.meta
                        // 非外链
                        if (!menuDTO.iFrame) {
                            menuVo1.path = "index"
                            menuVo1.name = menuVo.name
                            menuVo1.component = menuVo.component
                        } else {
                            menuVo1.path = menuDTO.path
                        }
                        menuVo.name = null
                        menuVo.meta = null
                        menuVo.component = "Layout"
                        val list1: MutableList<MenuVo> = ArrayList()
                        list1.add(menuVo1)
                        menuVo.children = list1
                    }
                    list.add(menuVo)
                }
            }
        )
        return list
    }

    override fun findOne(id: Long): Menu {
        val menu = menuRepository!!.findById(id).orElseGet { Menu() }
        isNull(menu.id, "Menu", "id", id)
        return menu
    }

    @Throws(IOException::class)
    override fun download(menuDtos: List<MenuDto>, response: HttpServletResponse?) {
        val list: MutableList<Map<String?, Any?>?> = ArrayList()
        for (menuDTO in menuDtos) {
            val map: MutableMap<String?, Any?> = LinkedHashMap()
            map["菜单标题"] = menuDTO.title
            map["菜单类型"] = if (menuDTO.type == null) "目录" else if (menuDTO.type == 1) "菜单" else "按钮"
            map["权限标识"] = menuDTO.permission
            map["外链菜单"] = if (menuDTO.iFrame) "是" else "否"
            map["菜单可见"] = if (menuDTO.hidden) "否" else "是"
            map["是否缓存"] = if (menuDTO.cache) "是" else "否"
            map["创建日期"] = menuDTO.getCreateTime()
            list.add(map)
        }
        downloadExcel(list, response!!)
    }

    private fun updateSubCnt(menuId: Long?) {
        if (menuId != null) {
            val count = menuRepository!!.countByPid(menuId)
            menuRepository.updateSubCntById(count, menuId)
        }
    }

    /**
     * 清理缓存
     *
     * @param id 菜单ID
     */
    fun delCaches(id: Long) {
        val users = userRepository!!.findByMenuId(id)
        redisUtils!!.del(CacheKey.MENU_ID + id)
        redisUtils.delByKeys(CacheKey.MENU_USER, users.stream().map { obj: User -> obj.id }
            .collect(Collectors.toSet()))
        // 清除 Role 缓存
        val roles = roleService!!.findInMenuId(object : ArrayList<Long?>() {
            init {
                add(id)
            }
        })
        redisUtils.delByKeys(CacheKey.ROLE_ID, roles.stream().map { obj: Role -> obj.id }
            .collect(Collectors.toSet()))
    }
}