package ch.uhlme.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.DataflowAnomalyAnalysis"})
class ByteCountTest {
    @Test
    @DisplayName("all level up to terabytes")
    void bytesToLotsOfGigabytes() {
        List<Long> inputs = Arrays.asList(
                512L,
                1000L + 950L,
                2L * 1000L,
                3L * 1000L * 1000L,
                4L * 1000L * 1000L * 1000L,
                5L * 1000L * 1000L * 1000L * 1000L,
                -512L,
                -2 * 1000L,
                -3 * 1000 * 1000L,
                -4 * 1000 * 1000 * 1000L,
                -2 * 1000 * 1000 * 1000 * 1000L
        );

        List<String> outputs = Arrays.asList(
                "512 B",
                "2.0 kB",
                "2.0 kB",
                "3.0 MB",
                "4.0 GB",
                "5.0 TB",
                "-512 B",
                "-2.0 kB",
                "-3.0 MB",
                "-4.0 GB",
                "-2.0 TB"
        );

        for (int i=0; i<inputs.size(); i++) {
            ByteCount bc = new ByteCount(inputs.get(i));
            assertThat(bc.toString(), is(outputs.get(i)));
            assertThat(bc.getCount(), is(inputs.get(i)));
        }
    }
}
