package util;

public interface Builder<T> {

    <Me extends Builder<T>> Me reset();

    T build();
}
