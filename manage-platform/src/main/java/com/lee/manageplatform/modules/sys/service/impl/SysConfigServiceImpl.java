package com.lee.manageplatform.modules.sys.service.impl;

import com.google.gson.Gson;
import com.lee.manageplatform.common.exception.RRException;
import com.lee.manageplatform.modules.sys.dao.SysConfigDao;
import com.lee.manageplatform.modules.sys.entity.SysConfigEntity;
import com.lee.manageplatform.modules.sys.service.SysConfigService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("sysConfigService")
public class SysConfigServiceImpl implements SysConfigService {
	@Autowired
	private SysConfigDao sysConfigDao;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(SysConfigEntity config) {
		sysConfigDao.save(config);
	}

	@Override
	public void update(SysConfigEntity config) {
	    config.setUpdateTime(new Date());
		sysConfigDao.update(config);
	}

	@Override
	public void updateValueByKey(String key, String value) {
		sysConfigDao.updateValueByKey(key, value);
	}

	@Override
	public void deleteBatch(Long[] ids) {
		sysConfigDao.deleteBatch(ids);
	}

	@Override
	public List<SysConfigEntity> queryList(Map<String, Object> map) {
		return sysConfigDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysConfigDao.queryTotal(map);
	}

	@Override
	public SysConfigEntity queryObject(Long id) {
		return sysConfigDao.queryObject(id);
	}

	@Override
	public String getValue(String key, String defaultValue) {
		String value = sysConfigDao.queryByKey(key);
		if(StringUtils.isBlank(value)){
			return defaultValue;
		}
		return value;
	}
	
	@Override
	public <T> T getConfigObject(String key, Class<T> clazz) {
		String value = getValue(key, null);
		if(StringUtils.isNotBlank(value)){
			return new Gson().fromJson(value, clazz);
		}

		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RRException("获取参数失败");
		}
	}

    /**
     * 根据type，获取配置信息列表
     * @author hanpeng
     * @date 2017-10-13
     */
    @Override
    public Map<String, SysConfigEntity> queryByType(Integer type) {
        return sysConfigDao.selectByType(type);
    }

}
