package ch.uhlme.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("rawtypes")
public class QuadrupleTest {
    @Test
    @DisplayName("hashcode")
    public void checkHashCode() {
        Quadruple t1 = new Quadruple<>("a", "b", "c", "d");
        Quadruple t2 = new Quadruple<>("a", "b", "c", "d");
        Quadruple t3 = new Quadruple<>("c", "d", "c", "d");

        Assertions.assertEquals(t1.hashCode(), t2.hashCode());
        Assertions.assertNotEquals(t1.hashCode(), t3.hashCode());
    }

    @Test
    @DisplayName("equals")
    public void checkEquals() {
        Quadruple t1 = new Quadruple<>("a", "b", "c", "d");
        Quadruple t2 = new Quadruple<>("a", "b", "c", "d");
        Quadruple t3 = new Quadruple<>("c", "d", "c", "d");

        Assertions.assertEquals(t1, t2);
        Assertions.assertNotEquals(t1, t3);
    }

    @Test
    @DisplayName("toString")
    public void checkToString() {
        Quadruple t1 = new Quadruple<>("a", "b", "c", "d");

        Assertions.assertEquals("Quadruple{first=a, second=b, third=c, fourth=d}", t1.toString());
    }
}
