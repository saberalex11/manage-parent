package com.lee.manageplatform.modules.sys.service.impl;

import com.lee.manageplatform.common.utils.Constant;
import com.lee.manageplatform.modules.sys.dao.SysMenuDao;
import com.lee.manageplatform.modules.sys.dao.SysUserDao;
import com.lee.manageplatform.modules.sys.dao.SysUserTokenDao;
import com.lee.manageplatform.modules.sys.entity.SysMenuEntity;
import com.lee.manageplatform.modules.sys.entity.SysUserEntity;
import com.lee.manageplatform.modules.sys.entity.SysUserTokenEntity;
import com.lee.manageplatform.modules.sys.service.ShiroService;
import com.lee.manageplatform.modules.sys.service.SysUserRoleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShiroServiceImpl implements ShiroService {
    @Autowired
    private SysMenuDao sysMenuDao;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysUserTokenDao sysUserTokenDao;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public Set<String> getUserPermissions(long userId) {
        List<String> permsList;

        //系统管理员，拥有最高权限
        if(userId == Constant.SUPER_ADMIN){
            List<SysMenuEntity> menuList = sysMenuDao.queryList(new HashMap<>());
            permsList = new ArrayList<>(menuList.size());
            for(SysMenuEntity menu : menuList){
                permsList.add(menu.getPerms());
            }
        }else{
            permsList = sysUserDao.queryAllPerms(userId);
        }
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for(String perms : permsList){
            if(StringUtils.isBlank(perms)){
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }

    @Override
    public SysUserTokenEntity queryByToken(String token) {
        return sysUserTokenDao.queryByToken(token);
    }

    @Override
    public SysUserEntity queryUser(Long userId) {
        return sysUserDao.queryObject(userId);
    }

	@Override
	public Set<String> getUserRoles(long userId) {
		return sysUserRoleService.queryRoleIdList(userId)
			.stream()
			.map(r -> r.toString())
			.collect(Collectors.toSet());
	}
}
