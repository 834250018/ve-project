package cn.ve.base.util;


import cn.ve.base.pojo.BaseQO;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author ve
 * @date 2021/8/13
 */
public class CsvUtils<T> {

    private final OutputStream out;
    private final String[] propertys;
    private final Map<String, Field> fieldsMap;
    private final Map<String, Function<String, String>> functionMap;

    /**
     * @param out         要输出的流
     * @param titles      导出的数据类型的成员变量中文名 如:姓名,手机号
     * @param propertys   导出的数据类型的成员变量名 如:name,phone
     * @param functionMap 导出的数据类型的对应的处理函数,如果找不到则使用数据库的值
     * @param tClass      导出的数据类型 如User.class
     * @throws Exception
     */
    public CsvUtils(OutputStream out, String[] titles, String[] propertys,
        Map<String, Function<String, String>> functionMap, Class<T> tClass) throws Exception {
        this.out = out;
        this.propertys = propertys;
        this.functionMap = functionMap;
        Field[] fields = tClass.getDeclaredFields();
        this.fieldsMap = new HashMap<>();
        for (Field field : fields) {
            fieldsMap.put(field.getName(), field);
        }
        // 写入标题
        for (String title : titles) {
            write(title);
            write(StringConstant.COMMA);
        }
        //写完文件头后换行
        writeln();
    }

    private void write(String text) throws Exception {
        if (text == null) {
            out.write("".getBytes(StandardCharsets.UTF_8));
        } else {
            out.write(text.getBytes(StandardCharsets.UTF_8));
        }
    }

    private void writeln() throws Exception {
        out.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 查询过程写入
     *
     * @param supplier  查询过程
     * @param qo        查询条件
     * @param pageCount 最大查询页数,传入10相当于查十页,每页一千条
     * @throws Exception
     */
    public void writeData(Supplier<List<T>> supplier, BaseQO qo, int pageCount) throws Exception {
        for (int i = 1; i <= pageCount; i++) {
            qo.setPageNum(i);
            qo.setPageSize(1000);
            List<T> ts = supplier.get();
            writeData(ts);
        }
        out.close();
    }

    /**
     * 手动写入
     *
     * @param list 数据
     * @throws Exception
     */
    public void writeData(List<T> list) throws Exception {
        //写内容
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        for (T t : list) {
            //利用反射获取所有字段
            for (String property : propertys) {
                Field field = fieldsMap.get(property);
                if (field != null) {
                    //设置字段可见性
                    field.setAccessible(Boolean.TRUE);
                    Object o = field.get(t);
                    String text;
                    if (o == null) {
                        text = "";
                    } else if (o instanceof Date) {
                        text = "_" + simpleDateFormat.format(o);
                    } else if (o instanceof String) {
                        if (((String)o).length() == 11) {
                            // 假设为手机号
                            text = "'" + o;
                        } else {
                            text = (String)o;
                        }
                    } else {
                        text = o.toString();
                    }
                    Function<String, String> function = functionMap.get(property);
                    if (function == null) {
                        write(text);
                        write(StringConstant.COMMA);
                    } else {
                        write(function.apply(text));
                        write(StringConstant.COMMA);

                    }
                }
            }
            //写完一行换行
            writeln();
        }
        out.flush();
    }
}
