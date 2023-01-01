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
package cn.stylefeng.guns.sys.config;

import cn.stylefeng.guns.sys.core.aop.BusinessLogAop;
import cn.stylefeng.guns.sys.core.aop.DataScopeAop;
import cn.stylefeng.guns.sys.core.aop.PermissionAop;
import cn.stylefeng.guns.sys.core.aop.WrapperAop;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 切面配置
 *
 * @author xuyuxiang
 * @date 2020/3/18 11:25
 */
@Configuration
public class AopConfig {

    /**
     * 日志切面
     *
     * @author xuyuxiang
     * @date 2020/3/20 14:10
     */
    @Bean
    public BusinessLogAop businessLogAop() {
        return new BusinessLogAop();
    }

    /**
     * 权限切面
     *
     * @author xuyuxiang
     * @date 2020/3/23 17:36
     */
    @Bean
    public PermissionAop permissionAop() {
        return new PermissionAop();
    }

    /**
     * 数据范围切面
     *
     * @author xuyuxiang
     * @date 2020/4/6 13:47
     */
    @Bean
    public DataScopeAop dataScopeAop() {
        return new DataScopeAop();
    }

    /**
     * 结果包装的aop
     *
     * @author fengshuonan
     * @date 2020/7/24 22:18
     */
    @Bean
    public WrapperAop wrapperAop() {
        return new WrapperAop();
    }
}
