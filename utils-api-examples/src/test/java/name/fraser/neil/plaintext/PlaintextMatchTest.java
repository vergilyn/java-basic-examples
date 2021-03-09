package name.fraser.neil.plaintext;

import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author vergilyn
 * @since 2021-02-05
 *
 * @see <a href="https://github.com/google/diff-match-patch/tree/master/java/tests/name/fraser/neil/plaintext">google/diff-match-patch</a>
 */
public class PlaintextMatchTest {
	protected diff_match_patch dmp;

	@BeforeEach
	public void before(){
		dmp = new diff_match_patch();
		dmp.Diff_Timeout = 0;
	}

	@Test
	public void match(){
		String text1 = "123456";
		String text2 = "123F56";

		LinkedList<diff_match_patch.Diff> diffs = dmp.diff_main(text1, text2);

		diffs.forEach(System.out::println);

		System.out.println("=================");

		System.out.println(dmp.diff_text1(diffs));  // == text1
		System.out.println(dmp.diff_text2(diffs));  // == text2

		System.out.println(dmp.diff_prettyHtml(diffs));
		System.out.println(dmp.diff_toDelta(diffs));

	}

	@Test
	public void patch(){
		String text1 = "123456";
		String text2 = "123356";

		LinkedList<diff_match_patch.Patch> diffs = dmp.patch_make(text1, text2);

		diffs.forEach(System.out::println);
	}
}
