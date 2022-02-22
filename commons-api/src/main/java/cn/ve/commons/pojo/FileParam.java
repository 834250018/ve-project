package cn.ve.commons.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class FileParam implements Serializable {

    /**
     * 文件base64
     */
    private String base64;
    /**
     * 文件前缀
     */
    private String prefix;

    /**
     * 文件后缀
     */
    private String suffix;

    /**
     * 覆盖的uri
     */
    private String uri;
}