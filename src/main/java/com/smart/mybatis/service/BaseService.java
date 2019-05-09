package com.smart.mybatis.service;


import com.github.pagehelper.PageInfo;
import com.smart.mybatis.page.Pageable;
import com.smart.mybatis.pojo.*;

import java.util.List;

public interface BaseService<T> {

    int insert(T entity);

    int update(T entity);

    int delete(T entity);

    Object findById(Long id, Class<T> cls);

    List<T> list(T entity, Class<T> cls);

    List<T> list(T entity, Class<T> cls, List<Query> queryList);

    List<T> list(T entity, Class<T> cls, List<Query> queryList, List<GroupBy> groupByList);

    List<T> list(T entity, Class<T> cls, List<OrderBy> orderList, List<GroupBy> groupByList, List<Like> likes);

    List<T> list(T entity, Class<T> cls, List<Query> queryList, List<OrderBy> orderList, List<GroupBy> groupByList, List<Like> likes);

    List<T> list(T entity, Class<T> cls, List<Query> queryList, List<OrderBy> orderList, List<GroupBy> groupByList, List<Like> likes, List<Compare> compareList);

    Object find(T entity);

    PageInfo<T> page(Pageable pageable, T entity, Class<T> cls);

    PageInfo<T> page(Pageable pageable, T entity, Class<T> cls, List<OrderBy> orderList, List<GroupBy> groupByList, List<Like> likes);

    /**
     * count
     */
    Integer count(T entity, Class<T> cls);

    Integer count(T entity, Class<T> cls, List<Compare> compareList);
}
