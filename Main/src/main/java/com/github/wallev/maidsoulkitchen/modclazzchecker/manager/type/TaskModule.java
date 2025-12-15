package com.github.wallev.maidsoulkitchen.modclazzchecker.manager.type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TaskModule {

    Type type();

    enum Type {
        DEV_MODULE,
        BERRY_MODULE,
        FRUIT_MODULE,
        FARM_MODULE,
        LAYER_MODULE,
        KITCHEN_MODULE,
        MAID_STORAGE_MANAGER_COMPAT_MODULE,
    }

}
