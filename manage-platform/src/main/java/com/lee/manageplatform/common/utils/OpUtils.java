package com.lee.manageplatform.common.utils;

import com.lee.manageplatform.modules.sys.entity.SysUserEntity;
import org.apache.shiro.SecurityUtils;

public class OpUtils {
    public static SysUserEntity getUser() {
        return (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
    }

    public static Long getUserId() {
        return getUser().getUserId();
    }

    public static Long getDeptId() {
        return getUser().getDeptId();
    }
}
