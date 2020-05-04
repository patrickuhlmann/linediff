package ch.uhlme.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("rawtypes")
public class TupleTest {
    @Test
    @DisplayName("hashcode")
    public void checkHashCode() {
        Tuple t1 = new Tuple<>("a", "b");
        Tuple t2 = new Tuple<>("a", "b");
        Tuple t3 = new Tuple<>("c", "d");

        Assertions.assertEquals(t1.hashCode(), t2.hashCode());
        Assertions.assertNotEquals(t1.hashCode(), t3.hashCode());
    }

    @Test
    @DisplayName("equals")
    public void checkEquals() {
        Tuple t1 = new Tuple<>("a", "b");
        Tuple t2 = new Tuple<>("a", "b");
        Tuple t3 = new Tuple<>("c", "d");

        Assertions.assertEquals(t1, t2);
        Assertions.assertNotEquals(t1, t3);
    }

    @Test
    @DisplayName("toString")
    public void checkToString() {
        Tuple t1 = new Tuple<>("a", "b");

        Assertions.assertEquals("Tuple{first=a, second=b}", t1.toString());
    }
}
