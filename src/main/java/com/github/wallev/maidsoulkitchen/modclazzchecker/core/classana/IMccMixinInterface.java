package com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana;

public interface IMccMixinInterface {

    static boolean applyInterfaceMixin(Class<?> targetClass) {
        return IMccMixinInterface.class.isAssignableFrom(targetClass);
    }

    static boolean applyInterfaceMixin(String targetClass) {
        try {
            return applyInterfaceMixin(Class.forName(targetClass, false, IMccMixinInterface.class.getClassLoader()));
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
