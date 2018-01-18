package com.lee.manageplatform.common.entity;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

@Service
public abstract class BaseServiceImpl<T> implements BaseService<T>{
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	protected Mapper<T> mapper;
	
	public List<T> queryByExample(Example example){
		return mapper.selectByExample(example);
	}
	
	public T queryOne(T entity){
    	return mapper.selectOne(entity);
    }
	
	public List<T> query(T entity){
		return mapper.select(entity);
	}

    public List<T> queryAll() {
		return mapper.selectAll();
	}
    
    public T queryByPrimaryKey(Object key) {
		return mapper.selectByPrimaryKey(key);
	}

    public int add(T record) {
		return mapper.insert(record);
	}

    public int addSelective(T record) {
		return mapper.insertSelective(record);
	}

    public int updateByPrimaryKey(T record) {
		return mapper.updateByPrimaryKey(record);
	}

    public int updateByPrimaryKeySelective(T record) {
		return mapper.updateByPrimaryKeySelective(record);
	}
    
    public int updateByExampleSelective(T record, Object example) {
    	return mapper.updateByExampleSelective(record,example);
    }

    public int deleteByPrimaryKey(Object key){
    	return mapper.deleteByPrimaryKey(key);
    }
    
    public int delete(T record){
    	return mapper.delete(record);
    }
}
