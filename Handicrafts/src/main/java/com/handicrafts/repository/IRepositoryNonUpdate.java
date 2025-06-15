package com.handicrafts.repository;

import java.util.List;

public interface IRepositoryNonUpdate<T> {
    List<T> getAll();
    int create(T t);
    int delete(int[] ids);
}
