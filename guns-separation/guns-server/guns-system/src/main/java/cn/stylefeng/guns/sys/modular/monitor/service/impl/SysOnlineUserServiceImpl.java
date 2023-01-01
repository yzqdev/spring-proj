/*
Copyright [2020] [https://www.stylefeng.cn]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Guns采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：

1.请不要删除和修改根目录下的LICENSE文件。
2.请不要删除和修改Guns源码头部的版权声明。
3.请保留源码和相关描述文件的项目出处，作者声明等。
4.分发源码时候，请注明软件出处 https://gitee.com/stylefeng/guns-separation
5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/stylefeng/guns-separation
6.若您的项目无法满足以上几点，可申请商业授权，获取Guns商业授权许可，请在官网购买授权，地址为 https://www.stylefeng.cn
 */
package cn.stylefeng.guns.sys.modular.monitor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.guns.core.context.constant.ConstantContextHolder;
import cn.stylefeng.guns.core.context.login.LoginContextHolder;
import cn.stylefeng.guns.core.exception.DemoException;
import cn.stylefeng.guns.core.factory.PageFactory;
import cn.stylefeng.guns.core.pojo.login.SysLoginUser;
import cn.stylefeng.guns.core.pojo.page.PageResult;
import cn.stylefeng.guns.core.util.PageUtil;
import cn.stylefeng.guns.sys.core.log.LogManager;
import cn.stylefeng.guns.sys.core.redis.RedisUtils;
import cn.stylefeng.guns.sys.modular.monitor.param.SysOnlineUserParam;
import cn.stylefeng.guns.sys.modular.monitor.result.SysOnlineUserResult;
import cn.stylefeng.guns.sys.modular.monitor.service.SysOnlineUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;


/**
 * 系统组织机构service接口实现类
 *
 * @author xuyuxiang
 * @date 2020/4/7 17:06
 */
@Service
@Slf4j
public class SysOnlineUserServiceImpl implements SysOnlineUserService {

    @Value("${online-key}")
    private String onlineKey;
    @Resource
    //private UserCache userCache;
    private RedisUtils redisUtils;

    /**
     * 使用内存缓存获取在线用户列表
     *
     * @param sysOnlineUserParam 系统在线用户参数
     * @return {@link PageResult}<{@link SysOnlineUserResult}>
     */
    //@Override
    //public PageResult<SysOnlineUserResult> listMemory(SysOnlineUserParam sysOnlineUserParam) {
    //    List<SysOnlineUserResult> tempList = CollectionUtil.newArrayList();
    //    // 获取内存缓存中的所有用户
    //    Map<String, SysLoginUser> allKeyValues = userCache.getAllKeyValues();
    //    for (Map.Entry<String, SysLoginUser> sysLoginUserEntry : allKeyValues.entrySet()) {
    //        SysOnlineUserResult sysOnlineUserResult = new SysOnlineUserResult();
    //        sysOnlineUserResult.setSessionId(sysLoginUserEntry.getKey());
    //        BeanUtil.copyProperties(sysLoginUserEntry.getValue(), sysOnlineUserResult);
    //        tempList.add(sysOnlineUserResult);
    //    }
    //    List<SysOnlineUserResult> listAll = tempList.stream()
    //            .sorted(Comparator.comparing(SysOnlineUserResult::getLastLoginTime, Comparator.reverseOrder()))
    //            .collect(Collectors.toList());
    //    Page<SysOnlineUserResult> page = PageFactory.defaultPage();
    //    page.setTotal(tempList.size());
    //    List<SysOnlineUserResult> resultList = PageUtil.page(page, listAll);
    //    return new PageResult<>(page, resultList);
    //}
    @Override
    public PageResult<SysOnlineUserResult> list(SysOnlineUserParam sysOnlineUserParam) {
        List<SysOnlineUserResult> tempList = CollUtil.newArrayList();
        List<String> keys = redisUtils.scan(onlineKey + "*");
        for (String key : keys) {
            SysOnlineUserResult sysOnlineUserResult = new SysOnlineUserResult();
            sysOnlineUserResult.setSessionId(key);
            BeanUtil.copyProperties(redisUtils.get(key), sysOnlineUserResult);
            tempList.add(sysOnlineUserResult);
        }

        List<SysOnlineUserResult> listAll = tempList.stream()
                .sorted(Comparator.comparing(SysOnlineUserResult::getLastLoginTime, Comparator.reverseOrder()))
                .toList();
        Page<SysOnlineUserResult> page = PageFactory.defaultPage();
        page.setTotal(tempList.size());
        List<SysOnlineUserResult> resultList = PageUtil.page(page, listAll);
        return new PageResult<>(page, resultList);
    }

    @Override
    public void forceExist(SysOnlineUserParam sysOnlineUserParam) {
        Boolean demoEnvFlag = ConstantContextHolder.getDemoEnvFlag();
        if (Boolean.TRUE.equals(demoEnvFlag)) {
            throw new DemoException();
        }

        //获取缓存的key
        String redisLoginUserKey = sysOnlineUserParam.getSessionId();


        //获取内存缓存的用户
        //SysLoginUser sysLoginUser = userCache.get(redisLoginUserKey);
        //获取redis缓存的用户
        SysLoginUser sysLoginUser =  BeanUtil.toBean(redisUtils.get( redisLoginUserKey),SysLoginUser.class);
        //如果缓存的用户存在，清除会话，否则表示该会话信息已失效，不执行任何操作
        if (ObjectUtil.isNotNull(sysLoginUser)) {

            //清除登录会话
            //userCache.remove(redisLoginUserKey);
            //使用redis清除
            redisUtils.del( redisLoginUserKey);

            //获取登录用户的账户信息
            String account = LoginContextHolder.me().getSysLoginUserAccount();

            //创建退出登录日志
            LogManager.me().executeExitLog(account);
        }
    }
}
