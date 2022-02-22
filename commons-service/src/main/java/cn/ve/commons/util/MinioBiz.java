package cn.ve.commons.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.ve.base.pojo.FileType;
import cn.ve.base.pojo.VeException;
import cn.ve.file.config.MinioEngine;
import io.minio.GetObjectResponse;
import io.minio.messages.Tags;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author ve
 * @date 2021/7/28
 */
@Slf4j
@Service
public class MinioBiz {

    @Autowired
    private MinioEngine minioEngine;
    @Value("${file.cleanup-expired-days}")
    private Integer cleanupExpiredDays;
    private static final String YYYYMMDD = "yyyyMMdd";
    private static final String TMP_SUFFIX = ".tmp";

    /**
     * 上传公共文件
     *
     * @param multipartFile
     * @return 文件完整url
     */
    public String uploadPublicFile(MultipartFile multipartFile, Boolean watermarkSkip) {
        File tempFile;
        try {
            tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), TMP_SUFFIX);
            multipartFile.transferTo(tempFile);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new VeException(400, "xxx");
        }
        String upload = minioEngine.upload(null, tempFile);
        tempFile.delete();
        return upload;
    }

    /**
     * 上传公共文件
     *
     * @param tempFile
     * @return 文件完整url
     */
    public String uploadPublicFile(File tempFile, Boolean watermarkSkip) {
        String upload = minioEngine.upload(null, tempFile);
        tempFile.delete();
        return upload;
    }

    /**
     * 上传临时文件
     *
     * @return 文件完整url
     */
    public void clearTempBucket() {
        minioEngine.clearTempBucket(DateTime.now().offset(DateField.DAY_OF_YEAR, -cleanupExpiredDays).toJdkDate());
    }

    /**
     * 上传临时文件
     *
     * @param multipartFile
     * @return 文件完整url
     */
    public String uploadTempFile(MultipartFile multipartFile, Boolean watermarkSkip) {
        File tempFile;
        try {
            tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), TMP_SUFFIX);
            multipartFile.transferTo(tempFile);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new VeException(500, "模板文件创建失败");
        }
        String upload = minioEngine.upload(minioEngine.TEMP_BUCKET, tempFile);
        tempFile.delete();
        return upload;
    }

    @Value("${minio.url}")
    String externalAddress;

    /**
     * 上传私有文件
     *
     * @param multipartFile
     * @return 文件uri = 文件目录+文件名
     */
    public String uploadPrivateFile(MultipartFile multipartFile, Boolean watermarkSkip) {
        File tempFile;
        try {
            tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), TMP_SUFFIX);
            multipartFile.transferTo(tempFile);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new VeException(500, "临时文件创建失败");
        }
        String url = minioEngine.upload(minioEngine.PRIVATE_BUCKET, tempFile);
        //        minioEngine.getPath()
        tempFile.delete();
        return url.replace(externalAddress + "/" + minioEngine.PRIVATE_BUCKET + "/", "");
    }

    /**
     * 上传私有文件
     *
     * @param file
     * @return 文件uri = 文件目录+文件名
     */
    public String uploadPrivateFile(File file, Boolean watermarkSkip) {
        String url = minioEngine.upload(minioEngine.PRIVATE_BUCKET, file);

        return url.replace(externalAddress + "/" + minioEngine.PRIVATE_BUCKET + "/", "");
    }

    /**
     * 覆盖上传私有文件
     *
     * @param file
     * @return 文件uri = 文件目录+文件名
     */
    public String putPrivateFile(File file, String uri, Boolean watermarkSkip) {
        String[] split = uri.split("/");
        String url = minioEngine.overrideUpload(minioEngine.PRIVATE_BUCKET, split[1], file);

        return url.replace(externalAddress + "/" + minioEngine.PRIVATE_BUCKET + "/", "");
    }

    public String getFileBase64(String uri) {
        GetObjectResponse fis = minioEngine.getFile(minioEngine.PRIVATE_BUCKET, uri);
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            IOUtils.copy(fis, outputStream);
            return Base64.toBase64String(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getFile(String uri, HttpServletResponse response) {
        GetObjectResponse fis = minioEngine.getFile(minioEngine.PRIVATE_BUCKET, uri);
        response.setHeader("Content-Disposition", "attachment;filename=" + "mydownload.jpg");
        response.setContentType("application/force-download");
        //        response.setCharacterEncoding("UTF-8");
        try {
            IOUtils.copy(fis, response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getFile(String uri, String fileName, HttpServletResponse response) {
        GetObjectResponse fis = minioEngine.getFile(minioEngine.PRIVATE_BUCKET, uri);
        response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        try {
            FileType fileType = FilePathHelper.parseFileType(fileName);
            response.setContentType(fileType.getMimeType());
        } catch (Exception e) {
            response.setContentType("application/force-download");
        }
        //        response.setCharacterEncoding("UTF-8");
        try {
            IOUtils.copy(fis, response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 返回一个1小时有效的预签名的文件url
     *
     * @param uri
     * @return
     */
    public String getFile(String uri) {
        return minioEngine.getPreSignFileUrl(minioEngine.PRIVATE_BUCKET, uri, 1, TimeUnit.HOURS);
    }

    public void tagFile(String uri, Map<String, String> map) {
        minioEngine.tagFile(minioEngine.PRIVATE_BUCKET, uri, map);
    }

    public Tags getFileTags(String uri) {
        return minioEngine.getFileTag(minioEngine.PRIVATE_BUCKET, uri);
    }

}
