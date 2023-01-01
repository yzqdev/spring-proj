package cn.hellohao.service;

import cn.hellohao.model.entity.SiteGroup;
import cn.hellohao.model.entity.Msg;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/19 16:29
 */
@Service
public interface GroupService extends IService<SiteGroup> {
    Page<SiteGroup> groupList(Page<SiteGroup> page, Integer usertype);
    SiteGroup getGroupListById(String id);
    Msg addGroup(SiteGroup siteGroup);
    Integer getCountFroUserType(Integer usertype);
    Msg deleteGroup(String id);
    Msg setGroup(SiteGroup siteGroup);
    SiteGroup getGroupFroUserType(Integer usertype);
}
