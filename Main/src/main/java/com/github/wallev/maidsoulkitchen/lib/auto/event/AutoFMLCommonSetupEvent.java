package com.github.wallev.maidsoulkitchen.lib.auto.event;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import net.minecraftforge.api.distmarker.Dist;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface AutoFMLCommonSetupEvent {

    Mods mods() default Mods.MSK;
    Dist[] value() default { Dist.CLIENT, Dist.DEDICATED_SERVER };

}
