package com.github.wallev.maidsoulkitchen.util;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModAnnotation;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class AnnotationHelper {
    private static final Marker MARKER = MarkerManager.getMarker("AnnotationAutoRead");

    public static <T extends Annotation> void read(Class<T> annotationType, Consumer<ModFileScanData.AnnotationData> consumer) {
        read(Type.getType(annotationType), consumer);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Annotation, O> void readWithObj(Class<T> annotationType, Consumer<O> obj, Object... params) {
        Set<Class<?>> objs = new HashSet<>();
        read(Type.getType(annotationType), annotationData -> {
            try {
                String clazzName = annotationData.memberName();
                Class<?> asmClazz = Class.forName(clazzName);
                if (objs.contains(asmClazz))
                    return;
                objs.add(asmClazz);
                Constructor<?> constructor = asmClazz.getDeclaredConstructor(Arrays.stream(params).map(Object::getClass).toArray(Class[]::new));
                O o = (O) constructor.newInstance(params);
                obj.accept(o);
            } catch (ClassNotFoundException | InvocationTargetException |
                     InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException ignored) {
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <T extends Annotation, O> void readWithObjAndTaskLoad(Class<T> annotationType, String taskFieldName, Consumer<O> obj, Object... params) {
        Set<Class<?>> objs = new HashSet<>();
        read(Type.getType(annotationType), annotationData -> {
            String taskVal = getEnumHolderValue(annotationData, taskFieldName);
            try {
                if (taskVal == null) {
                    MaidsoulKitchen.LOGGER.error(MARKER, "Failed to load task: {}", taskVal);
                    return;
                }

                if (!TaskInfo.valueOf(taskVal).canLoad()) {
                    MaidsoulKitchen.LOGGER.error(MARKER, "Failed to load task: {} because mod {} is not loaded", taskVal, TaskInfo.valueOf(taskVal));
                    return;
                }

                String clazzName = annotationData.memberName();
                Class<?> asmClazz = Class.forName(clazzName);
                if (objs.contains(asmClazz))
                    return;
                objs.add(asmClazz);

                O o;
                if (Arrays.stream(asmClazz.getDeclaredConstructors()).noneMatch(constructor1 -> Arrays.equals(constructor1.getParameterTypes(), Arrays.stream(params).map(Object::getClass).toArray(Class[]::new)))) {
                    o = (O) asmClazz.getDeclaredConstructor().newInstance();
                } else {
                    Constructor<?> constructor = asmClazz.getDeclaredConstructor(Arrays.stream(params).map(Object::getClass).toArray(Class[]::new));
                    o = (O) constructor.newInstance(params);
                }
                obj.accept(o);
            } catch (ClassNotFoundException | InvocationTargetException |
                     InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                MaidsoulKitchen.LOGGER.error(MARKER, "Failed to load task: {} because mod {} is not loaded", taskVal, taskVal);
                e.printStackTrace(System.err);
            }
        });
    }

    public static <T extends Annotation, O> void readWithObjAndTaskLoad(Class<T> annotationType, Consumer<O> obj, Object... params) {
        readWithObjAndTaskLoad(annotationType, "value", obj, params);
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
