package cn.ve.base.util;

import cn.ve.base.constant.SqlFilter;

import java.lang.reflect.Method;

/**
 * @author ve
 * @date 2021/9/2
 */
public interface MybatisUtils {

    /**
     * 通过statementId反射出SqlFilter
     *
     * @param id
     * @return
     */
    static SqlFilter getAnnotation(String id) {
        if (id == null) {
            return null;
        }
        if (id.contains("_COUNT")) {
            id = id.replace("_COUNT", "");
        }
        String className = id.substring(0, id.lastIndexOf(StringConstant.DOT));
        String methodName = id.substring(id.lastIndexOf(StringConstant.DOT) + 1);
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
        for (Method method : clazz.getMethods()) {
            if (method.getName().equals(methodName) && method.isAnnotationPresent(SqlFilter.class)) {
                return method.getAnnotation(SqlFilter.class);
            }
        }
        if (clazz.isAnnotationPresent(SqlFilter.class)) {
            return clazz.getAnnotation(SqlFilter.class);
        }
        return null;
    }

}
