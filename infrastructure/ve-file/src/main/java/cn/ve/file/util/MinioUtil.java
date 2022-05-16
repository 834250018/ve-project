package cn.ve.file.util;

import cn.ve.base.pojo.VeBaseException;
import cn.ve.base.util.StringConstant;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import io.minio.messages.Tags;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author ve
 * @date 2021年7月27日
 */
@Slf4j
public class MinioUtil {
    // 公共bucket,公开权限
    public static final String PUBLIC_BUCKET = "public";
    // 私有bucket,不公开权限
    public static final String PRIVATE_BUCKET = "private";
    public static final String CATALOG_DATE_FORMATTER = "yyyyMMdd";
    public static final String TEMP_PATH = "temp/";
    private static final String READ_ONLY =
        "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:ListBucket\",\"s3:GetBucketLocation\"],\"Resource\":[\"arn:aws:s3:::#bucketName#\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::#bucketName#/*\"]}]}";
    private static final String WRITE_ONLY =
        "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:ListBucketMultipartUploads\",\"s3:GetBucketLocation\"],\"Resource\":[\"arn:aws:s3:::#bucketName#\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:PutObject\",\"s3:AbortMultipartUpload\",\"s3:DeleteObject\",\"s3:ListMultipartUploadParts\"],\"Resource\":[\"arn:aws:s3:::#bucketName#/*\"]}]}";
    private static final String READ_WRITE =
        "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:ListBucketMultipartUploads\",\"s3:GetBucketLocation\",\"s3:ListBucket\"],\"Resource\":[\"arn:aws:s3:::#bucketName#\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:AbortMultipartUpload\",\"s3:DeleteObject\",\"s3:GetObject\",\"s3:ListMultipartUploadParts\",\"s3:PutObject\"],\"Resource\":[\"arn:aws:s3:::#bucketName#/*\"]}]}";
    /**
     * 常规操作
     */
    private final MinioClient minioClient;
    /**
     * 走预签名地址需要用到(需要证书)
     */
    private final MinioClient extMinioClient;
    private final String externalAddress;

    /**
     * @param insideNetwork   内网地址
     * @param externalAddress 外网地址
     * @param accessKey
     * @param secretKey
     */
    public MinioUtil(String insideNetwork, String externalAddress, String accessKey, String secretKey) {
        this.externalAddress = externalAddress;
        this.minioClient = MinioClient.builder().endpoint(insideNetwork).credentials(accessKey, secretKey).build();
        this.extMinioClient =
            MinioClient.builder().endpoint(this.externalAddress).credentials(accessKey, secretKey).build();
        // 检查并创建默认bucket
        checkBucket(PUBLIC_BUCKET);
        setBucketPolicy(PUBLIC_BUCKET, READ_ONLY);
        // 检查并创建默认bucket
        checkBucket(PRIVATE_BUCKET);
    }

    public void createBucket(String bucketName) {
        checkBucket(bucketName);
    }

    private void checkBucket(String bucketName) {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            log.error("minio服务异常【{}】", e.getMessage(), e);
            throw new VeBaseException("minio服务异常");
        }
    }

    /**
     * 获取桶配置策略
     *
     * @param bucketName
     */
    public void getBucketPolicy(String bucketName) {
        try {
            minioClient.getBucketPolicy(GetBucketPolicyArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            log.error("minio服务异常【{}】", e.getMessage(), e);
            throw new VeBaseException("minio服务异常");
        }
    }

    private void setBucketPolicy(String bucketName, String config) {
        try {
            minioClient.setBucketPolicy(
                SetBucketPolicyArgs.builder().bucket(bucketName).config(config.replace("#bucketName#", bucketName))
                    .build());
        } catch (Exception e) {
            log.error("minio服务异常【{}】", e.getMessage(), e);
            throw new VeBaseException("minio服务异常");
        }
    }

    /**
     * 清理指定日期的临时文件,以temp/yyyyMMdd/开头
     *
     * @param date
     */
    public void clearTempBucket(Date date) {
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(PUBLIC_BUCKET)
            .prefix(TEMP_PATH + new SimpleDateFormat(CATALOG_DATE_FORMATTER).format(date) + StringConstant.SLASH)
            .recursive(Boolean.TRUE).build());
        try {
            for (Result<Item> itemResult : results) {
                minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(PUBLIC_BUCKET).object(itemResult.get().objectName()).build());
            }
        } catch (Exception e) {
            log.error("minio服务异常【{}】", e.getMessage(), e);
            throw new VeBaseException("minio服务异常");
        }
    }

    public String getPath(String bucketName, String fileName) {
        return externalAddress + StringConstant.SLASH + bucketName + StringConstant.SLASH + fileName;
    }

    /**
     * 上传文件
     *
     * @param bucketName  桶名称
     * @param fileKey     文件名称带后缀,可以加斜杠设置minio目录
     * @param inputStream 文件流
     * @return
     */
    public String upload(String bucketName, String fileKey, InputStream inputStream) {
        try {
            ObjectWriteResponse resp = minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(fileKey).stream(inputStream, -1, -1).build());
            return externalAddress + StringConstant.SLASH + bucketName + StringConstant.SLASH + resp.object();
        } catch (Exception e) {
            log.error("minio服务异常【{}】", e.getMessage(), e);
            throw new VeBaseException("minio服务异常");
        }
    }

    /**
     * @param bucketName 桶名称
     * @param uri        目录+文件名称+文件后缀
     * @return
     */
    public InputStream getFile(String bucketName, String uri) {
        try {
            return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(uri).build());
        } catch (Exception e) {
            log.error("minio服务异常【{}】", e.getMessage(), e);
            throw new VeBaseException("minio服务异常");
        }
    }

    /**
     * 获取一个临时预签名的url
     *
     * @param bucketName 桶
     * @param uri        文件目录+文件名称+文件后缀
     * @param duration   有效时间
     * @param unit       有效时间单位
     * @return
     */
    public String getPreSignFileUrl(String bucketName, String uri, int duration, TimeUnit unit) {
        try {
            return extMinioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(uri).method(Method.GET)
                    .expiry(duration, unit).build());
        } catch (Exception e) {
            log.error("minio服务异常【{}】", e.getMessage(), e);
            throw new VeBaseException("minio服务异常");
        }
    }

    /**
     * 获取文件的标签
     *
     * @param bucketName
     * @param uri        文件目录+文件名称+文件后缀
     * @return
     */
    public Map<String, String> getTags(String bucketName, String uri) {
        try {
            Tags objectTags =
                minioClient.getObjectTags(GetObjectTagsArgs.builder().bucket(bucketName).object(uri).build());
            return new HashMap<>(objectTags.get());
        } catch (Exception e) {
            log.error("minio服务异常【{}】", e.getMessage(), e);
            throw new VeBaseException("minio服务异常");
        }
    }

    /**
     * 清空文件的标签
     *
     * @param bucketName
     * @param uri
     */
    public void clearTag(String bucketName, String uri) {
        try {
            minioClient.setObjectTags(
                SetObjectTagsArgs.builder().tags(new HashMap<>()).bucket(bucketName).object(uri).build());
        } catch (Exception e) {
            log.error("minio服务异常【{}】", e.getMessage(), e);
            throw new VeBaseException("minio服务异常");
        }
    }

    /**
     * 给文件追加标签
     *
     * @param bucketName
     * @param uri        文件目录+文件名称+文件后缀
     * @return
     */
    public void appendTag(String bucketName, String uri, Map<String, String> map) {
        Map<String, String> newTags = getTags(bucketName, uri);
        newTags.putAll(map);
        try {
            minioClient.setObjectTags(SetObjectTagsArgs.builder().tags(newTags).bucket(bucketName).object(uri).build());
        } catch (Exception e) {
            log.error("minio服务异常【{}】", e.getMessage(), e);
            throw new VeBaseException("minio服务异常");
        }
    }

    /**
     * 给文件追加标签
     *
     * @param bucketName
     * @param uri        文件目录+文件名称+文件后缀
     * @return
     */
    public void removeTag(String bucketName, String uri, List<String> list) {
        Map<String, String> newTags = getTags(bucketName, uri);
        for (String s : list) {
            newTags.remove(s);
        }
        try {
            minioClient.setObjectTags(SetObjectTagsArgs.builder().tags(newTags).bucket(bucketName).object(uri).build());
        } catch (Exception e) {
            log.error("minio服务异常【{}】", e.getMessage(), e);
            throw new VeBaseException("minio服务异常");
        }
    }

}
