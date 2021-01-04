package com.vergilyn.examples.bean;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.junit.jupiter.api.Test;
import org.testng.collections.Sets;

public class RemoteFileTest {

	/**
	 * excel 内容
	 * <pre>
	 *  ID  NAME
	 *  1   name-1
	 *  2   name-2
	 *  3   name-3
	 *  4   name-4
	 *  5   name-5
	 * </pre>
	 */
	@Test
	public void test(){
		String url = "https://127.0.0.1:8080/1609408624086.xlsx";

		HttpsURLConnection conn = null;
		InputStream inputStream = null;
		try {
			// 为了调用disconnect，需要区分http/https
			// http  HttpURLConnection
			// https HttpsURLConnection
			conn = (HttpsURLConnection) new URL(url).openConnection();
			conn.connect();

			inputStream = conn.getInputStream();
			Set<String> result = Sets.newHashSet();
			readExcel(inputStream, result);

			System.out.println(result);
		}catch (Exception e){
			e.printStackTrace();
		} finally {
			if (conn != null){
				conn.disconnect();
			}
			IOUtils.closeQuietly(inputStream);
		}
	}

	private void readExcel(InputStream inputStream, Set<String> result)
			throws IOException {
		try (Workbook wb = WorkbookFactory.create(inputStream)){

			Sheet sheet = wb.getSheetAt(0);
			int rowSize = sheet.getLastRowNum() + 1;
			// 第3行开始才是数据
			for (int rowIndex = 2; rowIndex < rowSize; rowIndex++) {
				Row row = sheet.getRow(rowIndex);
				if (row == null) {
					continue;
				}

				/* 转成XSSFCell是为了所有cell都用string的形式获取。
				 * 如果是`Cell`，Excel中对应的是 int，那么调用 `Cell#getStringCellValue()`会报异常。
				 */
				XSSFCell cell = (XSSFCell) row.getCell(1);
				if (cell == null){
					continue;
				}
				String cellValue = cell.getStringCellValue();
				cellValue = StringUtils.trimToEmpty(cellValue);

				if (StringUtils.isBlank(cellValue)){
					continue;
				}

				result.add(cellValue);
			}

		}catch (Exception e){
			throw e;
		}

	}
}
