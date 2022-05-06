package com.vergilyn.examples.usage.u0012;


/**
 * TODO 2022-03-30，场景：例如http请求返回 json， 所有字段都是 String，需要转换到 Integer/BigDecimal/Boolean 之类的类型。
 * （前提，已知 json-field 实际对应的java类型。）
 *
 * 可以参考：jdbc 或者 restTemplate（例如 FastJsonHttpMessageConverter） 中怎么处理的。（当然可以简单的 自己写。）
 *
 * @author vergilyn
 * @since 2022-03-30
 *
 * @see com.alibaba.fastjson.util.TypeUtils.TypeUtils#castToInt(Object)
 */
public class StringToObject {
}
