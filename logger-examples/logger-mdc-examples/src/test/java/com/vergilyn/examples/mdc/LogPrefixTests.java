package com.vergilyn.examples.mdc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import java.util.Map;

@Slf4j
public class LogPrefixTests {

	@Test
	public void basicLogPrefixContext(){
		LogPrefixContext.put("username", "vergilyn");
		log.debug("{} debug", LogPrefixContext.getLogPrefix());
	}

	/**
	 * 推荐，相对比 {@link #basicLogPrefixContext()} 更友好。
	 */
	@Test
	public void basicLogPrefixMDC(){
		LogPrefixMDC.put("username", "vergilyn");
		log.debug("{} debug", LogPrefixMDC.getLogPrefix());
	}

	/**
	 * <pre>{@linkplain org.slf4j.MDC} 核心常用方法
	 * - {@linkplain org.slf4j.MDC#clear()}
	 * - {@linkplain org.slf4j.MDC#get(String)}
	 * - {@linkplain org.slf4j.MDC#getCopyOfContextMap()}
	 * - {@linkplain org.slf4j.MDC#put(String, String)}
	 * - {@linkplain org.slf4j.MDC#remove(String)}
	 * - {@linkplain org.slf4j.MDC#setContextMap(Map)}
	 * </pre>
	 *
	 * 1. 共用`MDC`，通过 MDC 的key-prefix区分哪些属于 log-prefix （所以有些方法要特殊处理）；<br/>
	 * 2. `clear` 特殊处理，只clear MDC 中属于log-prefix的 key；<br/>
	 * 3. `getLogPrefix` 特殊处理<br/>
	 * 4. 如果存在 layout `%X{}`, 稍微有影响（不期望输出 log-prefix 前缀的 MDC.key，实际会）。 <br/>
	 *
	 * （更推荐 完全参照{@linkplain MDC}重新实现一个 `LogPrefixContext`，这样与 logger-MDC 完全区别开）
	 *
	 * @see LogPrefixMDC
	 */
	public static class LogPrefixContext {
		private static final String KEY_LOG_PREFIX = "%log_prefix%";

		private static String wrapperKey(String key){
			return KEY_LOG_PREFIX + key;
		}

		public static String getLogPrefix(){
			Map<String, String> copyOfContextMap = getCopyOfContextMap();

			StringBuilder builder = new StringBuilder();
			String actualKey;
			for (Map.Entry<String, String> entry : copyOfContextMap.entrySet()) {
				if (entry.getKey().startsWith(KEY_LOG_PREFIX)){
					actualKey = entry.getKey().substring(KEY_LOG_PREFIX.length());
					builder.append("[").append(actualKey).append("-").append(entry.getValue()).append("]");
				}
			}

			return builder.toString();
		}

		public static void clear() {
			Map<String, String> copyOfContextMap = getCopyOfContextMap();
			for (String key : copyOfContextMap.keySet()) {
				if (key.startsWith(KEY_LOG_PREFIX)){
					remove(key);
				}
			}
		}

		public static String get(String key) throws IllegalArgumentException {
			return MDC.get(wrapperKey(key));
		}

		public static Map<String, String> getCopyOfContextMap() {
			return MDC.getCopyOfContextMap();
		}

		public static void put(String key, String val) throws IllegalArgumentException {
			MDC.put(wrapperKey(key), val);
		}

		public static void remove(String key) throws IllegalArgumentException {
			MDC.remove(wrapperKey(key));
		}

		public static void setContextMap(Map<String, String> contextMap) {
			MDC.setContextMap(contextMap);
		}
	}

}
