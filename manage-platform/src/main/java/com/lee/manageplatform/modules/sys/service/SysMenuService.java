package com.lee.manageplatform.modules.sys.service;


import com.lee.manageplatform.modules.sys.entity.SysMenuEntity;

import java.util.List;
import java.util.Map;


/**
 * 菜单管理
 *
 * @author hanpeng
 * @date 2016年9月18日 上午9:42:16
 */
public interface SysMenuService {

	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 */
	List<SysMenuEntity> queryListParentId(Long parentId);

    /**
     * 根据parentId查询用户所拥有的菜单
     *
     * @author hanpeng
     * @date 2017-09-23 23:42
     */
    List<SysMenuEntity> queryByParentIdAndUserId(Long userId, Long parentId);

    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     * @author hanp
     * @date 2017-09-23 23:30
     */
    List<SysMenuEntity> querySubMenuList(Long userId, Long parentId);

	/**
	 * 获取不包含按钮的菜单列表
	 */
	List<SysMenuEntity> queryNotButtonList();
	
	/**
	 * 查询菜单
	 */
	SysMenuEntity queryObject(Long menuId);
	
	/**
	 * 查询菜单列表
	 */
	List<SysMenuEntity> queryList(Map<String, Object> map);
	
	/**
	 * 查询总数
	 */
	int queryTotal(Map<String, Object> map);
	
	/**
	 * 保存菜单
	 */
	void save(SysMenuEntity menu);
	
	/**
	 * 修改
	 */
	void update(SysMenuEntity menu);
	
	/**
	 * 删除
	 */
	void deleteBatch(Long[] menuIds);
	
	/**
	 * 查询用户的权限列表
	 */
	List<SysMenuEntity> queryUserList(Long userId);

	/**
	 * 判断用户权限
	 * @return
	 */
	boolean checkPermissions(String permissionskey, Long userId);

    /**
     * 根据菜单级别 类型和id查询可选择的父级菜单
     *
     * @author hanpeng
     * @date 2017-09-12
     */
    List<SysMenuEntity> queryParentMenu(Integer type, Integer Level, Integer menuId);

}
