package com.vergilyn.examples.poi;

import java.text.DecimalFormat;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

import static org.apache.poi.ss.usermodel.CellType.STRING;

public class ExcelParser {
	private static final String STR_NONE = "none";

	/**
	 * FIXME 2021-01-11 如果是date，读取的string错误。例如 实际是`2020-01-11`，string获取的是`44207`
	 * @param row
	 * @param index
	 * @return
	 */
	public static String cellToString(Row row, int index){
		Cell cell = row.getCell(index);

		if (cell == null) {
			return STR_NONE;
		}
		cell.setCellType(STRING);


		String cellValue = cell.getStringCellValue();
		cellValue = StringUtils.trimToEmpty(cellValue);

		return StringUtils.isBlank(cellValue) ? STR_NONE : cellValue;
	}

	/**
	 * FIXME 2021-01-14 未写完，只是想表达以下方式也不是很方便获取cell-value。
	 * @param cell
	 * @return
	 */
	private static Object getCellValue(Cell cell){
		switch (cell.getCellType()){
			case NUMERIC:
				// Numeric cell type (whole numbers, fractional numbers, dates)
				if (DateUtil.isCellDateFormatted(cell)) {
					return cell.getDateCellValue();
				} else {  //否
					return new DecimalFormat("#.######").format(cell.getNumericCellValue());
				}
			case STRING:
				return cell.getStringCellValue();
			default: break;
		}

		return null;
	}
}
