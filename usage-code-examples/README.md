# usage-code-examples

### u0001
格式化成表格打印，参考：JMH report
```text
Benchmark                               Mode  Cnt     Score     Error  Units
CollectionAddJMH.arrayList                ss    5     0.612 ±   1.338  ms/op
CollectionAddJMH.copyOnWriteArrayList     ss    5  2644.711 ± 152.747  ms/op
CollectionAddJMH.linkedList               ss    5     1.237 ±   0.313  ms/op
CollectionAddJMH.synchronizedArrayList    ss    5     2.002 ±   0.480  ms/op
CollectionAddJMH.vector                   ss    5     1.270 ±   0.882  ms/op
```

### u0003
获取泛型的真实类型。

1. java中存在泛型擦除，所以**无法在运行时获取instance的泛型类型**。
```JAVA

public class Main{
    public static void main(String[] args){
    	// integerList.get(0).getClass() 无法获取 null-list的情况。
		List<Integer> integerList = new ArrayList<Integer>();

		ParameterizedType parameterizedType = ((ParameterizedType) integerList.getClass().getGenericSuperclass());

		Type[] types = parameterizedType.getActualTypeArguments();

		// 打印：E
		System.out.println(types[0]);
	}
}
```

备注：  
可以利用`List<Integer> integerList = new ArrayList<Integer>(){}`特性获取，  
但是实用性不一定高。


2. 

see: spring-data `ClassTypeInformation.class` & `GenericTypeResolver`