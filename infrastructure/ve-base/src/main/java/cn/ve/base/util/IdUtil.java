package cn.ve.base.util;

import cn.hutool.core.lang.UUID;

/**
 * @author ve
 * @date 2022/2/23
 */
public class IdUtil {

    public static String genUUID() {
        return UUID.randomUUID().toString().replaceAll(StringConstant.DASHED, "");
    }

}
