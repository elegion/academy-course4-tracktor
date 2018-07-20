package com.elegion.tracktor.data;

import java.util.List;

/**
 * @author Azret Magometov
 */
public interface IRepository<T> {

    T getItem(long id);

    List<T> getAll();

    long insertItem(T t);

    boolean deleteItem(long id);

    void updateItem(T t);

}
