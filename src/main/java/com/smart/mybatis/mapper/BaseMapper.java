package com.smart.mybatis.mapper;


import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Mapper
public interface BaseMapper {

    int insertBatch(Map<String, Object> params);

    int insert(Map<String, Object> params);

    int update(Map<String, Object> params);

    int delete(Map<String, Object> params);

    HashMap<String,Object> findById(Map<String, Object> params);

    List<Map<String,Object>> list(Map<String, Object> params);

    HashMap<String,Object> find(Map<String, Object> params);

    /**count*/
    Integer count(Map<String, Object> params);

    /**批量更新*/
    int updateBatch(Map<String, Object> params);
}
