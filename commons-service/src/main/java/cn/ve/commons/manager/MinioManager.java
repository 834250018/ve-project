package cn.ve.commons.manager;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.ve.base.pojo.FileType;
import cn.ve.base.pojo.VeException;
import cn.ve.commons.util.FilePathHelper;
import cn.ve.rest.util.FileUtil;
import cn.ve.file.util.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author ve
 * @date 2021/7/28
 */
@Slf4j
@Service
public class MinioManager {

    @Resource
    private MinioUtil minioUtil;
    @Value("${file.cleanup-expired-days}")
    private Integer cleanupExpiredDays;
    @Value("${minio.url}")
    private String externalAddress;

    public String uploadPublicFile(String fileKey, File file) {
        return uploadPublicFile(fileKey, FileUtil.getInputStream(file));
    }

    public String uploadPublicFile(String fileKey, InputStream is) {
        return minioUtil.upload(MinioUtil.PUBLIC_BUCKET, fileKey, is);
    }

    public String uploadPrivateFile(String fileKey, File file) {
        return uploadPrivateFile(fileKey, FileUtil.getInputStream(file));
    }

    public String uploadPrivateFile(String fileKey, InputStream file) {
        String url = minioUtil.upload(MinioUtil.PRIVATE_BUCKET, fileKey, file);
        return url.replace(externalAddress + "/" + MinioUtil.PRIVATE_BUCKET + "/", "");
    }

    /**
     * 清理指定日期的临时文件
     *
     * @return 文件完整url
     */
    public void clearTempBucket() {
        minioUtil.clearTempBucket(DateTime.now().offset(DateField.DAY_OF_YEAR, -cleanupExpiredDays).toJdkDate());
    }

    public String getFileBase64(String bucketName, String uri) {
        InputStream fis = minioUtil.getFile(bucketName, uri);
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            IOUtils.copy(fis, outputStream);
            return Base64.toBase64String(outputStream.toByteArray());
        } catch (IOException e) {
            throw new VeException(e.getMessage());
        }
    }

    public void getFile(String bucketName, String uri, HttpServletResponse response) {
        InputStream fis = minioUtil.getFile(bucketName, uri);
        response.setHeader("Content-Disposition", "attachment;filename=\"" + uri + "\"");
        try {
            FileType fileType = FilePathHelper.parseFileType(uri);
            if (fileType == null) {
                response.setContentType("application/force-download");
            } else {
                response.setContentType(fileType.getMimeType());
            }
        } catch (Exception e) {
            response.setContentType("application/force-download");
        }
        //        response.setCharacterEncoding("UTF-8");
        try {
            IOUtils.copy(fis, response.getOutputStream());
        } catch (IOException e) {
            throw new VeException(e);
        }
    }

    /**
     * 返回一个1小时有效的预签名的文件url
     *
     * @param uri
     * @return
     */
    public String getPreSignFileUrl(String bucketName, String uri) {
        return minioUtil.getPreSignFileUrl(bucketName, uri, 1, TimeUnit.HOURS);
    }

    public void tagFile(String bucketName, String uri, Map<String, String> map) {
        minioUtil.appendTag(bucketName, uri, map);
    }

    public Map<String, String> getFileTags(String bucketName, String uri) {
        return minioUtil.getTags(bucketName, uri);
    }

}
