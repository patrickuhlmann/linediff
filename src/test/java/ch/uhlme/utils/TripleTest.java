package ch.uhlme.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("rawtypes")
class TripleTest {
  @Test
  void givenObjects_thenHashcode() {
    Triple t1 = new Triple<>("a", "b", "c");
    Triple t2 = new Triple<>("a", "b", "c");
    Triple t3 = new Triple<>("c", "d", "c");

    assertThat(t1.hashCode(), is(t2.hashCode()));
    assertThat(t1.hashCode(), not(t3.hashCode()));
  }

  @Test
  void givenObjects_thenEquals() {
    final Triple t1 = new Triple<>("a", "b", "c");
    final Triple t2 = new Triple<>("z", "b", "c");
    final Triple t3 = new Triple<>("a", "z", "c");
    final Triple t4 = new Triple<>("a", "b", "z");
    final Triple t5 = new Triple<>("a", "b", "c");

    assertThat(t1, not(""));
    assertThat(t1, is(t1));
    assertThat(t1, not(t2));
    assertThat(t1, not(t3));
    assertThat(t1, not(t4));
    assertThat(t1, is(t5));
  }

  @Test
  void givenObject_thenToString() {
    Triple t1 = new Triple<>("a", "b", "c");

    Assertions.assertEquals("Triple{first=a, second=b, third=c}", t1.toString());
  }
}
