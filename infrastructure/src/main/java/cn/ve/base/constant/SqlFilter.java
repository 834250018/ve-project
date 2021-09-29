package cn.ve.base.constant;

import java.lang.annotation.*;

/**
 * @author ve
 * @date 2021/9/2
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SqlFilter {
    boolean deletedIgnore();
}
