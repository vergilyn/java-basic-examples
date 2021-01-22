package com.vergilyn.examples.excel;

import java.io.InputStream;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.Test;

import static com.alibaba.fastjson.serializer.SerializerFeature.PrettyFormat;
import static com.vergilyn.examples.poi.ExcelParser.cellToString;

public class ExcelParserTest {

	/**
	 * 解析100w行（类似的）数据大约需要 10s。
	 */
	@Test
	public void parseExcel() {

		List<String[]> contents = Lists.newArrayList();

		int rowSize = 0, rowActual = 0;
		try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("vergilyn.xlsx");
			Workbook wb = WorkbookFactory.create(is)){

			Sheet sheet = wb.getSheetAt(0);
			rowSize = sheet.getLastRowNum() + 1;

			// 第3行开始才是数据
			for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
				Row row = sheet.getRow(rowIndex);
				if (row == null) {
					continue;
				}

				rowActual ++;

				contents.add(new String[] {
						cellToString(row, 0),
						cellToString(row, 1),
						cellToString(row, 2),
				});
			}

		}catch (Exception e){
			e.printStackTrace();
		}

		System.out.printf("rowSize: %d, rowActual: %d \n", rowSize, rowActual);
		System.out.println(JSON.toJSONString(contents, PrettyFormat));
	}

}
