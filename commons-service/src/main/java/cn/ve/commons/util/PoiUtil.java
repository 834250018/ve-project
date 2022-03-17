package cn.ve.commons.util;

import cn.ve.base.pojo.VeException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"resource"})
public class PoiUtil {

    // 默认单元格格式化日期字符串
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 默认单元格内容为数字时格式
    private static final DecimalFormat df = new DecimalFormat("0.00");

    private final static String SHEET_1 = "sheet1";

    public static List<List<Object>> readExcel(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        Workbook wb;
        if (file.getName().endsWith(".xls")) {
            wb = new HSSFWorkbook(fis);
        } else if (file.getName().endsWith(".xlsx")) {
            wb = new XSSFWorkbook(fis);
        } else {
            throw new VeException("文件格式不正确");
        }
        try {
            List<List<Object>> rowList = new ArrayList<>();
            ArrayList<Object> colList;
            Sheet sheet = wb.getSheetAt(0);
            Row row;
            Cell cell;
            Object value;
            int firstRowNum = sheet.getFirstRowNum();
            int LastRowNum = sheet.getLastRowNum();
            for (int i = firstRowNum, rowCount = 0; rowCount < sheet.getPhysicalNumberOfRows(); i++) {
                row = sheet.getRow(i);
                colList = new ArrayList<>();
                if (row == null || row.getPhysicalNumberOfCells() == 0) {
                    // 当读取行为空时
                    if (i >= LastRowNum) {
                        break;
                    } else if (i != sheet.getPhysicalNumberOfRows()) {// 判断是否是最后一行
                        rowList.add(colList);
                    }
                    continue;
                } else {
                    rowCount++;
                }
                if (isRowEmpty(row)) {
                    continue;
                }
                for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    if (cell == null || cell.getCellType() == CellType.BLANK) {
                        // 当该单元格为空
                        if (j != row.getLastCellNum()) {// 判断是否是该行中最后一个单元格
                            colList.add("");
                        }
                        continue;
                    }
                    switch (cell.getCellType()) {
                        case STRING:
                            System.out.println(i + "行" + j + " 列 is String type");
                            value = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                                value = df.format(cell.getNumericCellValue());
                            } else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                                value = ((XSSFCell)cell).getRawValue();
                            } else {
                                value = sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
                            }
                            System.out.println(i + "行" + j + " 列 is Number type ; DateFormt:" + value.toString());
                            break;
                        case BOOLEAN:
                            System.out.println(i + "行" + j + " 列 is Boolean type");
                            value = cell.getBooleanCellValue();
                            break;
                        case BLANK:
                            System.out.println(i + "行" + j + " 列 is Blank type");
                            value = "";
                            break;
                        default:
                            System.out.println(i + "行" + j + " 列 is default type");
                            value = cell.toString();
                            break;
                    }// end switch
                    colList.add(value);
                } // end for j
                rowList.add(colList);
            } // end for i

            return rowList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c <= row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    /**
     * 把生成的excel写入Response
     *
     * @param res
     * @param lists
     * @throws IOException
     */
    public static void writeIntoResponse(HttpServletResponse res, List<List<Object>> lists) throws IOException {
        OutputStream out = res.getOutputStream();
        res.setHeader("content-Type", "application/vnd.ms-excel");
        res.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("报表.xlsx", "utf-8"));

        //创建一个工作簿

        try (Workbook wb = new XSSFWorkbook()) {
            //创建一个sheet
            Sheet sheet = wb.createSheet(SHEET_1);
            //创建第一行标题行
            writeData(sheet, lists);

            wb.write(out);
        }
    }

    /**
     * 把数据写入excel,只是简单的demo,代码跟随业务改变而改变
     *
     * @param sheet
     * @param lists
     */
    private static void writeData(Sheet sheet, List<List<Object>> lists) {
        int i = 0, j;
        for (List<Object> list : lists) {
            Row row = sheet.createRow(i);
            j = 0;
            for (Object o : list) {
                Cell cell = row.createCell(j);
                cell.setCellValue(o.toString());
                j++;
            }
            i++;
        }
    }
}