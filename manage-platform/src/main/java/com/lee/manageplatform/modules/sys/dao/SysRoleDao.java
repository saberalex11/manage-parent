package com.lee.manageplatform.modules.sys.dao;

import com.lee.manageplatform.modules.sys.entity.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 * @author hanpeng
 * @date 2016年9月18日 上午9:33:33
 */
@Mapper
public interface SysRoleDao extends BaseDao<SysRoleEntity> {

    /**
     * 根据部门id查询部门所拥有的角色信息
     *
     * deptId 部门id roleType 角色类型
     * @author hanpeng
     * @date 2017-09-11
     */
    public List<SysRoleEntity> selectByDeptId(Map<String, Object> map);

}
