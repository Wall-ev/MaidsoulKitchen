package com.github.wallev.maidsoulkitchen.util;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModAnnotation;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class AnnotationHelper {

    public static <T extends Annotation> void read(Class<T> annotationType, Consumer<ModFileScanData.AnnotationData> consumer) {
        read(Type.getType(annotationType), consumer);
    }

    @SuppressWarnings("SameParameterValue")
    public static void read(Type annotationType, Consumer<ModFileScanData.AnnotationData> consumer) {
        ModList.get().getAllScanData().stream().flatMap(scanData -> scanData.getAnnotations().stream())
                .filter(annotationData -> Objects.equals(annotationData.annotationType(), annotationType))
                .forEach(consumer);
    }

    public static <T extends Annotation> Stream<ModFileScanData.AnnotationData> map(Class<T> annotationType) {
        return ModList.get().getAllScanData().stream().flatMap(scanData -> scanData.getAnnotations().stream())
                .filter(annotationData -> Objects.equals(annotationData.annotationType(), Type.getType(annotationType)));
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <T> T getHolderValue(ModFileScanData.AnnotationData data, String name) {
        return (T) data.annotationData().get(name);
    }

    @Nullable
    public static String getEnumHolderValue(ModFileScanData.AnnotationData data, String name) {
        ModAnnotation.EnumHolder o = getHolderValue(data, name);
        return o != null ? o.getValue() : null;
    }

    /**
     * 通过类名和字段名反射获取字段对象
     */
    public static Field getAnnotatedField(String className, String fieldName) {
        try {
            Class<?> clazz = Class.forName(className);
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("[TaskErrorLang]ClassNotFoundException: " + className, e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("[TaskErrorLang]NoSuchFieldException: " + className + "#" + fieldName, e);
        } catch (Exception e) {
            throw new RuntimeException("[TaskErrorLang]Exception: " + className + "#" + fieldName, e);
        }
    }

    /**
     * 通过类名和字段名反射获取字段对象
     */
    public static Field getAnnotatedField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("[TaskErrorLang]NoSuchFieldException: " + clazz + "#" + fieldName, e);
        } catch (Exception e) {
            throw new RuntimeException("[TaskErrorLang]Exception: " + clazz + "#" + fieldName, e);
        }
    }

}
