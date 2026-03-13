package com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.clazz.ClassAnalysisResult;

public interface IMskMixinInterface {

    static boolean applyInterfaceMixin(Class<?> targetClass) {
        return IMskMixinInterface.class.isAssignableFrom(targetClass);
    }

    static boolean applyInterfaceMixin(String targetClass, ClassAnalysisResult result) {
        try {
            Class<?> target = Class.forName(targetClass, false, IMskMixinInterface.class.getClassLoader());
            return applyInterfaceMixin(target);
        } catch (ClassNotFoundException | ClassCastException e) {
            return false;
        }
    }

}
