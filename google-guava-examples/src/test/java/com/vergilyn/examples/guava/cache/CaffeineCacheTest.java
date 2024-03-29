package com.vergilyn.examples.guava.cache;

import com.google.common.collect.ImmutableSet;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testng.AssertJUnit.assertFalse;

/**
 * @author VergiLyn
 * @date 2019-06-20
 */
public class CaffeineCacheTest {

    @Test
    public void test(){
        int numInsertions = 100_0000;

        BloomFilter<String> bf = BloomFilter.create(Funnels.unencodedCharsFunnel(), numInsertions);

        // Insert "numInsertions" even numbers into the BF.
        for (int i = 0; i < numInsertions * 2; i += 2) {
            bf.put(Integer.toString(i));
        }
        assertApproximateElementCountGuess(bf, numInsertions);

        // Assert that the BF "might" have all of the even numbers.
        for (int i = 0; i < numInsertions * 2; i += 2) {
            assertThat(bf.mightContain(Integer.toString(i))).isTrue();
        }

        // Now we check for known false positives using a set of known false positives.
        // (These are all of the false positives under 900.)
        ImmutableSet<Integer> falsePositives =
                ImmutableSet.of(
                        49, 51, 59, 163, 199, 321, 325, 363, 367, 469, 545, 561, 727, 769, 773, 781);
        for (int i = 1; i < 900; i += 2) {
            if (!falsePositives.contains(i)) {
                assertFalse("BF should not contain " + i, bf.mightContain(Integer.toString(i)));
            }
        }

        // Check that there are exactly 29824 false positives for this BF.
        int knownNumberOfFalsePositives = 29824;
        int numFpp = 0;
        for (int i = 1; i < numInsertions * 2; i += 2) {
            if (bf.mightContain(Integer.toString(i))) {
                numFpp++;
            }
        }
        assertEquals(knownNumberOfFalsePositives, numFpp);
        double expectedReportedFpp = (double) knownNumberOfFalsePositives / numInsertions;
        double actualReportedFpp = bf.expectedFpp();
        assertEquals(expectedReportedFpp, actualReportedFpp, 0.00015);
    }

    private static void assertApproximateElementCountGuess(BloomFilter<?> bf, int sizeGuess) {
        assertEquals(bf.approximateElementCount(), greaterThan((long) (sizeGuess * 0.99)));
        assertEquals(bf.approximateElementCount(), lessThan((long) (sizeGuess * 1.01)));
    }
}
