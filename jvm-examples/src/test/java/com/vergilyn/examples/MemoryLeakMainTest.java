package com.vergilyn.examples;

import java.util.Arrays;
import java.util.EmptyStackException;

/**
 * @author vergilyn
 * @date 2020-05-11
 */
public class MemoryLeakMainTest {

    /**
     * VM options: -Xms5m -Xmx5m
     *
     * @see <a href="http://www.javacui.com/Theory/376.html">Java代码中获取运行时内存情况</a>
     */
    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();

        // System.out.printf("maxMemory: %d, totalMemory: %d, freeMemory: %d \r\n", maxMemory, totalMemory, freeMemory);

        byte[] m1 = new byte[(int) (runtime.freeMemory() - 1_000_000)];

        byte[] m2 = new byte[1_000_000];
        System.out.println("end");
    }



    /**
     * <pre>
     *   以下代码实现了一个栈（先进后出（FILO））结构。
     *   乍看之下似乎没有什么明显的问题，它甚至可以通过你编写的各种单元测试。
     *   然而其中的 {@linkplain MyStack#pop()} 方法却存在内存泄露(memory-leak)的问题。
     *
     *   当我们用 pop 方法弹出栈中的对象时，该对象不会被当作垃圾回收，即使使用栈的程序不再引用这些对象，
     *   因为栈内部维护着对这些对象的过期引用（obsolete reference）。
     *
     *   在支持垃圾回收的语言中，内存泄露是很隐蔽的，这种内存泄露其实就是无意识的对象保持。
     *   如果一个对象引用被无意识的保留起来了，那么垃圾回收器不会处理这个对象，也不会处理该对象引用的其他对象，
     *   即使这样的对象只有少数几个，也可能会导致很多的对象被排除在垃圾回收之外，从而对性能造成重大影响，
     *   极端情况下会引发 Disk Paging（物理内存与硬盘的虚拟内存交换数据），甚至造成 OutOfMemoryError。
     * </pre>
     */
    public class MyStack<T> {
        private T[] elements;
        private int size = 0;
        private static final int INIT_CAPACITY = 16;
        public MyStack() {
            elements = (T[]) new Object[INIT_CAPACITY];
        }

        public void push(T elem) {
            ensureCapacity();
            elements[size++] = elem;
        }
        public T pop() {
            if(size == 0) throw new EmptyStackException();
            return elements[--size];
        }
        private void ensureCapacity() {
            if(elements.length == size) {
                elements = Arrays.copyOf(elements, 2 * size + 1);
            }
        }
    }
}
