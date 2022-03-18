package cn.ve.feign.config;

import cn.hutool.core.bean.BeanUtil;
import cn.ve.base.pojo.RequestHeader;
import cn.ve.base.pojo.RequestHeaderHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.util.Map;

/**
 * @author ve
 * @date 2022/3/18
 */
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        RequestHeader header = RequestHeaderHolder.get();
        Map<String, Object> map = BeanUtil.beanToMap(header);
        map.forEach((k, v) -> {
            template.header(k, v == null ? null : v.toString());
        });
    }
}
