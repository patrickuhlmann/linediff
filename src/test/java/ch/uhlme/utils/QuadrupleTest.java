package ch.uhlme.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

import org.junit.jupiter.api.Test;

@SuppressWarnings("rawtypes")
class QuadrupleTest {
  @Test
  void givenObjects_thenHashcode() {
    Quadruple t1 = new Quadruple<>("a", "b", "c", "d");
    Quadruple t2 = new Quadruple<>("a", "b", "c", "d");
    Quadruple t3 = new Quadruple<>("c", "d", "c", "d");

    assertThat(t1.hashCode(), is(t2.hashCode()));
    assertThat(t1.hashCode(), not(t3.hashCode()));
  }

  @Test
  void givenObjects_thenEquals() {
    final Quadruple t1 = new Quadruple<>("a", "b", "c", "d");
    final Quadruple t2 = new Quadruple<>("z", "b", "c", "d");
    final Quadruple t3 = new Quadruple<>("a", "z", "c", "d");
    final Quadruple t4 = new Quadruple<>("a", "b", "z", "d");
    final Quadruple t5 = new Quadruple<>("a", "b", "c", "z");
    final Quadruple t6 = new Quadruple<>("a", "b", "c", "z");
    final Quadruple t7 = new Quadruple<>("a", "b", "c", "d");

    assertThat(t1, not(""));
    assertThat(t1, is(t1));
    assertThat(t1, not(t2));
    assertThat(t1, not(t3));
    assertThat(t1, not(t4));
    assertThat(t1, not(t5));
    assertThat(t1, not(t6));
    assertThat(t1, is(t7));
  }

  @Test
  void givenObject_thenString() {
    Quadruple t1 = new Quadruple<>("a", "b", "c", "d");

    assertThat(t1.toString(), is("Quadruple{first=a, second=b, third=c, fourth=d}"));
  }
}
