package com.vergilyn.examples.usage.u0001;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vergilyn
 * @since 2021-04-23
 */
class TextOutputFormatTests {

	/**
	 * <pre>
	 *   Benchmark                               Mode  Cnt     Score     Error  Units
	 *   CollectionAddJMH.arrayList                ss    5     0.612 ±   1.338  ms/op
	 *   CollectionAddJMH.copyOnWriteArrayList     ss    5  2644.711 ± 152.747  ms/op
	 *   CollectionAddJMH.linkedList               ss    5     1.237 ±   0.313  ms/op
	 *   CollectionAddJMH.synchronizedArrayList    ss    5     2.002 ±   0.480  ms/op
	 *   CollectionAddJMH.vector                   ss    5     1.270 ±   0.882  ms/op
	 * </pre>
	 */
	@Test
	public void formatOutput(){
		String[] head = {"Benchmark", "Mode", "Cnt", "Score", "Error", "Units"};
		List<String[]> table = new ArrayList<>(16);

		table.add(new String[]{"CollectionAddJMH.arrayList", "ss", "5", "0.612 ±", "1.338", "ms/op"});
		table.add(new String[]{"CollectionAddJMH.copyOnWriteArrayList", "ss", "5", "2644.711 ±", "152.747", "ms/op"});
		table.add(new String[]{"CollectionAddJMH.linkedList", "ss", "5", "1.237 ±", "0.313", "ms/op"});
		table.add(new String[]{"CollectionAddJMH.synchronizedArrayList", "ss", "5", "2.002 ±", "0.480", "ms/op"});
		table.add(new String[]{"CollectionAddJMH.linkedList", "ss", "5", "1.270 秒", "0.882", "ms/op"});

		TextOutputFormat.print(head, table);
	}

	/**
	 * <a href="http://web.chacuo.net/charsettexttable">在线生成纯字符表格、在线ascii表格生成、csv转mysql 字符表格、txt转纯字符表格</a>
	 * <pre>
	 * id,name,age,sex,demo,test
	 * 1,Roberta,39,M,love,11111111111,试试中文
	 * 2,Oliver,25,M,live,zhong guo
	 * 3,Shayna,18,F,love,wu han,中文
	 * 4,Fechin,18,M
	 * </pre>
	 *
	 * mysql ascii: (notepad 看着都没问题，IDEA内看着没对其...)
	 * <pre>
	 * +----+---------+-----+-----+------+-------------+----------+
	 * | id | name    | age | sex | demo | test        |          |
	 * +----+---------+-----+-----+------+-------------+----------+
	 * | 1  | Roberta | 39  | M   | love | 11111111111 | 试试中文 |
	 * | 2  | Oliver  | 25  | M   | live | zhong guo   |          |
	 * | 3  | Shayna  | 18  | F   | love | wu han      | 中文     |
	 * | 4  | Fechin  | 18  | M   |      |             |          |
	 * +----+---------+-----+-----+------+-------------+----------+
	 * </pre>
	 *
	 *
	 */
	@Test
	public void chs(){
		String[] head = {"id", "name", "age", "sex", "demo", "test", "abc 文", "test"};
		List<String[]> table = new ArrayList<>(16);

		// [2, 7, 3, 3, 4, 11, 9, 11]
		// %-2s		%-7s		%-3s		%-3s		%-4s		%-11s		%-9s		%-11s
		// 2022-01-26, 无法解决 中文中间包含英文空格 。
		table.add(new String[]{"1", "Roberta", "39", "M", "love", "11111111111", "试试aa中文", "11111111111"});
		// table.add(new String[]{"2", "Oliver", "25", "M", "live", "zhong guo", "", "zhong guo"});
		table.add(new String[]{"3", "Shayna", "18", "F", "love", "wu han", "中 文", "wu han"});
		// table.add(new String[]{"4", "Fechin", "18", "M", "", "", "",""});

		TextOutputFormat.print(head, table);
	}

}