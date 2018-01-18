package com.lee.manageplatform.modules.sys.controller;

import com.alibaba.fastjson.JSON;
import com.lee.manageplatform.common.annotation.SysLog;
import com.lee.manageplatform.common.entity.R;
import com.lee.manageplatform.common.utils.Constant;
import com.lee.manageplatform.common.utils.Query;
import com.lee.manageplatform.common.utils.StringUtil;
import com.lee.manageplatform.common.validator.Assert;
import com.lee.manageplatform.common.validator.ValidatorUtils;
import com.lee.manageplatform.common.validator.group.AddGroup;
import com.lee.manageplatform.common.validator.group.UpdateGroup;
import com.lee.manageplatform.modules.sys.entity.SysUserEntity;
import com.lee.manageplatform.modules.sys.service.SysUserRoleService;
import com.lee.manageplatform.modules.sys.service.SysUserService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 * @author hanpeng
 * @date 2016年10月31日 上午10:40:10
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	
	/**
	 * 所有用户列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:user:list")
	public R list(@RequestParam Map<String, Object> params){
		//只有超级管理员，才能查看所有管理员列表
		if(getUserId() != Constant.SUPER_ADMIN){
			params.put("createUserId", getUserId());
		}
		
		//查询列表数据
		Query query = new Query(params);
		List<SysUserEntity> userList = sysUserService.queryList(query);
		int total = sysUserService.queryTotal(query);
		
//		PageUtils pageUtil = new PageUtils(userList, total, query.getLimit(), query.getPage());

		return R.ok().put("data", userList).put("count",total);
	}
	
	/**
	 * 获取登录的用户信息
	 */
	@RequestMapping("/info")
	public R info(){
        logger.info("当前登录的用户信息：：："+ JSON.toJSONString(getUser()));
		return R.ok().put("user", getUser());
	}
	
	/**
	 * 修改登录用户密码
	 */
	@SysLog("修改密码")
	@RequestMapping("/changePassword")
	public R changePassword(@RequestBody Map<String,Object> parames){

        String password = StringUtil.isEmpty(parames.get("password"))?null:parames.get("password").toString();
        String newPassword = StringUtil.isEmpty(parames.get("newPassword"))?null:parames.get("newPassword").toString();

		Assert.isBlank(newPassword, "新密码不为能空");

		//sha256加密
		password = new Sha256Hash(password, getUser().getSalt()).toHex();
		//sha256加密
		newPassword = new Sha256Hash(newPassword, getUser().getSalt()).toHex();
				
		//更新密码
		int count = sysUserService.updatePassword(getUserId(), password, newPassword);
		if(count == 0){
			return R.error("原密码不正确");
		}
		
		return R.ok();
	}
	
	/**
	 * 用户角色信息列表
	 */
	@RequestMapping("/info/{userId}")
	@RequiresPermissions("sys:user:info")
	public R info(@PathVariable("userId") Long userId){
		SysUserEntity user = sysUserService.queryObject(userId);
		
		//获取用户所属的角色列表
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
		user.setRoleIdList(roleIdList);
		
		return R.ok().put("data", user);
	}
	
	/**
	 * 保存用户
	 */
	@SysLog("保存用户")
	@RequestMapping("/save")
	@RequiresPermissions("sys:user:save")
	public R save(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, AddGroup.class);

        user.setCreateId(getUserId().intValue());
        sysUserService.save(user);

        return R.ok();
    }
	
	/**
	 * 修改用户
	 */
	@SysLog("修改用户")
	@RequestMapping("/update")
	@RequiresPermissions("sys:user:update")
	public R update(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, UpdateGroup.class);

        user.setUpdateId(getUserId().intValue());
        sysUserService.update(user);

        return R.ok();
    }
	
	/**
	 * 删除用户
	 */
	@SysLog("删除用户")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public R delete(@RequestBody Long[] userIds){
		if(ArrayUtils.contains(userIds, 1L)){
			return R.error("系统管理员不能删除");
		}
		
		if(ArrayUtils.contains(userIds, getUserId())){
			return R.error("当前用户不能删除");
		}
		
		sysUserService.deleteBatch(userIds);
		
		return R.ok();
	}

    /**
     * 解锁
     * @param parames 输入的密码 password
     * @author hanpeng
     * @date 2017-10-27
     */
    @RequestMapping(value = "/unlock", method = RequestMethod.POST)
    public R unlock(@RequestBody Map<String,Object> parames)throws IOException {

        String password = StringUtil.isEmpty(parames.get("password"))?null:parames.get("password").toString();

        if (password == null){
            return R.error("请输入密码再解锁！");
        }

        //用户信息
        SysUserEntity user = getUser();

        //  登录过期、
        if (user == null){
          return R.error('3',"登录失效");
        }

        //密码错误
        if(!user.getPassword().equals(new Sha256Hash(password, user.getSalt()).toHex())) {
            return R.error("密码错误，请重新输入！");
        }

        return R.ok();
    }

}
