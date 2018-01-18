package com.lee.manageplatform.modules.sys.service.impl;

import com.lee.manageplatform.common.utils.Constant;
import com.lee.manageplatform.modules.sys.dao.SysMenuDao;
import com.lee.manageplatform.modules.sys.entity.SysMenuEntity;
import com.lee.manageplatform.modules.sys.service.ShiroService;
import com.lee.manageplatform.modules.sys.service.SysMenuService;
import com.lee.manageplatform.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service("sysMenuService")
public class SysMenuServiceImpl implements SysMenuService {

	@Autowired
	private SysMenuDao sysMenuDao;
	@Autowired
	private SysUserService sysUserService;
    @Autowired
    private ShiroService shiroService;

    /**
     * 根据父菜单，查询子菜单
     *
     * @param parentId 父菜单ID
     */
    @Override
    public List<SysMenuEntity> queryListParentId(Long parentId) {
        return sysMenuDao.queryListParentId(parentId);
    }

    /**
     * 根据parentId查询用户所拥有的菜单
     *
     * @param userId
     * @param parentId
     * @author hanpeng
     * @date 2017-09-23 23:42
     */
    @Override
    public List<SysMenuEntity> queryByParentIdAndUserId(Long userId, Long parentId) {
        //系统管理员，拥有最高权限,获取所有菜单
        if(userId == Constant.SUPER_ADMIN){
            return queryListParentId(parentId);
        }
        Map<String,Object> parames = new HashMap<>();
        parames.put("userId",userId);
        parames.put("parentId",parentId);
        return sysMenuDao.queryByParentIdAndUserId(parames);
    }

    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     * @author hanp
     * @date 2017-09-23 23:30
     */
    @Override
	public List<SysMenuEntity> querySubMenuList(Long userId,Long parentId) {

        //二级菜单
        List<SysMenuEntity> list = this.queryByParentIdAndUserId(userId,parentId);

        if(null != list && !list.isEmpty()){

            List<SysMenuEntity> subMenuList = new ArrayList<>();

            for (SysMenuEntity menu : list) {
                SysMenuEntity subMenu = new SysMenuEntity();
                subMenu.setName(menu.getName());
                subMenu.setIcon(menu.getIcon());
                subMenu.setUrl(menu.getUrl());

                //三级菜单
                List<SysMenuEntity> multiMenuList = this.queryByParentIdAndUserId(userId,menu.getMenuId());
                if(null != multiMenuList && !multiMenuList.isEmpty()) {
                    List<SysMenuEntity> childrens = new ArrayList<>();
                    for (SysMenuEntity menu2 : multiMenuList) {

                        SysMenuEntity children = new SysMenuEntity();
                        children.setName(menu2.getName());
                        children.setIcon(menu2.getIcon());
                        children.setUrl(menu2.getUrl());
                        childrens.add(children);
                    }
                    subMenu.setList(childrens);
                }
                subMenuList.add(subMenu);

            }
            return subMenuList;

        }
        return null;
	}

	@Override
	public List<SysMenuEntity> queryNotButtonList() {
		return sysMenuDao.queryNotButtonList();
	}
	
	@Override
	public SysMenuEntity queryObject(Long menuId) {
		return sysMenuDao.queryObject(menuId);
	}

	@Override
	public List<SysMenuEntity> queryList(Map<String, Object> map) {
		return sysMenuDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysMenuDao.queryTotal(map);
	}

	@Override
	public void save(SysMenuEntity menu) {
        if (menu.getLevel() == null){
            menu.setLevel(-1);
        }
		sysMenuDao.save(menu);
	}

	@Override
	public void update(SysMenuEntity menu) {
		sysMenuDao.update(menu);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] menuIds) {
		sysMenuDao.deleteBatch(menuIds);
	}

    /**
     * 获取用户权限下的所有一级菜单
     * @param userId
     * @date 2017-09-23 23:22
     * @author hanp
     */
	@Override
	public List<SysMenuEntity> queryUserList(Long userId) {
        //系统管理员，拥有最高权限,获取所有菜单
        if(userId == Constant.SUPER_ADMIN){
            return queryListParentId(0L);
        }

        //用户菜单列表
		return sysMenuDao.selectUserList(userId);
	}

	@Override
    public boolean checkPermissions(String permissionskey, Long userId) {
        Set<String> permissions = shiroService.getUserPermissions(userId);
        return permissions.contains(permissionskey);
    }

    /**
     * 根据菜单级别 类型和id查询可选择的父级菜单
     *
     * @param type
     * @param Level
     * @param menuId
     * @author hanpeng
     * @date 2017-09-12
     */
    @Override
    public List<SysMenuEntity> queryParentMenu(Integer type, Integer Level, Integer menuId) {
        Map<String, Object> parames = new HashMap<>();
        parames.put("menuId", menuId);
        //1.菜单类型为1-菜单，菜单级别为1级菜单 父级菜单为空
        if (type.equals(Constant.MenuType.MENU.getValue()) && Level.equals(Constant.MenuLevel.FIRST.getValue())) {
            return null;
        }
        //2.菜单类型为1-菜单，菜单级别为2级菜单 查询1级父级菜单
        if (type.equals(Constant.MenuType.MENU.getValue()) && Level.equals(Constant.MenuLevel.SECOND.getValue())) {
            parames.put("type", Constant.MenuType.MENU.getValue());
            parames.put("level", Constant.MenuLevel.FIRST.getValue());
        }
        //3.菜单类型为1-菜单，菜单级别为3级菜单 查询2级父级菜单
        if (type.equals(Constant.MenuType.MENU.getValue()) && Level.equals(Constant.MenuLevel.THIRD.getValue())) {
            parames.put("type", Constant.MenuType.MENU.getValue());
            parames.put("level", Constant.MenuLevel.SECOND.getValue());
        }
        //4.菜单类型为2-按钮，菜单级别为3级菜单 查询3级菜单
        if (type.equals(Constant.MenuType.BUTTON.getValue()) && Level.equals(Constant.MenuLevel.THIRD.getValue())) {
            parames.put("type", Constant.MenuType.MENU.getValue());
            parames.put("level", Constant.MenuLevel.THIRD.getValue());
        }
        return sysMenuDao.selectByTypeAndLevel(parames);
    }

}
