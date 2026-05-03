package com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModLang {
    Mods value() default Mods.MC;
    String en_us() default "";
    String zh_cn() default "";
    boolean custom() default false;
}
