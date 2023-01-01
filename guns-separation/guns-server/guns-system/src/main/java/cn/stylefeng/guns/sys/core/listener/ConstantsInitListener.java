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
package cn.stylefeng.guns.sys.core.listener;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.db.DbUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.handler.EntityListHandler;
import cn.hutool.db.sql.SqlExecutor;
import cn.hutool.log.Log;
import cn.stylefeng.guns.core.context.constant.ConstantContext;
import cn.stylefeng.guns.core.enums.CommonStatusEnum;
import cn.stylefeng.guns.core.exception.ServiceException;
import cn.stylefeng.guns.sys.modular.consts.enums.SysConfigExceptionEnum;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * 初始化常量的监听器
 * <p>
 * 当spring装配好配置后，就去数据库读constants
 * <p>等待解决</p>
 * <p>
 *     <a href="https://docs.spring.io/spring-boot/docs/2.7.0/reference/htmlsingle/#features.spring-application.application-events-and-listeners">链接</a>
 * </p>
 * @author stylefeng
 * @date 2020/6/6 23:39
 */

public class ConstantsInitListener implements ApplicationListener<ApplicationContextInitializedEvent>, Ordered {

    private static final Log log = Log.get();

    private static final String CONFIG_LIST_SQL = "select code,value from sys_config where status = ?";

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void onApplicationEvent(ApplicationContextInitializedEvent applicationContextInitializedEvent) {
        ConfigurableEnvironment environment = applicationContextInitializedEvent.getApplicationContext().getEnvironment();

        // 获取数据库连接配置
        String dataSourceUrl = environment.getProperty("spring.datasource.url");
        String dataSourceUsername = environment.getProperty("spring.datasource.username");
        String dataSourcePassword = environment.getProperty("spring.datasource.password");

        // 如果有为空的配置，终止执行
        if (ObjectUtil.hasEmpty(dataSourceUrl, dataSourceUsername, dataSourcePassword)) {
            throw new ServiceException(SysConfigExceptionEnum.DATA_SOURCE_NOT_EXIST);
        }

        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            assert dataSourceUrl != null;
            conn = DriverManager.getConnection(dataSourceUrl, dataSourceUsername, dataSourcePassword);

            // 获取sys_config表的数据
            List<Entity> entityList = SqlExecutor.query(conn, CONFIG_LIST_SQL, new EntityListHandler(), CommonStatusEnum.ENABLE.getCode());
            try {
log.info("constantInitlistner");
                Files.writeString(Paths.get("ConstantContextHolder.ctx"),entityList.toString());
            }catch (IOException e){
                e.printStackTrace();
            }
            // 将查询到的参数配置添加到缓存
            if (ObjectUtil.isNotEmpty(entityList)) {
                entityList.forEach(sysConfig -> ConstantContext.putConstant(sysConfig.getStr("code"), sysConfig.getStr("value")));
            }
        } catch (SQLException | ClassNotFoundException e) {
            log.error(">>> 读取数据库constants配置信息出错：{}", e.getMessage());
            throw new ServiceException(SysConfigExceptionEnum.DATA_SOURCE_NOT_EXIST);
        } finally {
            DbUtil.close(conn);
        }

    }
}
