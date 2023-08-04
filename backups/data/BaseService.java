package com.benardmathu.hfms.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bernard
 */
public interface BaseService<T> {
    T save(T item) ;
    T update(T item);
    void delete(T item);
    T get(Long id);
    List<T> getAll();
    List<T> saveAll(ArrayList<T> items);
}
