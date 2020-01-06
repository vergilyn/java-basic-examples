package com.vergilyn.examples;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * {@linkplain java.util.ArrayList}、{@linkplain java.util.LinkedList} 几种遍历方式的性能比较；
 * <p>
 * 针对`ArrayList`，TYPE1的效率普遍比TYPE4-5低。
 *
 * <p>2020-01-03 注意测试代码之间有影响
 * @author VergiLyn
 * @date 2020-01-03
 * @see <a href="https://www.trinea.cn/android/arraylist-linkedlist-loop-performance/">ArrayList和LinkedList的几种循环遍历方式及性能对比分析</a>
 */
public class ArrayListAndLinkedListLoopPerformanceTest {

    public static void main(String[] args) {

        System.out.print("compare loop performance of ArrayList");
        loopListCompare(getArrayLists(1_0000, 10_0000, 100_0000, 1000_0000));

        System.out.print("\r\n\r\ncompare loop performance of LinkedList");
        loopListCompare(getLinkedLists(100, 1000, 10000, 100000));
    }

    public static List<Integer>[] getArrayLists(int... sizeArray) {
        List<Integer>[] listArray = new ArrayList[sizeArray.length];
        for (int i = 0; i < listArray.length; i++) {
            int size = sizeArray[i];
            List<Integer> list = new ArrayList<>(size);
            for (int j = 0; j < size; j++) {
                list.add(j);
            }
            listArray[i] = list;
        }
        return listArray;
    }

    public static List<Integer>[] getLinkedLists(int... sizeArray) {
        List<Integer>[] listArray = new LinkedList[sizeArray.length];
        for (int i = 0; i < listArray.length; i++) {
            int size = sizeArray[i];
            List<Integer> list = new LinkedList<Integer>();
            for (int j = 0; j < size; j++) {
                list.add(j);
            }
            listArray[i] = list;
        }
        return listArray;
    }

    public static void loopListCompare(List<Integer>... listArray) {
        printHeader(listArray);
        long startTime, endTime;

        // Type 1
        for (int i = 0; i < listArray.length; i++) {
            List<Integer> list = listArray[i];
            startTime = System.currentTimeMillis();
            for (Integer j : list) {
                // use j
            }
            endTime = System.currentTimeMillis();
            printCostTime(i, listArray.length, "for each", endTime - startTime);
        }

        // Type 2
        for (int i = 0; i < listArray.length; i++) {
            List<Integer> list = listArray[i];
            startTime = System.currentTimeMillis();
            // Iterator<Integer> iterator = list.iterator();
            // while(iterator.hasNext()) {
            // iterator.next();
            // }
            for (Iterator<Integer> iterator = list.iterator(); iterator.hasNext();) {
                iterator.next();
            }
            endTime = System.currentTimeMillis();
            printCostTime(i, listArray.length, "for iterator", endTime - startTime);
        }

        // Type 3
        for (int i = 0; i < listArray.length; i++) {
            List<Integer> list = listArray[i];
            startTime = System.currentTimeMillis();
            for (int j = 0; j < list.size(); j++) {
                list.get(j);
            }
            endTime = System.currentTimeMillis();
            printCostTime(i, listArray.length, "for list.size()", endTime - startTime);
        }

        // Type 4
        for (int i = 0; i < listArray.length; i++) {
            List<Integer> list = listArray[i];
            startTime = System.currentTimeMillis();
            int size = list.size();
            for (int j = 0; j < size; j++) {
                list.get(j);
            }
            endTime = System.currentTimeMillis();
            printCostTime(i, listArray.length, "for size = list.size()", endTime - startTime);
        }

        // Type 5
        for (int i = 0; i < listArray.length; i++) {
            List<Integer> list = listArray[i];
            startTime = System.currentTimeMillis();
            for (int j = list.size() - 1; j >= 0; j--) {
                list.get(j);
            }
            endTime = System.currentTimeMillis();
            printCostTime(i, listArray.length, "for j--", endTime - startTime);
        }

        // Type 6
        /*for (int i = 0; i < listArray.length; i++) {
            List<Integer> list = listArray[i];
            startTime = System.currentTimeMillis();
            list.forEach(e -> { });
            endTime = System.currentTimeMillis();
            printCostTime(i, listArray.length, "stream", endTime - startTime);
        }*/
    }

    static int FIRST_COLUMN_LENGTH = 23, OTHER_COLUMN_LENGTH = 12, TOTAL_COLUMN_LENGTH = 71;
    static final DecimalFormat COMMA_FORMAT = new DecimalFormat("#,###");

    public static void printHeader(List<Integer>... listArray) {
        printRowDivider();
        for (int i = 0; i < listArray.length; i++) {
            if (i == 0) {
                StringBuilder sb = new StringBuilder().append("list size");
                while (sb.length() < FIRST_COLUMN_LENGTH) {
                    sb.append(" ");
                }
                System.out.print(sb);
            }

            StringBuilder sb = new StringBuilder().append("| ").append(COMMA_FORMAT.format(listArray[i].size()));
            while (sb.length() < OTHER_COLUMN_LENGTH) {
                sb.append(" ");
            }
            System.out.print(sb);
        }
        TOTAL_COLUMN_LENGTH = FIRST_COLUMN_LENGTH + OTHER_COLUMN_LENGTH * listArray.length;
        printRowDivider();
    }

    public static void printRowDivider() {
        System.out.println();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < TOTAL_COLUMN_LENGTH) {
            sb.append("-");
        }
        System.out.println(sb);
    }

    public static void printCostTime(int i, int size, String caseName, long costTime) {
        if (i == 0) {
            StringBuilder sb = new StringBuilder().append(caseName);
            while (sb.length() < FIRST_COLUMN_LENGTH) {
                sb.append(" ");
            }
            System.out.print(sb);
        }

        StringBuilder sb = new StringBuilder().append("| ").append(costTime).append(" ms");
        while (sb.length() < OTHER_COLUMN_LENGTH) {
            sb.append(" ");
        }
        System.out.print(sb);

        if (i == size - 1) {
            printRowDivider();
        }
    }

}
