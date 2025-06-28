package com.github.wallev.maidsoulkitchen.util.classana;

public interface IMaidsoulKitchenInterface {

    static boolean applyInterfaceMixin(Class<?> targetClass) {
        return IMaidsoulKitchenInterface.class.isAssignableFrom(targetClass);
    }

    static boolean applyInterfaceMixin(String targetClass) {
        try {
            return applyInterfaceMixin(Class.forName(targetClass));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

}
