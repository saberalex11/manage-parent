package com.lee.manageplatform.modules.sys.service.impl;

import com.lee.manageplatform.common.enums.EnStatus;
import com.lee.manageplatform.common.utils.StringUtil;
import com.lee.manageplatform.modules.sys.dao.SysUserDao;
import com.lee.manageplatform.modules.sys.entity.SysUserEntity;
import com.lee.manageplatform.modules.sys.service.SysRoleService;
import com.lee.manageplatform.modules.sys.service.SysUserRoleService;
import com.lee.manageplatform.modules.sys.service.SysUserService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 系统用户
 *
 * @author hanpeng
 * @date 2016年9月18日 上午9:46:09
 */
@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysRoleService sysRoleService;

	@Override
	public List<String> queryAllPerms(Long userId) {
		return sysUserDao.queryAllPerms(userId);
	}

	@Override
	public List<Long> queryAllMenuId(Long userId) {
		return sysUserDao.queryAllMenuId(userId);
	}

	@Override
	public SysUserEntity queryByUserName(String userName) {
		return sysUserDao.queryByUserName(userName);
	}
	
	@Override
	public SysUserEntity queryObject(Long userId) {
		return sysUserDao.queryObject(userId);
	}

	@Override
	public List<SysUserEntity> queryList(Map<String, Object> map){
        List<SysUserEntity> list = sysUserDao.queryList(map);
        for (SysUserEntity sysUserEntity:list) {
            Integer operator = StringUtil.isNotEmpty(sysUserEntity.getUpdateId())?sysUserEntity.getUpdateId():sysUserEntity.getCreateId();
            sysUserEntity.setOperator(this.queryObject(operator.longValue()).getUserName());
        }
		return list;
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysUserDao.queryTotal(map);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(SysUserEntity user) {
		user.setCreateTime(new Date());
		//sha256加密
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
		user.setSalt(salt);
        user.setStatus(EnStatus.有效.getValue());
		sysUserDao.save(user);
		
		//保存用户与角色关系
//		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(SysUserEntity user) {
        user.setUpdateTime(new Date());
        if (StringUtils.isBlank(user.getPassword())) {
            user.setPassword(null);
        }else{
            //从数据库获取密码加密的salt
            SysUserEntity sysUserEntity = this.queryObject(user.getUserId());
			user.setPassword(new Sha256Hash(user.getPassword().trim(), sysUserEntity.getSalt()).toHex());
		}
		sysUserDao.update(user);
		
		//保存用户与角色关系
//		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] userId) {
		sysUserDao.deleteBatch(userId);
	}

	@Override
	public int updatePassword(Long userId, String password, String newPassword) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("password", password);
		map.put("newPassword", newPassword);
		return sysUserDao.updatePassword(map);
	}

	@Override
	public SysUserEntity queryByUserAccount(String userAccount) {
		return sysUserDao.queryByUserAccount(userAccount);
	}

	/**
	 * 查询用户和角色信息
	 * zhoukai7
	 * @param userId
	 * @return
	 */
	@Override
	public List<SysUserEntity> queryRoleById(Long userId){
		return sysUserDao.queryRoleById(userId);
	}



}
