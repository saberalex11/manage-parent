package com.lee.manageplatform.modules.sys.controller;

import com.lee.manageplatform.common.annotation.SysLog;
import com.lee.manageplatform.common.entity.R;
import com.lee.manageplatform.common.exception.RRException;
import com.lee.manageplatform.common.utils.Constant;
import com.lee.manageplatform.modules.sys.entity.SysMenuEntity;
import com.lee.manageplatform.modules.sys.service.ShiroService;
import com.lee.manageplatform.modules.sys.service.SysMenuService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 系统菜单
 *
 * @author hanpeng
 * @date 2016年10月27日 下午9:58:15
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends AbstractController {

	@Autowired
	private SysMenuService sysMenuService;

    @Autowired
	private ShiroService shiroService;
	
	/**
	 * 所有菜单列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:menu:list")
	public R list(){
		List<SysMenuEntity> menuList = sysMenuService.queryList(new HashMap<>());
        int total = sysMenuService.queryTotal(new HashMap<>());
		return R.ok().put("data",menuList).put("count",total);
	}
	
	/**
	 * 选择菜单(添加、修改菜单)
	 */
	@RequestMapping("/select")
	@RequiresPermissions("sys:menu:select")
	public R select(){
		//查询列表数据
		List<SysMenuEntity> menuList = sysMenuService.queryNotButtonList();
		
		//添加顶级菜单
		SysMenuEntity root = new SysMenuEntity();
		root.setMenuId(0L);
		root.setName("一级菜单");
		root.setParentId(-1L);
		root.setOpen(true);
		menuList.add(root);

        return R.ok().put("data", menuList);
    }

    /**
     * 菜单信息
	 */
	@RequestMapping("/info/{menuId}")
	@RequiresPermissions("sys:menu:info")
	public R info(@PathVariable("menuId") Long menuId){
		SysMenuEntity menu = sysMenuService.queryObject(menuId);
		return R.ok().put("data", menu);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存菜单")
	@RequestMapping("/save")
	@RequiresPermissions("sys:menu:save")
	public R save(@RequestBody SysMenuEntity menu){
		//数据校验
		verifyForm(menu);
		
		sysMenuService.save(menu);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改菜单")
	@RequestMapping("/update")
	@RequiresPermissions("sys:menu:update")
	public R update(@RequestBody SysMenuEntity menu){
		//数据校验
		verifyForm(menu);
				
		sysMenuService.update(menu);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除菜单")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:menu:delete")
	public R delete(long menuId){
		if(menuId <= 31){
			return R.error("系统菜单，不能删除");
		}

		//判断是否有子菜单或按钮
		List<SysMenuEntity> menuList = sysMenuService.queryListParentId(menuId);
        if (!menuList.isEmpty()) {
            return R.error("请先删除子菜单或按钮");
		}

		sysMenuService.deleteBatch(new Long[]{menuId});
		
		return R.ok();
	}
	
	/**
	 * 用户菜单列表
	 */
	@RequestMapping("/getUserMenuList")
	public R user(){
		List<SysMenuEntity> menuList = sysMenuService.queryUserList(getUserId());
		Set<String> permissions = shiroService.getUserPermissions(getUserId());
		return R.ok().put("menuList", menuList).put("permissions", permissions);
	}

    /**
     * 左侧用户菜单列表
     */
    @RequestMapping("/getSubMenuList")
    public R user(@RequestParam Long parentId){
        List<SysMenuEntity> subMenuList = sysMenuService.querySubMenuList(getUserId(),parentId);
        return R.ok().put("subMenuList",subMenuList);
    }

	/**
	 * 验证参数是否正确
	 */
	private void verifyForm(SysMenuEntity menu){
		if(StringUtils.isBlank(menu.getName())){
			throw new RRException("菜单名称不能为空");
		}
		
		if(menu.getParentId() == null){
			throw new RRException("上级菜单不能为空");
		}
		
		//菜单
        if (menu.getType() == Constant.MenuType.MENU.getValue() && StringUtils.isBlank(menu.getUrl())) {
            throw new RRException("菜单URL不能为空");
        }
		
		//上级菜单类型
        int parentType = Constant.MenuType.MENU.getValue();
        if (menu.getParentId() != 0) {
            SysMenuEntity parentMenu = sysMenuService.queryObject(menu.getParentId());
            parentType = parentMenu.getType();
		}
		
		//目录、菜单
        if (menu.getType().intValue() == Constant.MenuType.MENU.getValue() ||
                menu.getType().intValue() == Constant.MenuType.MENU.getValue()) {
            if (parentType != Constant.MenuType.MENU.getValue()) {
                throw new RRException("上级菜单只能为菜单类型");
            }
            return;
        }
		
		//按钮
		if(menu.getType() == Constant.MenuType.BUTTON.getValue()){
			if(parentType != Constant.MenuType.MENU.getValue()){
				throw new RRException("上级菜单只能为菜单类型");
			}
			return ;
		}
	}

    /**
     * 获取父级菜单(添加、修改菜单) depreated
     */
    @RequestMapping("/queryParentMenu")
    @RequiresPermissions("sys:menu:select")
    public R queryParentMenu(@RequestBody Map<String, Object> map) {
        Integer type = StringUtils.isEmpty(map.get("type").toString()) ? null : Integer.parseInt(map.get("type").toString());
        Integer level = StringUtils.isEmpty(map.get("level").toString()) ? null : Integer.parseInt(map.get("level").toString());
        Integer menuId = StringUtils.isEmpty(map.get("menuId").toString()) ? null : Integer.parseInt(map.get("menuId").toString());

        //查询列表数据
        List<SysMenuEntity> menuList = sysMenuService.queryParentMenu(type, level, menuId);

        return R.ok().put("data", menuList);
    }

}
