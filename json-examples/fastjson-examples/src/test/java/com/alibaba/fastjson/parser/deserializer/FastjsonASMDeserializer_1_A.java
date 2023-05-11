
package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexerBase;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.JavaBeanInfo;
import com.vergilyn.examples.json.serialize.InvokeHandlerTests;

import java.lang.reflect.Type;

/* arthas 反编译的代码（不完整，删除了错误的代码。）
 *
 * Decompiled with CFR.
 *
 * Could not load the following classes:
 *  com.vergilyn.examples.json.serialize.InvokeHandlerTests$A
 *  com.vergilyn.examples.json.serialize.InvokeHandlerTests$B
 */
public class FastjsonASMDeserializer_1_A extends JavaBeanDeserializer {
	public char[] name_asm_prefix__ = "\"name\":".toCharArray();

	public char[] b_asm_prefix__ = "\"b\":".toCharArray();

	public ObjectDeserializer name_asm_deser__;

	public ObjectDeserializer b_asm_deser__;

	public FastjsonASMDeserializer_1_A(ParserConfig parserConfig, JavaBeanInfo javaBeanInfo) {
		super(parserConfig, javaBeanInfo);
	}

	public Object createInstance(DefaultJSONParser defaultJSONParser, Type type) {
		return new InvokeHandlerTests.A();
	}

	public Object deserialze(DefaultJSONParser defaultJSONParser, Type type, Object object, int n) {
		block16:
		{
			String string;
			InvokeHandlerTests.B b;
			int n2;
			InvokeHandlerTests.A a;
			block18:
			{
				ParseContext parseContext;
				ParseContext parseContext2;
				block17:
				{
					block20:
					{
						JSONLexerBase jSONLexerBase;
						block19:
						{
							String string2;
							jSONLexerBase = (JSONLexerBase) defaultJSONParser.lexer;
							if (jSONLexerBase.token() == 14 && jSONLexerBase.isEnabled(n, 8192)) {
								return this.deserialzeArrayMapping(defaultJSONParser, type, object, null);
							}
							if (!jSONLexerBase.isEnabled(512) || jSONLexerBase.scanType(
									"com.vergilyn.examples.json.serialize.InvokeHandlerTests$A") == -1)
								break block16;
							ParseContext parseContext3 = defaultJSONParser.getContext();
							int n3 = 0;
							a = new InvokeHandlerTests.A();
							parseContext2 = defaultJSONParser.getContext();
							parseContext = defaultJSONParser.setContext(parseContext2, a, object);
							if (jSONLexerBase.matchStat == 4)
								break block17;
							int n4 = 0;
							n2 = 0;
							boolean bl = jSONLexerBase.isEnabled(4096);
							b = null;
							if (bl) {
								n2 |= 2;
								string2 = jSONLexerBase.stringDefaultValue();
							} else {
								string2 = null;
							}
							string = string2;
							if (!jSONLexerBase.matchField(this.b_asm_prefix__)) {
								b = null;
							} else {
								n2 |= 1;
								++n3;
								if (this.b_asm_deser__ == null) {
									this.b_asm_deser__ = defaultJSONParser.getConfig().getDeserializer(
											(Type) ((Object) InvokeHandlerTests.B.class));
								}
								b = (InvokeHandlerTests.B) this.b_asm_deser__.deserialze(defaultJSONParser,
								                                                         (Type) ((Object) InvokeHandlerTests.B.class),
								                                                         "b");
								if (defaultJSONParser.getResolveStatus() == 1) {
									DefaultJSONParser.ResolveTask resolveTask = defaultJSONParser.getLastResolveTask();
									resolveTask.ownerContext = defaultJSONParser.getContext();
									resolveTask.fieldDeserializer = this.getFieldDeserializer("b");
									defaultJSONParser.setResolveStatus(0);
								}
							}
							string = jSONLexerBase.scanFieldString(this.name_asm_prefix__);
							if (jSONLexerBase.matchStat > 0) {
								n2 |= 2;
							}
							if ((n4 = jSONLexerBase.matchStat) == -1)
								break block18;
							if (jSONLexerBase.matchStat <= 0)
								break block19;
							++n3;
							if (jSONLexerBase.matchStat == 4)
								break block20;
						}
						if (jSONLexerBase.matchStat != 4)
							break block18;
					}
					if ((n2 & 1) != 0) {
						a.setB(b);
					}
					if ((n2 & 2) != 0) {
						a.setName(string);
					}
				}
				defaultJSONParser.setContext(parseContext2);
				if (parseContext != null) {
					parseContext.object = a;
				}
				return a;
			}
			if ((n2 & 1) != 0) {
				a.setB(b);
			}
			if ((n2 & 2) != 0) {
				a.setName(string);
			}
			return (InvokeHandlerTests.A) this.parseRest(defaultJSONParser, type, object, a, n, new int[] { n2 });
		}
		return super.deserialze(defaultJSONParser, type, object, n);
	}
}