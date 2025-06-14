package com.handicrafts.repository.intf;

import java.util.List;

public interface IDao<T> {
    List<T> getAll();
    int create(T t);
    int update(T t);
    int delete(int[] ids);
}
