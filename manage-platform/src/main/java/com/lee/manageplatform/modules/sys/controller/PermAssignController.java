package com.lee.manageplatform.modules.sys.controller;

import com.alibaba.fastjson.JSON;
import com.lee.manageplatform.common.annotation.SysLog;
import com.lee.manageplatform.common.entity.R;
import com.lee.manageplatform.modules.sys.service.SysRoleDeptService;
import com.lee.manageplatform.modules.sys.service.SysRoleMenuService;
import com.lee.manageplatform.modules.sys.service.SysUserRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Description:角色资源、角色部门、用户角色分配
 *
 * @author: HANP
 * @date: 2017-09-20
 */
@RestController
@RequestMapping("/sys/permAssign")
public class PermAssignController extends AbstractController {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Autowired
    private SysRoleDeptService sysRoleDeptService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 保存角色资源关系
     * @param map
     * @author hanp
     * @date  2017-09-20
     */
    @SysLog("保存角色资源关系")
    @RequestMapping("/roleMenuSave")
    @RequiresPermissions("sys:permAssign:roleMenuSave")
    public R roleMenuSave(@RequestBody Map<String,Object> map) {

        Long roleId = map.get("roleId") == null?null:Long.parseLong(map.get("roleId").toString());
        String menu = map.get("menuIdList") == null?null:map.get("menuIdList").toString();
        List<Long> menuIdList = JSON.parseArray(menu,Long.class);
        //保存角色与菜单关系
        sysRoleMenuService.saveOrUpdate(roleId,menuIdList);
        return R.ok();
    }

    /**
     * 保存角色部门关系
     * @param map
     * @author hanp
     * @date  2017-09-20
     */
    @SysLog("保存部门角色关系")
    @RequestMapping("/deptRoleSave")
    @RequiresPermissions("sys:permAssign:deptRoleSave")
    public R deptRoleSave(@RequestBody Map<String,Object> map) {

        Long deptId = map.get("deptId") == null?null:Long.parseLong(map.get("deptId").toString());
        String role = map.get("roleIdList") == null?null:map.get("roleIdList").toString();
        List<Long> roleIdList = JSON.parseArray(role,Long.class);
        //保存角色与部门关系
        sysRoleDeptService.saveOrUpdate(deptId,roleIdList);

        return R.ok();
    }

    /**
     * 保存用户角色关系
     * @param map
     * @author hanp
     * @date  2017-09-20
     */
    @SysLog("保存用户角色关系")
    @RequestMapping("/userRoleSave")
    @RequiresPermissions("sys:permAssign:userRoleSave")
    public R userRoleSave(@RequestBody Map<String,Object> map) {

        Long userId = map.get("userId") == null?null:Long.parseLong(map.get("userId").toString());
        String role = map.get("roleIdList") == null?null:map.get("roleIdList").toString();
        List<Long> roleIdList = JSON.parseArray(role,Long.class);
        //保存角色与部门关系
        sysUserRoleService.saveOrUpdate(userId,roleIdList);

        return R.ok();
    }


}
