package dao;


import java.util.List;

public interface IDao<T> {
    void save(T t);
    void update(T t);
    void delete(String id);
    T get(String id);
    List<T> getAll();
}
