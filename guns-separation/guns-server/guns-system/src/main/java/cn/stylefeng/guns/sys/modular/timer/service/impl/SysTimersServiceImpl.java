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
package cn.stylefeng.guns.sys.modular.timer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.guns.core.exception.ServiceException;
import cn.stylefeng.guns.core.factory.PageFactory;
import cn.stylefeng.guns.core.pojo.page.PageResult;
import cn.stylefeng.guns.core.timer.TimerTaskRunner;
import cn.stylefeng.guns.sys.modular.timer.entity.SysTimers;
import cn.stylefeng.guns.sys.modular.timer.enums.TimerJobStatusEnum;
import cn.stylefeng.guns.sys.modular.timer.enums.exp.SysTimersExceptionEnum;
import cn.stylefeng.guns.sys.modular.timer.mapper.SysTimersMapper;
import cn.stylefeng.guns.sys.modular.timer.param.SysTimersParam;
import cn.stylefeng.guns.sys.modular.timer.service.SysTimersService;
import cn.stylefeng.guns.sys.modular.timer.service.TimerExeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 定时任务 服务实现类
 *
 * @author stylefeng
 * @date 2020/6/30 18:26
 */
@Service
public class SysTimersServiceImpl extends ServiceImpl<SysTimersMapper, SysTimers> implements SysTimersService {

    @Resource
    private TimerExeService timerExeService;

    @Override
    public PageResult<SysTimers> page(SysTimersParam sysTimersParam) {

        // 构造条件
        LambdaQueryWrapper<SysTimers> queryWrapper = new LambdaQueryWrapper<>();

        if (ObjectUtil.isNotNull(sysTimersParam)) {

            // 拼接查询条件-任务名称
            if (ObjectUtil.isNotEmpty(sysTimersParam.getTimerName())) {
                queryWrapper.like(SysTimers::getTimerName, sysTimersParam.getTimerName());
            }

            // 拼接查询条件-状态（字典 1运行  2停止）
            if (ObjectUtil.isNotEmpty(sysTimersParam.getJobStatus())) {
                queryWrapper.like(SysTimers::getJobStatus, sysTimersParam.getJobStatus());
            }
        }

        // 查询分页结果
        return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
    }

    @Override
    public List<SysTimers> list(SysTimersParam sysTimersParam) {

        // 构造条件
        LambdaQueryWrapper<SysTimers> queryWrapper = new LambdaQueryWrapper<>();

        if (ObjectUtil.isNotNull(sysTimersParam)) {

            // 拼接查询条件-任务名称
            if (ObjectUtil.isNotEmpty(sysTimersParam.getTimerName())) {
                queryWrapper.like(SysTimers::getTimerName, sysTimersParam.getTimerName());
            }

            // 拼接查询条件-状态（字典 1运行  2停止）
            if (ObjectUtil.isNotEmpty(sysTimersParam.getJobStatus())) {
                queryWrapper.eq(SysTimers::getJobStatus, sysTimersParam.getJobStatus());
            }
        }

        return this.list(queryWrapper);
    }

    @Override
    public void add(SysTimersParam sysTimersParam) {

        // 将dto转为实体
        SysTimers sysTimers = new SysTimers();
        BeanUtil.copyProperties(sysTimersParam, sysTimers);

        // 设置为停止状态，点击启动时启动任务
        sysTimers.setJobStatus(TimerJobStatusEnum.STOP.getCode());

        this.save(sysTimers);
    }

    @Override
    public void delete(SysTimersParam sysTimersParam) {

        // 先停止id为参数id的定时器
        CronUtil.remove(String.valueOf(sysTimersParam.getId()));

        this.removeById(sysTimersParam.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(SysTimersParam sysTimersParam) {

        // 更新库中记录
        SysTimers oldTimer = this.querySysTimers(sysTimersParam);
        BeanUtil.copyProperties(sysTimersParam, oldTimer);
        this.updateById(oldTimer);

        // 查看被编辑的任务的状态
        Integer jobStatus = oldTimer.getJobStatus();

        // 如果任务正在运行，则停掉这个任务，从新运行任务
        if (jobStatus.equals(TimerJobStatusEnum.RUNNING.getCode())) {
            CronUtil.remove(String.valueOf(oldTimer.getId()));
            timerExeService.startTimer(
                    String.valueOf(sysTimersParam.getId()),
                    sysTimersParam.getCron(),
                    sysTimersParam.getActionClass());
        }
    }

    @Override
    public SysTimers detail(SysTimersParam sysTimersParam) {
        return this.querySysTimers(sysTimersParam);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void start(SysTimersParam sysTimersParam) {

        // 更新库中的状态
        LambdaUpdateWrapper<SysTimers> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SysTimers::getJobStatus, TimerJobStatusEnum.RUNNING.getCode())
                .eq(SysTimers::getId, sysTimersParam.getId());
        this.update(wrapper);

        // 添加定时任务调度
        SysTimers sysTimers = this.querySysTimers(sysTimersParam);
        timerExeService.startTimer(String.valueOf(sysTimers.getId()), sysTimers.getCron(), sysTimers.getActionClass());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void stop(SysTimersParam sysTimersParam) {

        // 更新库中的状态
        LambdaUpdateWrapper<SysTimers> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SysTimers::getJobStatus, TimerJobStatusEnum.STOP.getCode())
                .eq(SysTimers::getId, sysTimersParam.getId());
        this.update(wrapper);

        // 关闭定时任务调度
        SysTimers sysTimers = this.querySysTimers(sysTimersParam);
        timerExeService.stopTimer(String.valueOf(sysTimers.getId()));
    }

    @Override
    public List<String> getActionClasses() {
        //获取继承TimerTaskrunner的类
        Map<String, TimerTaskRunner> timerTaskRunnerMap = SpringUtil.getBeansOfType(TimerTaskRunner.class);
        if (ObjectUtil.isNotEmpty(timerTaskRunnerMap)) {
            Collection<TimerTaskRunner> values = timerTaskRunnerMap.values();
            return values.stream().map(i -> i.getClass().getName()).collect(Collectors.toList());
        } else {
            return CollUtil.newArrayList();
        }
    }

    /**
     * 获取定时任务
     *
     * @author stylefeng
     * @date 2020/6/30 18:26
     */
    private SysTimers querySysTimers(SysTimersParam sysTimersParam) {
        SysTimers sysTimers = this.getById(sysTimersParam.getId());
        if (ObjectUtil.isEmpty(sysTimers)) {
            throw new ServiceException(SysTimersExceptionEnum.NOT_EXISTED);
        }
        return sysTimers;
    }

}
