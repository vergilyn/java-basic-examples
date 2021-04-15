package com.vergilyn.examples.logger;

import java.time.LocalTime;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

/**
 *
 * @author vergilyn
 * @since 2021-04-07
 *
 * @see <a href="https://www.liaoxuefeng.com/wiki/1252599548343744/1264738568571776">使用JDK Logging</a>
 */
public class JdkLoggerTests {

	@Test
	public void global(){
		Logger logger = Logger.getGlobal();
		logger.info("start process...");
		logger.warning("memory is running out...");
		logger.fine("ignored.");
		logger.severe("process will be terminated...");
	}

	@Test
	public void custom(){
		Logger logger = Logger.getLogger("vergilynJdkLoggerFormat");
		//如果需要将日志文件写到文件系统中，需要创建一个FileHandler对象
		ConsoleHandler consoleHandler = new ConsoleHandler();

		//创建日志格式文件：本次采用自定义的Formatter
		consoleHandler.setFormatter(new VergilynJdkLoggerFormat());
		logger.addHandler(consoleHandler);

		logger.info("start process...");
		logger.warning("memory is running out...");
		logger.fine("ignored.");
		logger.severe("process will be terminated...");
	}

	class VergilynJdkLoggerFormat extends Formatter {
		private static final String SPACE = " ";

		@Override
		public String format(LogRecord record) {
			StringBuilder builder = new StringBuilder();
			builder.append(LocalTime.now()).append(SPACE)
					.append(Thread.currentThread().getName()).append(SPACE)
					.append(record.getLevel().getName()).append(SPACE)
					.append(record.getSourceClassName()).append(SPACE)
					.append(">>>> ").append(record.getMessage());

			return builder.toString();
		}
	}
}
