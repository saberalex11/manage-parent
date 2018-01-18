package com.lee.manageplatform.modules.sys.service.impl;

import com.lee.manageplatform.modules.sys.dao.SysRoleDeptDao;
import com.lee.manageplatform.modules.sys.service.SysRoleDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 角色与部门对应关系
 *
 * @author hanpeng
 * @date 2017年6月21日 23:42:30
 */
@Service("sysRoleDeptService")
public class SysRoleDeptServiceImpl implements SysRoleDeptService {
	@Autowired
	private SysRoleDeptDao sysRoleDeptDao;

	@Override
	@Transactional
	public void saveOrUpdate(Long deptId, List<Long> roleIdList) {
		//先删除部门与角色关系
		sysRoleDeptDao.delete(deptId);

		if(roleIdList.size() == 0){
			return ;
		}

		//保存部门与角色关系
		Map<String, Object> map = new HashMap<>();
		map.put("deptId", deptId);
		map.put("roleIdList", roleIdList);
		sysRoleDeptDao.save(map);
	}

	@Override
	public List<Long> queryRoleIdListByDeptId(Integer deptId) {
		return sysRoleDeptDao.selectRoleIdListByDeptId(deptId);
	}

}
