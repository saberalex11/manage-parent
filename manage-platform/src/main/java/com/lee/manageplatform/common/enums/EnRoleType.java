package com.lee.manageplatform.common.enums;

/**
 * Description:
 *
 * @author: HANP
 * @date: 2017-11-05
 */
public enum EnRoleType {

    FUNCTION_ROLE("功能角色", 1), DATA_ROLE("数据角色", 2);

    // 成员变量
    private String name;
    private int value;

    // 构造方法
    private EnRoleType(String name, int value) {
        this.name = name;
        this.value = value;
    }

    // 普通方法
    public static String getName(int value) {
        for (EnRoleType c : EnRoleType.values()) {
            if (c.value == value ) {
                return c.name;
            }
        }
        return null;
    }
    // get set 方法
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getValue() {
        return value;
    }
    public void setIndex(int value) {
        this.value = value;
    }

}
