package cn.ve.base.util;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ve
 * @date 2021/8/6
 */
public class BeanUtils {
    public static <T> T copy(Object source, Class<T> tClass, String... ignoreProperties) {
        if (source == null) {
            return null;
        }
        return BeanUtil.copyProperties(source, tClass, ignoreProperties);
    }

    public static <T> List<T> copyList(List<?> sources, Class<T> tClass, String... ignoreProperties) {
        if (sources == null) {
            return new ArrayList<>();
        }
        List<T> result;
        if (sources instanceof Page) {
            Page<?> page = (Page<?>)sources;
            result = new Page<>(page.getPageNum(), page.getPageSize());
            ((Page<T>)result).setTotal(page.getTotal());
        } else {
            result = new ArrayList<>();
        }
        for (Object source : sources) {
            result.add(copy(source, tClass, ignoreProperties));
        }
        return result;
    }
}
