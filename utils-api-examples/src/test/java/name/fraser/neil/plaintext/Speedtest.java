package name.fraser.neil.plaintext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

/**
 *
 * @author vergilyn
 * @since 2021-02-05
 *
 * @see <a href="https://github.com/google/diff-match-patch/tree/master/java/tests/name/fraser/neil/plaintext">google/diff-match-patch</a>
 */
public class Speedtest {

	@Test
	public void diff(){
		String text1 = readFile("speed_test_1.txt");
		String text2 = readFile("speed_test_2.txt");

		diff_match_patch dmp = new diff_match_patch();
		dmp.Diff_Timeout = 0;

		// Execute one reverse diff as a warmup.
		dmp.diff_main(text2, text1, false);

		long start_time = System.nanoTime();
		LinkedList<diff_match_patch.Diff> diffs = dmp.diff_main(text1, text2, false);
		long end_time = System.nanoTime();
		System.out.printf("Elapsed time: %d ms\n", TimeUnit.NANOSECONDS.toMillis(end_time - start_time));

		diffs.forEach(diff -> {
			System.out.println(diff.toString());
		});
	}

	private String readFile(String filename){
		URL url = Speedtest.class.getClassLoader().getResource(filename);

		StringBuilder sb = new StringBuilder();
		try(
			// Read a file from disk and return the text contents.
			FileReader input = new FileReader(new File(url.toURI()));
			BufferedReader bufRead = new BufferedReader(input)) {

			String line = bufRead.readLine();
			while (line != null) {
				sb.append(line).append('\n');
				line = bufRead.readLine();
			}
		} catch (Exception e){
			e.printStackTrace();
		}

		return sb.toString();
	}
}
