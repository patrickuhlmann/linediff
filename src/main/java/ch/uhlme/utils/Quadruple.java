package ch.uhlme.utils;

import java.util.Objects;

public class Quadruple<X, Y, Z, W> {
    private final X first;
    private final Y second;
    private final Z third;
    private final W fourth;

    public Quadruple(X first, Y second, Z third, W fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
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

    public W getFourth() {
        return fourth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quadruple<?, ?, ?, ?> quadruple = (Quadruple<?, ?, ?, ?>) o;
        return Objects.equals(first, quadruple.first)
                && Objects.equals(second, quadruple.second)
                && Objects.equals(third, quadruple.third)
                && Objects.equals(fourth, quadruple.fourth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third, fourth);
    }

    @Override
    public String toString() {
        return "Quadruple{"
                + "first="
                + first
                + ", second="
                + second
                + ", third="
                + third
                + ", fourth="
                + fourth
                + '}';
    }
}
