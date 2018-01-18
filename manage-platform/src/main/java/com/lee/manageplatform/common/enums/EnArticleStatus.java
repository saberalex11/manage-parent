package com.lee.manageplatform.common.enums;

/**
 * Description:文章状态枚举
 *
 * @author: HANP
 * @date: 2017-10-09
 */
public enum EnArticleStatus {

    无效((short)0),待发布((short)1),已发布((short)2),下线((short)3);


    private EnArticleStatus(short value){
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
    public static EnArticleStatus get(short value){
        for(EnArticleStatus t:EnArticleStatus.values()){
            if(t.getValue()==value){
                return t;
            }
        }
        return null;
    }
}
