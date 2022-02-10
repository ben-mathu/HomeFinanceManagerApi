package com.benardmathu.hfms.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bernard
 */
public interface BaseService<T> {
    T save(T item) ;
    int update(T item);
    int delete(T item);
    T get(String id);
    List<T> getAll();
    List<T> getAll(String id);
    int saveAll(ArrayList<T> items);
}
