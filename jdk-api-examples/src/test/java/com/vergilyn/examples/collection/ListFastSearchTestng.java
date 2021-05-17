package com.vergilyn.examples.collection;

import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.RandomUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

/**
 * @author vergilyn
 * @date 2020-04-01
 */
public class ListFastSearchTestng {
    private List<JavaBean> beans;
    private List<Integer> ids;

    @BeforeTest
    public void beforeTest(){
        int size = 100;

        beans = Stream.iterate(new JavaBean(1), JavaBean::new)
                .limit(100).collect(Collectors.toList());

        ids = Lists.newArrayList();
        while (size-- > 0){
            if (RandomUtils.nextBoolean()){
                ids.add(size);
            }
        }

        ids.add(0, 102);
        ids.add(0, 110);
    }

    /**
     * 二分法快速查找，前提list有序！<br/>
     * 根据 复杂对象 中某个property进行排序，并将其作为 查找key。
     * <p>
     *   {@linkplain java.util.Collections#binarySearch(List, Object, java.util.Comparator)} - jdk实现，但只支持`Comparator<? super T> c` <br/>
     *   {@linkplain org.apache.commons.collections4.CollectionUtils#select(Iterable, Predicate)} - apache-common-collections4，简单的for遍历，不满足需求
     * </p>
     */
    @Test
    public void doubleIterator(){
        for (Integer id : ids){
            int index = iteratorBinarySearch(beans, id, (o1, o2) -> o1.getId().compareTo(o2));

            if (index < 0){
                System.out.printf("[id: %d] is not exists. \r\n", id);
            }else {
                JavaBean bean = beans.get(index);
                System.out.printf("[id: %d] >>>> %s \r\n", id, bean);
            }
        }
    }

    /**
     * 根据 {@linkplain java.util.Collections#indexedBinarySearch(List, Object, java.util.Comparator)} 扩展。
     * <br/> 只是需要修改`V key` 和 `Comparator<? super T, V> c` 即可。
     *
     * <br/> 特别：jdk 中针对 `!(list instanceof RandomAccess || list.size() < 5000)` 的查找有特殊优化！
     * @param c 扩展为`Comparator<? super T, V> c`
     * @see java.util.Collections#indexedBinarySearch(List, Object, java.util.Comparator)
     * @see java.util.Collections#iteratorBinarySearch(List, Object, java.util.Comparator)
     */
    private static <T,V> int iteratorBinarySearch(List<? extends T> l, V key, Comparator<? super T, V> c) {
        int low = 0;
        int high = l.size()-1;
        ListIterator<? extends T> i = l.listIterator();

        while (low <= high) {
            int mid = (low + high) >>> 1;

            /* jdk 针对`!(list instanceof RandomAccess || list.size() < 5000)`的特殊优化
             * 否则`T midVal = list.get(mid);`
             */
            T midVal = get(i, mid);

            int cmp = c.compare(midVal, key);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1);  // key not found
    }

    /**
     * @see java.util.Collections#get(ListIterator, int)
     */
    private static <T> T get(ListIterator<? extends T> i, int index) {
        T obj = null;
        int pos = i.nextIndex();
        if (pos <= index) {
            do {
                obj = i.next();
            } while (pos++ < index);
        } else {
            do {
                obj = i.previous();
            } while (--pos > index);
        }
        return obj;
    }

    /**
     * @see java.util.Comparator
     */
    @FunctionalInterface
    interface Comparator<T, V>{
        int compare(T o1, V o2);
    }

    @Data
    @NoArgsConstructor
    @ToString
    class JavaBean {
        private Integer id;
        private String name;

        public JavaBean(Integer id) {
            this.id = id;
            this.name = "name - " + id;
        }

        public JavaBean(JavaBean bean) {
            this(bean.id + 1);
        }
    }
}
