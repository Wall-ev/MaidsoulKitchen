package com.github.wallev.maidsoulkitchen.mixinmanager.legacy;

public interface IJsonConfig<V>{
    void set(V value);
    V getDefault();
    V get();
    String getKey();
}
