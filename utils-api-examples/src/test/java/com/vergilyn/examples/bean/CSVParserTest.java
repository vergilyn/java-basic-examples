package com.vergilyn.examples.bean;

import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;

public class CSVParserTest {

	@Test
	public void parser() {
		try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("vergilyn-test.csv")){
			CSVParser parser = CSVParser.parse(inputStream, StandardCharsets.UTF_8, CSVFormat.DEFAULT);
			List<CSVRecord> list = parser.getRecords();

			list.forEach(csvRecord -> {
				System.out.println(csvRecord.get(0));
			});
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void output() {

		final StringWriter sw = new StringWriter();
		try (final CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT)) {

			for (int i = 0; i < 10000; i++) {
//				printer.printRecord(RandomUtils.nextLong(10_0000_0000L, 99_9999_9999L));
				printer.printRecord(UUID.randomUUID().toString());
			}

			printer.flush();

			final String result = sw.toString();
			System.out.println(result);

		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
