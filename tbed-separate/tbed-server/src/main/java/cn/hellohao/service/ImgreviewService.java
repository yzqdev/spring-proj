package cn.hellohao.service;

import cn.hellohao.model.entity.Imgreview;
import org.springframework.stereotype.Service;

@Service
public interface ImgreviewService {
    int deleteByPrimaryKey(Integer id);

    int insert(Imgreview record);

    int insertSelective(Imgreview record);

    Imgreview selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Imgreview record);

    int updateByPrimaryKey(Imgreview record);

    Imgreview selectByusing(Integer using);

}
