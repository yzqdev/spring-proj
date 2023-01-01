package cn.hellohao.mapper;

import cn.hellohao.model.entity.Imgreview;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ImgreviewMapper extends BaseMapper<Imgreview> {
    int deleteByPrimaryKey(Integer id);

    int insert(Imgreview record);

    int insertSelective(Imgreview record);

    Imgreview selectByPrimaryKey(@Param("id") String id);

    Imgreview selectByusing(@Param("using") Integer using);

    int updateByPrimaryKeySelective(Imgreview record);

    int updateByPrimaryKey(Imgreview record);
}