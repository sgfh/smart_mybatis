package com.smart.mybatis.mapper;


import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Mapper
public interface BaseMapper {

    int insert(Map<String, Object> params);

    int update(Map<String, Object> params);

    int delete(Map<String, Object> params);

    HashMap<String,Object> findById(Map<String, Object> params);

    List<Map<String,Object>> list(Map<String, Object> params);

    HashMap<String,Object> find(Map<String, Object> params);

    HashMap<String,Object> findLinkT(Map<String, Object> params);

    List<Map<String,Object>> findLinkListT(Map<String, Object> params);

    //带有排序
    List<Map<String,Object>> findOrderLinkT(Map<String, Object> params);

    /**count*/
    Integer count(Map<String, Object> params);
}
