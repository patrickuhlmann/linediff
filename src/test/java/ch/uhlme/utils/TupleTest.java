package ch.uhlme.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

import org.junit.jupiter.api.Test;

@SuppressWarnings("rawtypes")
class TupleTest {
  @Test
  void givenObjects_thenHashcode() {
    Tuple t1 = new Tuple<>("a", "b");
    Tuple t2 = new Tuple<>("a", "b");
    Tuple t3 = new Tuple<>("c", "d");

    assertThat(t1.hashCode(), is(t2.hashCode()));
    assertThat(t1.hashCode(), not(t3.hashCode()));
  }

  @Test
  void givenObjects_thenEquals() {
    Tuple t1 = new Tuple<>("a", "b");
    Tuple t2 = new Tuple<>("z", "b");
    final Tuple t3 = new Tuple<>("a", "z");
    final Tuple t4 = new Tuple<>("a", "b");

    assertThat(t1, not(""));
    assertThat(t1, is(t1));
    assertThat(t1, not(t2));
    assertThat(t1, not(t3));
    assertThat(t1, is(t4));
  }

  @Test
  void checkToString() {
    Tuple t1 = new Tuple<>("a", "b");

    assertThat(t1.toString(), is("Tuple{first=a, second=b}"));
  }
}
