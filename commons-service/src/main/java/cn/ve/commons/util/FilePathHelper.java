/**
 *
 */
package cn.ve.commons.util;

import cn.ve.base.pojo.FileType;
import cn.ve.base.util.StringConstant;

public class FilePathHelper {

    public static FileType parseFileType(String filePath) {
        if (filePath.contains("/")) {
            filePath = filePath.substring(filePath.lastIndexOf("/"));
        }
        filePath = filePath.split("\\?")[0];
        if (filePath.contains(StringConstant.DOT)) {
            String suffix = filePath.substring(filePath.lastIndexOf(StringConstant.DOT) + 1);
            return FileType.valueOf2(suffix);
        }
        return null;
    }

}
