package ch.uhlme.utils;

public class Quadruple<X, Y, Z, W> {
    public final X first;
    public final Y second;
    public final Z third;
    public final W fourth;

    public Quadruple(X first, Y second, Z third, W fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }
}