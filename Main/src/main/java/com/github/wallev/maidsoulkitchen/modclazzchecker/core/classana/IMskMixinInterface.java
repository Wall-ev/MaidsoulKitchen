package com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana;

public interface IMskMixinInterface {

    static boolean applyInterfaceMixin(Class<?> targetClass) {
        return IMskMixinInterface.class.isAssignableFrom(targetClass);
    }

    static boolean applyInterfaceMixin(String targetClass) {
        try {
            return applyInterfaceMixin(Class.forName(targetClass, false, IMskMixinInterface.class.getClassLoader()));
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
