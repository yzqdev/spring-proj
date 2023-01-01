package cn.hellohao.service.impl;

import cn.hellohao.mapper.GroupMapper;
import cn.hellohao.mapper.UserMapper;
import cn.hellohao.model.entity.SiteGroup;
import cn.hellohao.model.entity.SysUser;
import cn.hellohao.exception.CodeException;
import cn.hellohao.model.entity.Msg;
import cn.hellohao.service.GroupService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/19 16:30
 */
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, SiteGroup> implements GroupService {
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Page<SiteGroup> groupList(Page<SiteGroup> page, Integer usertype) {
        return groupMapper.grouplist(page,usertype);
    }

    @Override
    public SiteGroup getGroupListById(String id) {
        return groupMapper.idGroupList(id);
    }

    @Override
    public Msg addGroup(SiteGroup siteGroup) {
        final Msg msg = new Msg();
        if(siteGroup.getUserType()!=0){
            Integer count = groupMapper.getUserTypeCount(siteGroup.getUserType());
            if(count==0){
                groupMapper.addgroup(siteGroup);
                msg.setInfo("添加成功");
            }else{
                msg.setCode("110401");
                msg.setInfo("分配的该用户组已存在。请勿重复分配。");
            }
        }else{
            groupMapper.addgroup(siteGroup);
            msg.setInfo("添加成功");
        }
        return msg;
    }

    @Override
    public Integer getCountFroUserType(Integer usertype) {
        return groupMapper.getUserTypeCount(usertype);
    }

    @Override
    @Transactional//默认遇到throw new RuntimeException(“…”);会回滚
    public Msg deleteGroup(String id) {
        Msg msg = new Msg();
        Integer ret = 0;
        ret = groupMapper.deleteGroup(id);
        if(ret>0){
            List<SysUser> sysUserList = userMapper.getUserListFromGroupId(id);
            for (SysUser sysUser : sysUserList) {
                SysUser u = new SysUser();
                u.setGroupId("1");
                u.setUid(sysUser.getUid());
                userMapper.change(u);
            }
            msg.setInfo("删除成功");
        }else{
            msg.setCode("500");
            msg.setInfo("删除成功");
            throw new CodeException("用户之没有设置成功。");
        }
        return msg;
    }

    @Override
    public Msg setGroup(SiteGroup siteGroup) {
        Msg msg = new Msg();
        if(siteGroup.getUserType()!=0){
            SiteGroup siteGroupFroUserType = groupMapper.getGroupByUserType(siteGroup.getUserType());
            if(siteGroupFroUserType !=null){
                if(siteGroupFroUserType.getUserType().equals(siteGroup.getUserType())){
                    if(siteGroupFroUserType.getId().equals(siteGroup.getId())){
                        groupMapper.updateById(siteGroup);
                        msg.setInfo("修改成功");
                    }else{
                        msg.setCode("110401");
                        msg.setInfo("分配的该用户组已存在。请勿重复分配。");
                    }
                }else{
                    if(groupMapper.getUserTypeCount(siteGroup.getUserType())>0){
                        msg.setCode("110401");
                        msg.setInfo("分配的该用户组已存在。请勿重复分配。");
                    }else{
                        groupMapper.updateById(siteGroup);
                        msg.setInfo("修改成功");
                    }
                }
            }else{
                groupMapper.updateById(siteGroup);
                msg.setInfo("修改成功");
            }
        }else{
            groupMapper.updateById(siteGroup);
            msg.setInfo("修改成功");
        }
        return msg;
    }

    @Override
    public SiteGroup getGroupFroUserType(Integer usertype) {
        return groupMapper.getGroupByUserType(usertype);
    }

}
