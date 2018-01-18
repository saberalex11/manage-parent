package com.lee.manageplatform.common.enums;

/**
 * 状态枚举
 * @author hanp
 * 2015年12月9日
 */
public enum EnStatus {
	
	无效((short)0),有效((short)1);
	private EnStatus(short value){
		this.value=value;
	}
	
	private short value;
	
	public short getValue() {
		return value;
	}
	
	/**
	 * 获取
	 * @param value
	 * @return
	 */
	public static EnStatus get(short value){
		for(EnStatus t:EnStatus.values()){
			if(t.getValue()==value){
				return t;
			}
		}
		return null;
	}
	
}
