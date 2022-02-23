package cn.ve.commons.util;

import cn.ve.base.pojo.VeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @author ve
 * @date 2022/2/23
 */
@Slf4j
public class FileUtil {

    private static final String TMP_SUFFIX = ".tmp";

    public static File getFile(MultipartFile multipartFile) {
        File tempFile = genTempFile(multipartFile.getName());
        try {
            multipartFile.transferTo(tempFile);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new VeException(400, "xxx");
        }
        return tempFile;
    }

    public static InputStream getInputStream(MultipartFile multipartFile) {
        try {
            return multipartFile.getInputStream();
        } catch (IOException e) {
            log.error("文件转换异常【{}】", e.getMessage(), e);
            throw new VeException("文件转换异常");
        }
    }

    public static File getBytes(byte[] data) {
        File source = genTempFile();
        try {
            OutputStream os = new FileOutputStream(source);
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(os);
            bufferedOutput.write(data);
            bufferedOutput.close();
            os.close();
        } catch (Exception e) {
            log.error("文件转换异常【{}】", e.getMessage(), e);
            throw new VeException("文件转换异常");
        }
        return source;
    }

    public static InputStream getInputStream(byte[] data) {
        return new ByteArrayInputStream(data);
    }

    public static File getFile(InputStream is) {
        File source = genTempFile();
        try {
            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            FileOutputStream fos = new FileOutputStream(source);
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            fos.close();
            is.close();
        } catch (Exception e) {
            log.error("文件转换异常【{}】", e.getMessage(), e);
            throw new VeException("文件转换异常");
        }
        return source;
    }

    public static File genTempFile(String fileName) {
        try {
            return File.createTempFile(String.valueOf(System.currentTimeMillis()), fileName);
        } catch (IOException e) {
            log.error("创建缓存文件异常【{}】", e.getMessage(), e);
            throw new VeException("创建缓存文件异常");
        }
    }

    public static File genTempFile() {
        try {
            return File.createTempFile(String.valueOf(System.currentTimeMillis()), TMP_SUFFIX);
        } catch (IOException e) {
            log.error("创建缓存文件异常【{}】", e.getMessage(), e);
            throw new VeException("创建缓存文件异常");
        }
    }

    public static InputStream getInputStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            log.error("文件转换异常【{}】", e.getMessage(), e);
            throw new VeException("文件转换异常");
        }
    }

}
