package com.lee.manageplatform.common.annotation;

import java.lang.annotation.*;

/**
 * Description:
 *
 * @author: HANP
 * @date: 2017-10-11
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TokenIgnore {
}
