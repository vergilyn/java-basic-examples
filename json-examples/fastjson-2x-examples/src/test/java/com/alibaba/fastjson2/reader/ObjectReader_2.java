/*
 * Decompiled with CFR.
 *
 * Could not load the following classes:
 *  com.vergilyn.examples.fastjson2.serialize.FieldNameSensitiveTests$JavaBean
 */
package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.vergilyn.examples.fastjson2.serialize.FieldNameSensitiveTests;

import java.util.function.Supplier;

/**
 * <pre>
 * {@code
 *    @Getter
 *    @Setter
 *    @NoArgsConstructor
 *    public static class JavaBean implements Serializable {
 * 		private String username;
 *
 * 		private String userName;
 *
 *    }
 * }
 * </pre>
 *
 * 生成出来的代码中，一定不会调用 `userName`
 */
public final class ObjectReader_2
		extends ObjectReaderAdapter {
	public FieldReader fr0;
	public ObjectReader objectReader_0;

	public ObjectReader_2(Class clazz, Supplier supplier, FieldReader[] fieldReaderArray) {
		super(clazz, null, null, 0L, null, supplier, null, fieldReaderArray);
		this.fr0 = fieldReaderArray[0];
	}

	@Override
	public Object createInstance(long l) {
		return new FieldNameSensitiveTests.JavaBean();
	}

	@Override
	public Object readJSONBObject(JSONReader jSONReader, long l) {
		// 忽略此代码逻辑
		return null;
	}

	@Override
	public Object readObject(JSONReader jSONReader, long l) {
		long l2;
		if (jSONReader.isJSONB()) {
			return this.readJSONBObject(jSONReader, l);
		}
		if (jSONReader.isArray() && jSONReader.isSupportBeanArray(l)) {
			boolean bl = jSONReader.nextIfMatch('[');
			FieldNameSensitiveTests.JavaBean javaBean = new FieldNameSensitiveTests.JavaBean();
			String string = jSONReader.readString();
			if (string != null) {
				// empty if block
			}
			javaBean.setUsername(string);
			jSONReader.nextIfMatch(']');
			jSONReader.nextIfMatch(',');
			return javaBean;
		}
		jSONReader.nextIfMatch('{');
		FieldNameSensitiveTests.JavaBean javaBean = new FieldNameSensitiveTests.JavaBean();
		int n = 0;
		while (!jSONReader.nextIfMatch('}') && (l2 = jSONReader.readFieldNameHashCode()) != -1L) {
			if (n == 0 && l2 == 3044221079117626727L && jSONReader.isSupportAutoType(l)) {
				return this.auoType(jSONReader, this.objectClass, l);
			}
			if (l2 == -7735419762015468167L) {
				String string = jSONReader.readString();
				if (string != null) {
					// empty if block
				}
				javaBean.setUsername(string);
			} else {
				if (jSONReader.isSupportSmartMatch(l)) {
					l2 = jSONReader.getNameHashCodeLCase();
				}
				if (l2 == -7735419762015468167L) {
					String string = jSONReader.readString();
					if (string != null) {
						// empty if block
					}
					javaBean.setUsername(string);
				} else {
					jSONReader.skipValue();
				}
			}
			++n;
		}
		jSONReader.nextIfMatch(',');
		return javaBean;
	}

	@Override
	public FieldReader getFieldReader(long l) {
		if (l != -7735419762015468167L) {
			return null;
		}
		return this.fr0;
	}

	@Override
	public FieldReader getFieldReaderLCase(long l) {
		if (l != -7735419762015468167L) {
			return null;
		}
		return this.fr0;
	}
}
