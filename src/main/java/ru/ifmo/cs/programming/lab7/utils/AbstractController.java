package ru.ifmo.cs.programming.lab7.utils;

import java.util.List;

public abstract class AbstractController <E, K> {
    public abstract List<E> getAll();
    public abstract E getEntityById(K id);
    public abstract E update(E entity);
    public abstract boolean delete(K id);
    public abstract boolean create(E entity);
}