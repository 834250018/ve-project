package cn.ve.commons.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class FileParam implements Serializable {

    /**
     * 文件base64
     */
    private String base64;

    /**
     * 文件名称(含后缀)
     */
    private String fileName;

    /**
     * 文件路径(含目录跟后缀)
     */
    private String filePath;

    /**
     * 打标签
     */
    private Map<String, String> tags;
}