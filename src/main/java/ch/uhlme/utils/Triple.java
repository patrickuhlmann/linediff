package ch.uhlme.utils;

import java.util.Objects;

public class Triple<X, Y, Z> {
  private final X first;
  private final Y second;
  private final Z third;

  /**
   * Just a container storing three elements.
   *
   * @param first first element
   * @param second second element
   * @param third third element
   */
  public Triple(X first, Y second, Z third) {
    this.first = first;
    this.second = second;
    this.third = third;
  }

  public X getFirst() {
    return first;
  }

  public Y getSecond() {
    return second;
  }

  public Z getThird() {
    return third;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;
    return Objects.equals(first, triple.first)
        && Objects.equals(second, triple.second)
        && Objects.equals(third, triple.third);
  }

  @Override
  public int hashCode() {
    return Objects.hash(first, second, third);
  }

  @Override
  public String toString() {
    return "Triple{" + "first=" + first + ", second=" + second + ", third=" + third + '}';
  }
}
