package com.DAO;


import java.io.Serializable;
import java.util.List;

public interface JpaDao<T, ID extends Serializable> {
    T save(T entity);
    T update(T entity);
    void delete(T entity);
    T findById(ID id);
    List<T> findAll();
    void flush();
}
