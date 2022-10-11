# json-examples

- [ ] 3. 2022-10-09，反序列化时，

### [x] 1. 序列化时保留 boolean类型的`is`前缀。方式：`NameFilter` （fastjson2更推荐`ContextNameFilter`）

### [x] 2. 序列化时 驼峰命名强制改成 下划线命名。(实现方式`同1`)


### [ ] 3. json反序列话时，期望调用后置处理方法
类似 spring `InitializingBean#afterPropertiesSet() / @PostConstruct`。  

2022-10-11，Gson可以实现，参考：gson-examples, [CustomPostConstructAdapterFactoryTests.java](gson-examples%2Fsrc%2Ftest%2Fjava%2Fcom%2Fvergilyn%2Fexamples%2Fgson%2FCustomPostConstructAdapterFactoryTests.java)

例如：
```java

public static interface InitializingBean {
	void afterPropertiesSet() throws Exception;
}

@Data
@NoArgsConstructor
public class A implements InitializingBean{
	private String name = "A";

	private B b;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.name += "_afterPropertiesSet";
		if (this.b == null){
			this.b = new B();
		}
	}
}

@Data
@NoArgsConstructor
public class B implements InitializingBean{
	private String name = "B";

	private C c = new C();

	@Override
	public void afterPropertiesSet() throws Exception {
		this.name += "_afterPropertiesSet";

		if (this.c == null){
			this.c = new C();
		}
	}
}

@Data
@NoArgsConstructor
public class C implements InitializingBean{
	private String name = "C";

	@Override
	public void afterPropertiesSet() throws Exception {
		this.name += "_afterPropertiesSet";
	}
}
```

假设现在存在 `jsonStr = "{\"name\": \"A\"}"`，反序列化后期望：
 - `{"name":"A_afterPropertiesSet","b":{"name":"B","c":{"name":"C"}}}`
 - `{"name":"A_afterPropertiesSet","b":{"name":"B_afterPropertiesSet","c":{"name":"C_afterPropertiesSet"}}}`

如果根据示例代码的结构，**实际结果为 期望1**。
**原因：**    
A对象是由json反序列化创建，可以触发 afterSet。但是 `B ` 其实是由于 `A.afterSet`，并没有主动调用`B.afterSet`（同理C）。

如果想达到期望2 的结果，可以写成：
```java
// 同理 B中也需要主动调用`c.afterPropertiesSet()`
public class A implements InitializingBean{
	private String name = "A";

	private B b;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.name += "_afterPropertiesSet";
		if (this.b == null){
			this.b = new B();
			this.b.afterPropertiesSet();
		}
	}
}
```
