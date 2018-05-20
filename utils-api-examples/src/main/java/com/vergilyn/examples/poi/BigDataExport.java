package com.vergilyn.examples.poi;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * 利用Apache-POI:{@link org.apache.poi.xssf.streaming.SXSSFWorkbook}导出100w行excel数据。
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/10/19
 */
public class BigDataExport {
    private final static String FILE_PATH = "D://big_data_excel.xlsx";
    /** 导出100w行数据 */
    private final static int MAX_ROW = 100 * 10000;
    /** 内存中最大保留数据行数 */
    private final static int ROW_ACCESS_WINDOW_SIZE = 1000;

    public static void main(String[] args) {
        OutputStream os = null;
        SXSSFWorkbook sxwb = null;
        try {
            os = new FileOutputStream(FILE_PATH);
            //内存中保留 ROW_ACCESS_WINDOW_SIZE 条数据，以免内存溢出，其余写入硬盘
            sxwb = new SXSSFWorkbook(ROW_ACCESS_WINDOW_SIZE);
            Sheet sheet = sxwb.createSheet("sheet1");
            for (int i = 0; i < MAX_ROW; i++) {
                // SXSSFSheet.createRow();里面有flushRows(ROW_ACCESS_WINDOW_SIZE)
                Row row = sheet.createRow(i);
                Cell cell = row.createCell(0);
                cell.setCellValue(i);
            }
            sxwb.write(os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 删除sxwb创建的临时文件
            // 参考路径: C:\Users\{用户名}\AppData\Local\Temp\poi-sxssf-sheet6856262485338076614.xml
            if(sxwb != null){
                sxwb.dispose();
            }

            if(os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
