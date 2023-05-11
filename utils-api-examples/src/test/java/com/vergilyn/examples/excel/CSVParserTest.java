package com.vergilyn.examples.excel;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CSVParserTest {

	@Test
	public void parser() {
		try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("vergilyn.csv")){
			CSVParser parser = CSVParser.parse(inputStream, StandardCharsets.UTF_8, CSVFormat.DEFAULT);
			List<CSVRecord> list = parser.getRecords();

			list.forEach(csvRecord -> {
				csvRecord.forEach(s -> {
					System.out.print(s + ", ");

				});
				System.out.println();
			});
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void output() {

		final StringWriter sw = new StringWriter();
		try (final CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT)) {

			for (int i = 0; i < 15000; i++) {
//				printer.printRecord(RandomUtils.nextLong(10_0000_0000L, 99_9999_9999L));
				printer.printRecord(i + 1);
// 				printer.printRecord("16038503215768327");
// 				printer.printRecord("16552565762179258");
			}

			printer.flush();

			final String result = sw.toString();
			System.out.println(result);

		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
