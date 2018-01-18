package com.lee.manageplatform.modules.sys.service;

import java.util.List;


/**
 * 角色与部门对应关系
 *
 * @author hanpeng
 * @date 2017年6月21日 23:42:30
 */
public interface SysRoleDeptService {
	
	void saveOrUpdate(Long roleId, List<Long> deptIdList);
	
	/**
	 * 根据部门ID，获取角色ID列表
     * @author hanp
     * @date 2017-09-21
	 */
	List<Long> queryRoleIdListByDeptId(Integer deptId);
	
}
