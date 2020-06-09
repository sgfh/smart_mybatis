package com.smart.mybatis.service;


import com.github.pagehelper.PageInfo;
import com.smart.mybatis.page.Pageable;
import com.smart.mybatis.pojo.*;

import java.util.List;

public interface BaseService<T> {

    int insert(T entity);

    int insert(List<T> list);

    int update(T entity);

    int update(T entity, List<UpdateColumn> limitList);

    int updateBatch(List<T> entities);

    int delete(T entity);

    T findById(Long id, Class<T> cls);

    List<T> list(T entity);

    List<T> list(T entity,  List<Query> queryList);

    List<T> list(T entity, List<Query> queryList, List<GroupBy> groupByList);

    List<T> list(T entity,  List<OrderBy> orderList, List<GroupBy> groupByList, List<Like> likes);

    List<T> list(T entity, List<Query> queryList, List<OrderBy> orderList, List<GroupBy> groupByList, List<Like> likes);

    List<T> list(T entity, List<Query> queryList, List<OrderBy> orderList, List<GroupBy> groupByList, List<Like> likes, List<Compare> compareList);

    T find(T entity);

    T find(T entity, List<Query> queryList);

    PageInfo<T> page(Pageable pageable, T entity);

    PageInfo<T> page(Pageable pageable, T entity, List<OrderBy> orderList, List<GroupBy> groupByList, List<Like> likes);
    PageInfo<T> page(Pageable pageable, T entity, List<Query> queryList, List<OrderBy> orderList, List<GroupBy> groupByList, List<Like> likes, List<Compare> compareList);

    Integer count(T entity, CountField countField);


    Integer count(T entity,  CountField countField, List<Compare> compareList);

    Integer count(T entity, CountField countField, List<Compare> compareList, List<Query> queryList);
}
