package com.example.final_project_faz3.maktab.ir.util.validation;

@FunctionalInterface
public interface TriConsumer<T, U, V> {
    void accept(T t, U u, V v);
}

