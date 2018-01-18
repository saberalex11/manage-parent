package com.lee.manageplatform.common.enums;

/**
 * 首页推荐类型的枚举类
 * @author wangyl
 * @date 2017-11-14 10:20
 */
public enum EnRecommend {

    //产品推荐 、解决方案推荐、终端推荐、列表推荐
    PRODUCT((short)1),SOLUTIONS((short)2),TERMINAL((short)1),LIST((short)2);
    //成员变量
    private short value;
    //构造器
    private EnRecommend(short value) {
        this.value = value;
    }

    /**
     * 获取参数
     * @return
     */
    public short getValue() {
        return value;
    }

    /**
     * 获取
     * @param value
     * @return
     */
    public static EnRecommend get(short value){
        for(EnRecommend t:EnRecommend.values()){
            if(t.getValue()==value){
                return t;
            }
        }
        return null;
    }
}
