package com.lee.manageplatform.modules.sys.dao;

import com.lee.manageplatform.modules.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 * @author hanpeng
 * @date 2016年9月18日 上午9:34:11
 */
@Mapper
public interface SysUserDao extends BaseDao<SysUserEntity> {
	
	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> queryAllPerms(Long userId);

	/**
	 * 查询用户的部门和角色信息
	 * zhoukai7
	 * @param userId
	 * @return
	 */
	List<SysUserEntity> queryRoleById(Long userId);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);
	
	/**
	 * 根据用户名，查询系统用户
	 */
	SysUserEntity queryByUserName(String userName);
	
	/**
	 * 修改密码
	 */
	int updatePassword(Map<String, Object> map);

	/**
	 * 根据账号，查询系统用户
	 */
	SysUserEntity queryByUserAccount(String userAccount);
}
