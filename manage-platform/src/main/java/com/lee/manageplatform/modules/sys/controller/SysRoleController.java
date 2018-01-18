package com.lee.manageplatform.modules.sys.controller;

import com.lee.manageplatform.common.annotation.SysLog;
import com.lee.manageplatform.common.entity.R;
import com.lee.manageplatform.common.utils.Constant;
import com.lee.manageplatform.common.utils.Query;
import com.lee.manageplatform.common.validator.ValidatorUtils;
import com.lee.manageplatform.modules.sys.entity.SysRoleEntity;
import com.lee.manageplatform.modules.sys.service.SysRoleDeptService;
import com.lee.manageplatform.modules.sys.service.SysRoleMenuService;
import com.lee.manageplatform.modules.sys.service.SysRoleService;
import com.lee.manageplatform.modules.sys.service.SysUserRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 * @author hanpeng
 * @date 2016年11月8日 下午2:18:33
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {

	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysRoleDeptService sysRoleDeptService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
	
	/**
	 * 角色列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:role:list")
	public R list(@RequestParam Map<String, Object> params){

		//如果不是超级管理员，则只查询自己创建的角色列表
		if(getUserId() != Constant.SUPER_ADMIN){
			params.put("createUserId", getUserId());
		}
		
		//查询列表数据
		Query query = new Query(params);
//        query.put("roleType", EnRoleType.FUNCTION_ROLE.getValue());
		List<SysRoleEntity> list = sysRoleService.queryList(query);
		int total = sysRoleService.queryTotal(query);

		return R.ok().put("data", list).put("count",total);
	}
	
	/**
	 * 角色列表
	 */
	@RequestMapping("/select")
	@RequiresPermissions("sys:role:select")
	public R select(){
		Map<String, Object> map = new HashMap<>();

		//如果不是超级管理员，则只查询自己所拥有的角色列表
		if(getUserId() != Constant.SUPER_ADMIN){
			map.put("createUserId", getUserId());
		}
		List<SysRoleEntity> list = sysRoleService.queryList(map);
		
		return R.ok().put("list", list);
	}
	
	/**
	 * 角色信息
	 */
	@RequestMapping("/info/{roleId}")
	@RequiresPermissions("sys:role:info")
	public R info(@PathVariable("roleId") Long roleId){
		SysRoleEntity role = sysRoleService.queryObject(roleId);
		
		//查询角色对应的菜单
		List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
		role.setMenuIdList(menuIdList);

		//查询角色对应的部门
//		List<Long> deptIdList = sysRoleDeptService.queryDeptIdList(roleId);
//		role.setDeptIdList(deptIdList);
		
		return R.ok().put("role", role);
	}
	
	/**
	 * 保存角色
	 */
	@SysLog("保存角色")
	@RequestMapping("/save")
	@RequiresPermissions("sys:role:save")
	public R save(@RequestBody SysRoleEntity role){
        role.setCreateId(getUserId().intValue());
		sysRoleService.save(role);
		
		return R.ok();
	}
	
	/**
	 * 修改角色
	 */
	@SysLog("修改角色")
	@RequestMapping("/update")
	@RequiresPermissions("sys:role:update")
	public R update(@RequestBody SysRoleEntity role){
		ValidatorUtils.validateEntity(role);
        role.setUpdateId(getUserId().intValue());
		sysRoleService.update(role);
		
		return R.ok();
	}
	
	/**
	 * 删除角色
	 */
	@SysLog("删除角色")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:role:delete")
	public R delete(@RequestBody Long[] roleIds){
		sysRoleService.deleteBatch(roleIds);
		
		return R.ok();
	}


    /**
     * 根据部门id查询部门所拥有的角色
     *
     * @date 2017-01-11
     * @author hanpeng
     */
    @RequestMapping("/queryByDeptId")
//    @RequiresPermissions("sys:role:queryByDeptId")
    public R queryByDeptId(@RequestParam Integer deptId, Integer userId) {

        List<SysRoleEntity> list = sysRoleService.queryByDeptId(deptId,userId);

        return R.ok().put("data", list);
    }

	/**
	 * 用户角色列表
	 */
	@RequestMapping("/getUserRoleList")
	public R queryCurrentUserRoles() {
		List<Long> roles = sysUserRoleService.queryRoleIdList(getUserId());
		return R.ok().put("roles", roles);
	}

}
