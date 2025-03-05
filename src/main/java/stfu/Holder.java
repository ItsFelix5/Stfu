package stfu;

public interface Holder<T> {
    T stfu$get();
    void stfu$set(T value);
}
