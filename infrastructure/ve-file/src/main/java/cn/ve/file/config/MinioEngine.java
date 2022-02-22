package cn.ve.file.config;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import io.minio.messages.Tags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author ve
 * @date 2021年7月27日
 */
@Slf4j
public class MinioEngine {
    // 公共bucket,公开权限
    public static final String TEMP_BUCKET = "temp";
    // 私有bucket,不公开权限
    public static final String PRIVATE_BUCKET = "private";
    private static final String CATALOG_SEPARATOR = "/";
    private final String defaultBucketName;
    private final MinioClient minioClient;
    private final MinioClient extMinioClient;
    private final String externalAddress;

    /**
     * @param insideNetwork     内网地址
     * @param externalAddress   外网地址
     * @param defaultBucketName 默认bucketName
     * @param accessKey
     * @param secretKey
     */
    public MinioEngine(String insideNetwork, String externalAddress, String defaultBucketName, String accessKey,
        String secretKey) {
        defaultBucketName = defaultBucketName.toLowerCase(Locale.ROOT);
        //        this.allowBucketNames = Splitter.on(",").splitToList(allowBucketNames);
        // 图片压缩格式，为空则不压缩
        this.defaultBucketName = defaultBucketName;
        this.externalAddress = externalAddress;
        this.minioClient = MinioClient.builder().endpoint(insideNetwork).credentials(accessKey, secretKey).build();
        this.extMinioClient =
            MinioClient.builder().endpoint(this.externalAddress).credentials(accessKey, secretKey).build();
        // 检查并创建默认bucket
        checkBucket(defaultBucketName);
        setBucketPolicy(defaultBucketName, READ_ONLY);
        // 检查并创建默认bucket
        checkBucket(TEMP_BUCKET);
        setBucketPolicy(TEMP_BUCKET, READ_ONLY);
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
            throw new RuntimeException("minio服务异常");
        }
    }

    public void getBucketPolicy(String bucketName) {
        try {
            minioClient.getBucketPolicy(GetBucketPolicyArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            log.error("minio服务异常【{}】", e.getMessage(), e);
            throw new RuntimeException("minio服务异常");
        }
    }

    private static final String READ_ONLY =
        "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:ListBucket\",\"s3:GetBucketLocation\"],\"Resource\":[\"arn:aws:s3:::#bucketName#\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::#bucketName#/*\"]}]}";

    private static final String WRITE_ONLY =
        "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:ListBucketMultipartUploads\",\"s3:GetBucketLocation\"],\"Resource\":[\"arn:aws:s3:::#bucketName#\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:PutObject\",\"s3:AbortMultipartUpload\",\"s3:DeleteObject\",\"s3:ListMultipartUploadParts\"],\"Resource\":[\"arn:aws:s3:::#bucketName#/*\"]}]}";

    private static final String READ_WRITE =
        "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:ListBucketMultipartUploads\",\"s3:GetBucketLocation\",\"s3:ListBucket\"],\"Resource\":[\"arn:aws:s3:::#bucketName#\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:AbortMultipartUpload\",\"s3:DeleteObject\",\"s3:GetObject\",\"s3:ListMultipartUploadParts\",\"s3:PutObject\"],\"Resource\":[\"arn:aws:s3:::#bucketName#/*\"]}]}";

    private void setBucketPolicy(String bucketName, String config) {
        try {
            minioClient.setBucketPolicy(
                SetBucketPolicyArgs.builder().bucket(bucketName).config(config.replace("#bucketName#", bucketName))
                    .build());
        } catch (Exception e) {
            log.error("minio服务异常【{}】", e.getMessage(), e);
            throw new RuntimeException("minio服务异常");
        }
    }

    /**
     * 清理指定日期的临时文件
     *
     * @param date
     */
    public void clearTempBucket(Date date) {
        Iterable<Result<Item>> results = minioClient.listObjects(
            ListObjectsArgs.builder().bucket(TEMP_BUCKET).prefix(new SimpleDateFormat("yyyyMMdd").format(date) + "/")
                .recursive(true).build());
        results.forEach(itemResult -> {
            Item item;
            try {
                item = itemResult.get();
            } catch (Exception e) {
                log.error("minio服务异常【{}】", e.getMessage(), e);
                throw new RuntimeException("minio服务异常");
            }
            try {
                minioClient
                    .removeObject(RemoveObjectArgs.builder().bucket(TEMP_BUCKET).object(item.objectName()).build());
            } catch (Exception e) {
                log.error("minio服务异常【{}】", e.getMessage(), e);
                throw new RuntimeException("minio服务异常");
            }
        });
    }

    public String upload(String bucketName, File file) {
        return overrideUpload(bucketName, getUUID(), file);
    }

    private String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public String upload(String bucketName, byte[] data) {
        return overrideUpload(bucketName, getUUID(), data);
    }

    public String upload(String bucketName, InputStream in) {
        return overrideUpload(bucketName, getUUID(), in);
    }

    public String getPath(String fileName) {
        return externalAddress + CATALOG_SEPARATOR + defaultBucketName + CATALOG_SEPARATOR + fileName;
    }

    public String overrideUpload(String bucketName, String fileKey, File file) {
        String catalog = "yyyyMMdd";
        bucketName = StringUtils.isEmpty(bucketName) ? defaultBucketName : bucketName;
        //        if (!existsConfig(allowBucketNames, bucketName)) {
        //            throw new RuntimeException("非法bucket，不允许上传");
        //        }
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectWriteResponse resp = minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(catalog + CATALOG_SEPARATOR + fileKey + ".xx")
                    .stream(fis, file.length(), -1).build());
            fis.close();
            return externalAddress + CATALOG_SEPARATOR + bucketName + CATALOG_SEPARATOR + resp.object();
        } catch (Exception e) {
            log.error("minio服务异常【{}】", e.getMessage(), e);
            throw new RuntimeException("minio服务异常");
        }
    }

    /**
     * @param bucketName
     * @param uri        = catalog + fileKey + fileType
     * @return
     */
    public GetObjectResponse getFile(String bucketName, String uri) {
        try {
            return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(uri).build());
        } catch (Exception e) {
            log.error("minio服务异常【{}】", e.getMessage(), e);
            throw new RuntimeException("minio服务异常");
        }
    }

    /**
     * 获取一个临时预签名的url(一小时)
     *
     * @param bucketName 桶
     * @param uri        = catalog + fileKey + fileType
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
            throw new RuntimeException("minio服务异常");
        }
    }

    public String overrideUpload(String bucketName, String fileKey, byte[] data) {
        String catalog = "yyyyMMdd";
        bucketName = StringUtils.isEmpty(bucketName) ? defaultBucketName : bucketName;
        //        if (!existsConfig(allowBucketNames, bucketName)) {
        //            throw new RuntimeException("非法bucket，不允许上传");
        //        }
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectWriteResponse resp = minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(catalog + CATALOG_SEPARATOR + fileKey + ".xx")
                    .stream(bis, data.length, -1).build());
            bis.close();
            return externalAddress + CATALOG_SEPARATOR + bucketName + CATALOG_SEPARATOR + resp.object();
        } catch (Exception e) {
            log.error("minio服务异常【{}】", e.getMessage(), e);
            throw new RuntimeException("minio服务异常");
        }
    }

    private File bytes2File(byte[] data) {
        File source;
        try {
            source = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".xx");
            OutputStream os = new FileOutputStream(source);
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(os);
            bufferedOutput.write(data);
            bufferedOutput.close();
            os.close();
        } catch (Exception e) {
            log.error("图片处理异常【{}】", e.getMessage(), e);
            throw new RuntimeException("图片处理异常");
        }
        return source;
    }

    private File inputStream2File(InputStream is) {
        File source;
        try {
            source = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".xx");
            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            FileOutputStream fos = new FileOutputStream(source);
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            fos.close();
            is.close();
        } catch (Exception e) {
            log.error("图片处理异常【{}】", e.getMessage(), e);
            throw new RuntimeException("图片处理异常");
        }
        return source;
    }

    /**
     * 检查配置，如果包含则返回true，否则false
     *
     * @param configs
     * @param checkedParam
     */
    private boolean existsConfig(List<String> configs, String checkedParam) {
        for (String config : configs) {
            if (checkedParam.equals(config)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public String overrideUpload(String bucketName, String fileKey, InputStream is) {
        bucketName = StringUtils.isEmpty(bucketName) ? defaultBucketName : bucketName;
        File tempFile;
        try {
            tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".temp");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            OutputStream outStream = new FileOutputStream(tempFile);
            outStream.write(buffer);
        } catch (Exception e) {
            log.error("文件输入流异常【{}】", e.getMessage(), e);
            throw new RuntimeException("文件输入流异常");
        }
        return overrideUpload(bucketName, fileKey, tempFile);
    }

    /**
     * @param bucketName
     * @param uri        = catalog + fileKey + fileType
     * @return
     */
    public Tags getFileTag(String bucketName, String uri) {
        try {
            return minioClient.getObjectTags(GetObjectTagsArgs.builder().bucket(bucketName).object(uri).build());
        } catch (Exception e) {
            log.error("minio服务异常【{}】", e.getMessage(), e);
            throw new RuntimeException("minio服务异常");
        }
    }

    /**
     * @param bucketName
     * @param uri        = catalog + fileKey + fileType
     * @return
     */
    public void tagFile(String bucketName, String uri, Map<String, String> map) {
        try {
            minioClient.setObjectTags(SetObjectTagsArgs.builder().tags(map).bucket(bucketName).object(uri).build());
        } catch (Exception e) {
            log.error("minio服务异常【{}】", e.getMessage(), e);
            throw new RuntimeException("minio服务异常");
        }
    }

    /**
     * @param bucketName
     * @param uri        = catalog + fileKey + fileType
     * @return
     */
    public void appendFileTag(String bucketName, String uri, Map<String, String> map) {
        try {
            Tags objectTags =
                minioClient.getObjectTags(GetObjectTagsArgs.builder().bucket(bucketName).object(uri).build());
            HashMap<String, String> newTags = new HashMap<>(objectTags.get());
            newTags.putAll(map);
            minioClient.setObjectTags(SetObjectTagsArgs.builder().tags(newTags).bucket(bucketName).object(uri).build());
        } catch (Exception e) {
            log.error("minio服务异常【{}】", e.getMessage(), e);
            throw new RuntimeException("minio服务异常");
        }
    }

}
