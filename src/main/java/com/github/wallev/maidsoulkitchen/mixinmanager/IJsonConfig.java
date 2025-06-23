package com.github.wallev.maidsoulkitchen.mixinmanager;

public interface IJsonConfig<V>{
    void set(V value);
    V getDefault();
    V get();
    String getKey();
}
