package ch.uhlme.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("rawtypes")
public class TripleTest {
    @Test
    @DisplayName("hashcode")
    public void checkHashCode() {
        Triple t1 = new Triple<>("a", "b", "c");
        Triple t2 = new Triple<>("a", "b", "c");
        Triple t3 = new Triple<>("c", "d", "c");

        Assertions.assertEquals(t1.hashCode(), t2.hashCode());
        Assertions.assertNotEquals(t1.hashCode(), t3.hashCode());
    }

    @Test
    @DisplayName("equals")
    public void checkEquals() {
        Triple t1 = new Triple<>("a", "b", "c");
        Triple t2 = new Triple<>("z", "b", "c");
        Triple t3 = new Triple<>("a", "z", "c");
        Triple t4 = new Triple<>("a", "b", "z");
        Triple t5 = new Triple<>("a", "b", "c");

        Assertions.assertEquals(t1, t1);
        Assertions.assertNotEquals(t1, "");
        Assertions.assertNotEquals(t1, t2);
        Assertions.assertNotEquals(t1, t3);
        Assertions.assertNotEquals(t1, t4);
        Assertions.assertEquals(t1, t5);

    }

    @Test
    @DisplayName("toString")
    public void checkToString() {
        Triple t1 = new Triple<>("a", "b", "c");

        Assertions.assertEquals("Triple{first=a, second=b, third=c}", t1.toString());
    }
}
