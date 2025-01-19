package com.github.wallev.maidsoulkitchen.handler.api;

import com.github.wallev.maidsoulkitchen.handler.initializer.CookContainerSerializerRulesManager;
import com.github.wallev.maidsoulkitchen.handler.initializer.CookRecIngredientSerializerManager;
import com.github.wallev.maidsoulkitchen.handler.initializer.CookRecRecipeInitializerManager;

public interface IMaidsoulKitchen {
    /**
     * 注册逻辑处理器
     *
     * @param manager 注册器
     */
    default void addCookContainerSerializerRules(CookContainerSerializerRulesManager manager) {

    }

    /**
     * 注册配方序列化规则
     *
     * @param manager 注册器
     */
    default void addCookRecIngredientSerializer(CookRecIngredientSerializerManager manager) {

    }

    /**
     * 注册配方初始化器
     *
     * @param manager 注册器
     */
    default void addCookRecRecipeInitializer(CookRecRecipeInitializerManager manager) {

    }
}
