package com.lee.manageplatform.modules.sys.dao;

import com.lee.manageplatform.modules.sys.entity.SysMenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 菜单管理
 *
 * @author hanpeng
 * @date 2016年9月18日 上午9:33:01
 */
@Mapper
public interface SysMenuDao extends BaseDao<SysMenuEntity> {
	
	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 */
	List<SysMenuEntity> queryListParentId(@Param("parentId") Long parentId);
	
	/**
	 * 获取不包含按钮的菜单列表
	 */
	List<SysMenuEntity> queryNotButtonList();
	
	/**
	 * 查询用户的权限列表
	 */
	List<SysMenuEntity> selectUserList(@Param("userId") Long userId);

    /**
     * 根据菜单级别 类型和id查询可选择的父级菜单
     *
     * @author hanpeng
     * @date 2017-09-12
     */
    List<SysMenuEntity> selectByTypeAndLevel(Map<String, Object> map);

    /**
     * 根据parentId查询用户所拥有的菜单
     *
     * @author hanpeng
     * @date 2017-09-23 23:42
     */
    List<SysMenuEntity> queryByParentIdAndUserId(Map<String, Object> map);

	/**
	 * 查询全部权限代码
	 * @author liulh
	 * @return
	 */
	List<SysMenuEntity> queryAll();

}
