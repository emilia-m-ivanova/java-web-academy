package dao;


public interface KeyGenerator<K> {
    K getNextId();
    void setInitialValue(K initialValue);
}
