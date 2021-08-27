package at.binter.gcd.util;

public interface Converter<E> {
    boolean showDeAndEncoded();

    E encode(E input);

    E decode(E input);
}