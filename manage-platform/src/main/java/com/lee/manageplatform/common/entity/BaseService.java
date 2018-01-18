package com.lee.manageplatform.common.entity;

import java.util.List;

import tk.mybatis.mapper.entity.Example;

public interface BaseService<T> {
	
	List<T> queryByExample(Example example);

	T queryOne(T entity);

	List<T> query(T entity);

    List<T> queryAll();
    
    T queryByPrimaryKey(Object key);

    int add(T record);

    int addSelective(T record);

    int updateByPrimaryKey(T record);

    int updateByPrimaryKeySelective(T record);
    
    int updateByExampleSelective(T record, Object example);
    
    int deleteByPrimaryKey(Object key);

    int delete(T record);
    
}
