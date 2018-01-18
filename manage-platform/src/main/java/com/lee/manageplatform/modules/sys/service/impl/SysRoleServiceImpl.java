package com.lee.manageplatform.modules.sys.service.impl;

import com.lee.manageplatform.common.utils.StringUtil;
import com.lee.manageplatform.modules.sys.dao.SysRoleDao;
import com.lee.manageplatform.modules.sys.entity.SysRoleEntity;
import com.lee.manageplatform.modules.sys.service.SysRoleDeptService;
import com.lee.manageplatform.modules.sys.service.SysRoleService;
import com.lee.manageplatform.modules.sys.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * 角色
 *
 * @author hanpeng
 * @date 2016年9月18日 上午9:45:12
 */
@Service("sysRoleService")
public class SysRoleServiceImpl implements SysRoleService {
	@Autowired
	private SysRoleDao sysRoleDao;
	@Autowired
	private SysRoleDeptService sysRoleDeptService;
	@Autowired
	private SysUserRoleService sysUserRoleService;

	@Override
	public SysRoleEntity queryObject(Long roleId) {
		return sysRoleDao.queryObject(roleId);
	}

	@Override
	public List<SysRoleEntity> queryList(Map<String, Object> map) {
        Integer deptId = StringUtil.isEmpty(map.get("deptId"))?null:Integer.parseInt(map.get("deptId").toString()) ;
        Long userId = StringUtil.isEmpty(map.get("userId"))?null:Long.parseLong(map.get("userId").toString()) ;

        List<SysRoleEntity> roleList = sysRoleDao.queryList(map) ;
        List<Long> roleIdList = new ArrayList<>();
        if (deptId!=null){
            roleIdList.addAll(sysRoleDeptService.queryRoleIdListByDeptId(deptId));
        }
        if (userId!=null){
            roleIdList.addAll(sysUserRoleService.queryRoleIdList(userId));
        }
        if (!roleIdList.isEmpty()){
            for (SysRoleEntity role:roleList){
                if (roleIdList.contains(role.getRoleId())){
                    role.setLAY_CHECKED(true);
                }
            }
        }
		return roleList;
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysRoleDao.queryTotal(map);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(SysRoleEntity role) {
		role.setCreateTime(new Date());
		sysRoleDao.save(role);
		
		//保存角色与菜单关系
//		sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());

		//保存角色与部门关系
//		sysRoleDeptService.saveOrUpdate(role.getRoleId(), role.getDeptIdList());
    }

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(SysRoleEntity role) {
        role.setUpdateTime(new Date());
        sysRoleDao.update(role);

        //更新角色与菜单关系
//		sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());

		//保存角色与部门关系
//		sysRoleDeptService.saveOrUpdate(role.getRoleId(), role.getDeptIdList());

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] roleIds) {
		sysRoleDao.deleteBatch(roleIds);
	}

    /**
     * 根据部门id查询部门所拥有的角色信息
     *
     * @param deptId
     * @author hanpeng
     * @date 2017-09-11
     */
    @Override
    public List<SysRoleEntity> queryByDeptId(Integer deptId,Integer userId) {
        Map<String,Object> parames = new HashMap<>(16);
        parames.put("deptId",deptId);
        //查询整个部门的权限
        List<SysRoleEntity> roleList = sysRoleDao.selectByDeptId(parames);
        List<Long> roleIdList = new ArrayList<>();
        //查询这个人的权限
        roleIdList.addAll(sysUserRoleService.queryRoleIdList(userId.longValue()));
        if (!roleIdList.isEmpty()){
            for (SysRoleEntity role:roleList){
                if (roleIdList.contains(role.getRoleId())){
                    role.setLAY_CHECKED(true);
                }
            }
        }
        return roleList;
    }

}
