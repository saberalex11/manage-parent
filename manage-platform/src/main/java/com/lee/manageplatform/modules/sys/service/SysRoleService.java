package com.lee.manageplatform.modules.sys.service;


import com.lee.manageplatform.modules.sys.entity.SysRoleEntity;

import java.util.List;
import java.util.Map;


/**
 * 角色
 *
 * @author hanpeng
 * @date 2016年9月18日 上午9:42:52
 */
public interface SysRoleService {
	
	SysRoleEntity queryObject(Long roleId);
	
	List<SysRoleEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SysRoleEntity role);
	
	void update(SysRoleEntity role);
	
	void deleteBatch(Long[] roleIds);

    /**
     * 根据部门id查询部门所拥有的角色信息
     *
     * @author hanpeng
     * @date 2017-09-11
     */
    List<SysRoleEntity> queryByDeptId(Integer deptId, Integer userId);

}
