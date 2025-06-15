package com.handicrafts.repository;

import java.util.List;

public interface IRepository<T> {
    List<T> getAll();
    int create(T t);
    int update(T t);
    int delete(int[] ids);
}
