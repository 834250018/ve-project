package cn.ve.commons.job;

import cn.ve.commons.manager.MinioManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author ve
 * @date 2022/2/23
 */
@Component
@Slf4j
public class XXLJob {

    @Resource
    private MinioManager minioManager;

    //    @XxlJob("demoJobHandler1")
    public void clear() throws Exception {
        minioManager.clearTempBucket();
    }

}
