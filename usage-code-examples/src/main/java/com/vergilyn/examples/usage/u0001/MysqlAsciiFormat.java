package com.vergilyn.examples.usage.u0001;

/**
 * <a href="http://web.chacuo.net/charsettexttable">在线生成纯字符表格、在线ascii表格生成、csv转mysql 字符表格、txt转纯字符表格</a>
 *
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
 * @author vergilyn
 * @since 2022-01-26
 *
 * @see <a href="https://github.com/vdmeer/asciitable">vdmeer/asciitable</a>
 * @see <a href="https://github.com/freva/ascii-table">freva/ascii-table（推荐参考，需要解决 中英文对齐问题）</a>
 * @see <a href="https://github.com/MitchTalmadge/ASCII-Data">MitchTalmadge/ASCII-Data (基于`String.format`)</a>
 */
public class MysqlAsciiFormat {
	public static final char SEPARATOR_ANGLE = '+';
	public static final char SEPARATOR_HORIZONTAL = '-';
	public static final char SEPARATOR_VERTICAL = '|';



}
