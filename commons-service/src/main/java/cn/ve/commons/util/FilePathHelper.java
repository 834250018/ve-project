/**
 *
 */
package cn.ve.commons.util;

import cn.ve.base.pojo.FileType;

public class FilePathHelper {

    public static FileType parseFileType(String filePath) {
        if (filePath.contains("/")) {
            filePath = filePath.substring(filePath.lastIndexOf("/"));
        }
        filePath = filePath.split("\\?")[0];
        if (filePath.contains(".")) {
            String suffix = filePath.substring(filePath.lastIndexOf(".") + 1);
            return FileType.valueOf2(suffix);
        }
        return null;
    }

}
