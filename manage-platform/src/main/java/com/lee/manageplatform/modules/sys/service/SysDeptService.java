package com.lee.manageplatform.modules.sys.service;


import com.lee.manageplatform.modules.sys.entity.SysDeptEntity;

import java.util.List;
import java.util.Map;

/**
 * 部门管理
 *
 * @author hanpeng
 * @date 2017-06-20 15:23:47
 */
public interface SysDeptService {
	
	SysDeptEntity queryObject(Long deptId);
	
	List<SysDeptEntity> queryList(Map<String, Object> map);

	void save(SysDeptEntity sysDept);
	
	void update(SysDeptEntity sysDept);
	
	void delete(Long deptId);

	/**
	 * 查询子部门ID列表
	 * @param parentId  上级部门ID
	 */
	List<Long> queryDetpIdList(Long parentId);

	/**
	 * 获取子部门ID(包含本部门ID)，用于数据过滤
	 */
	String getSubDeptIdList(Long deptId);

	/**
	 * 封装部门的树形结构
	 * zhoukai7
	 * @return
	 */
	Map<Long, String> encapsulationDeptTree(int deptId, Long roleIds);

	/**
	 * 市场部门树形结构
	 * zhoukai7
	 * @param roleIds
	 * @return
	 */
	Map<Long, String> mktDeptTree(Long roleIds);

}
