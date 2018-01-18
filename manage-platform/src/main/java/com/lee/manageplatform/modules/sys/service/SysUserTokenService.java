package com.lee.manageplatform.modules.sys.service;


import com.lee.manageplatform.common.entity.R;
import com.lee.manageplatform.modules.sys.entity.SysUserTokenEntity;

/**
 * 用户Token
 *
 * @author hanpeng
 * @date 2017-03-23 15:22:07
 */
public interface SysUserTokenService {

	SysUserTokenEntity queryByUserId(Long userId);

	SysUserTokenEntity queryByToken(String token);
	
	void save(SysUserTokenEntity token);
	
	void update(SysUserTokenEntity token);

	/**
	 * 生成token
	 * @param userId  用户ID
	 */
	R createToken(long userId);

}
