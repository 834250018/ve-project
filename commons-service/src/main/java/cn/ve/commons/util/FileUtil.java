package cn.ve.commons.util;

import cn.ve.base.pojo.VeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author ve
 * @date 2022/2/23
 */
@Slf4j
public class FileUtil {

    private static final String TMP_SUFFIX = ".tmp";

    public static File getTempFile(MultipartFile multipartFile) {
        File tempFile;
        try {
            tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), TMP_SUFFIX);
            multipartFile.transferTo(tempFile);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new VeException(400, "xxx");
        }
        return tempFile;
    }

}
