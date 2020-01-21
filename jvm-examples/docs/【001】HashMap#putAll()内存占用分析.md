# 【001】HashMap#putAll()内存占用分析

## 案例

代码参考`com.vergilyn.examples.HashMapPutAllTestng`。  
(通过`testng`进行测试，并不是使用`main(String[] args)`测试。)

1. 调用`test-method`前：  
![【HashMapPutAllTestng】before-method.png](./images/【HashMapPutAllTestng】before-method.png)


2. 初始化`map`前，并且手动执行1次GC：  
![【HashMapPutAllTestng】before-init-map&manual-gc-after.png](./images/【HashMapPutAllTestng】before-init-map&manual-gc-after.png)

3. 初始化`map`后：  
![【HashMapPutAllTestng】init-map-after.png](./images/【HashMapPutAllTestng】init-map-after.png)

4. 调用`HashMap.putAll(...)`后：  
![【HashMapPutAllTestng】hashmap-putall-after.png](./images/【HashMapPutAllTestng】hashmap-putall-after.png)

5. 退出`test-method`后(为了释放局部变量和无效的对象)：  
![【HashMapPutAllTestng】exit-test-method.png](./images/【HashMapPutAllTestng】exit-test-method.png)

### 分析过程
#### 1. 初始化map后
通过`3.`可知，初始化 100,000 的key-value，生成了 100,000 的Instance：
- char[]: String 内部使用了 `char[]`
- java.lang.String: map的value是String
- java.util.HashMap$Node: map的value的链表结构
- java.lang.Integer: map的key


#### 2. 调用`putAll`后
只有`java.util.HashMap$Node`是原来的2倍。

原因，通过阅读JDK1.8的源码可知，`putAll`实现是遍历后add。

```JAVA
// HashMap#putAll 源码，JDK_1.8
public class HashMap<K,V> extends AbstractMap<K,V>
    implements Map<K,V>, Cloneable, Serializable {
    
    public void putAll(Map<? extends K, ? extends V> m) {
        putMapEntries(m, true);
    }
    
    final void putMapEntries(Map<? extends K, ? extends V> m, boolean evict) {
        int s = m.size();
        if (s > 0) {
            // 省略...
            // 遍历source-map，然后target-map调用put
            for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
                K key = e.getKey();
                V value = e.getValue();
                putVal(hash(key), key, value, false, evict);
            }
        }
    }
    
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                       boolean evict) {
        Node<K,V>[] tab; Node<K,V> p; int n, i;
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);  // java.util.HashMap$Node
        else {
            // 省略...
        }
        // 省略...
    }
    
    // java.util.HashMap$Node 是HashMap的静态内部类
    // Create a regular (non-tree) node
    Node<K,V> newNode(int hash, K key, V value, Node<K,V> next) {
        return new Node<>(hash, key, value, next);
    }

}
```

### 扩展

#### 1. String 内部还存在`private int hash;`，为什么未生成相应的`int`实例对象？
```JAVA
public final class String
    implements java.io.Serializable, Comparable<String>, CharSequence {
    /** The value is used for character storage. */
    private final char value[];

    /** Cache the hash code for the string */
    private int hash; // Default to 0

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = -6849794470754667710L;
    
    public String() {
        this.value = "".value;
    }

    public String(String original) {
        this.value = original.value;
        this.hash = original.hash;
    }
}
```
由String的构造函数可知，默认构造函数只会申请`value`所需要的`char[]`。

#### 2. 为什么key`java.lang.Integer`也没成倍增长？
不知道怎么解释或描述，感觉应该成倍增长，但又觉得确实没必要增长...（感觉如果有C语言的 指针&地址 基础会比较好理解）

- [Java 到底是值传递还是引用传递？](https://www.zhihu.com/question/31203609)：重点看`二：搞清楚赋值运算符（=）的作用`
- [指针 & 地址的联系和区别](https://segmentfault.com/a/1190000012875369)

例如`key = 1024`, 其 Integer实例对象所在内存地址`0x0001`。  
这个实例对象在`init map`时已经开辟，即`map.key[1024] 成员变量（指针pointer-01）指向的地址是 0x0001`。
在`HashMap.putAll() >>>> putVal(...)`时**（涉及java“引用传递”）**，`putAllMap.key[1024] 成员变量（指针pointer-02）指向的地址也是 0x0001`。
所以2个map的key都指向各自相同的 Integer实例对象。

那么如果是：
```
HashMap<Integer, String> putAllMap = new HashMap<>(size);
for (Map.Entry<Integer, String> e : map.entrySet()) {
    putAllMap.put(new Integer(e.getKey()), e.getValue());
}

System.gc();
sleep(1, "iterator HashMap.put(...) success, and manual invoke `System.gc()`, putAllMap.size: " + putAllMap.size());
 
```

CIAO!!!，没有成倍开辟 100,000 的Integer！！！
2020-01-21:  
  测试代码可知，在for-each后执行了GC，并且后续并未使用`map`。所以，GC会回收map所占用的对象，所以剩余的 100,000 `java.lang.Integer`不是`map`时创建的。  
而是`new Integer(e.getKey())`产生的。
  测试代码参考`com.vergilyn.examples.GCTestng`。