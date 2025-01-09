package com.github.wallev.maidsoulkitchen.handler.serializer;

import com.github.wallev.maidsoulkitchen.handler.serializer.rule.AbstractCookRecSerializer;

public enum CookRecipes {
//    (new DefaultCookRecSerializer<>()),



    ;
    public final AbstractCookRecSerializer<?> cookRecSerializer;

    CookRecipes(AbstractCookRecSerializer<?> cookRecSerializer) {
        this.cookRecSerializer = cookRecSerializer;
    }
}
