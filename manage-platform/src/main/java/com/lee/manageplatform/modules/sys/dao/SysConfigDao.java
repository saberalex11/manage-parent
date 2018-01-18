package com.lee.manageplatform.modules.sys.dao;

import com.lee.manageplatform.modules.sys.entity.SysConfigEntity;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 系统配置信息
 *
 * @author hanpeng
 * @date 2016年12月4日 下午6:46:16
 */
@Mapper
public interface SysConfigDao extends BaseDao<SysConfigEntity> {
	
	/**
	 * 根据key，查询value
	 */
	String queryByKey(String paramKey);
	
	/**
	 * 根据key，更新value
	 */
	int updateValueByKey(@Param("key") String key, @Param("value") String value);

    /**
     * 根据type，查询value
     */
    @MapKey("key")
    Map<String,SysConfigEntity> selectByType(Integer type);
	
}