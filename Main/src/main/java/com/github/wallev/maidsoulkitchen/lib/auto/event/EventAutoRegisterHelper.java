package com.github.wallev.maidsoulkitchen.lib.auto.event;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.util.AnnotationHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.moddiscovery.ModAnnotation;
import net.minecraftforge.forgespi.language.ModFileScanData;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EventAutoRegisterHelper {

    public static void autoSubscribeInterModEnqueueEvent(InterModEnqueueEvent event) {
        autoSubscribeParallelEvent(AutoInterModEnqueueEvent.class, event);
    }

    public static void autoSubscribeEventBus() {
        EventAutoRegisterHelper.autoRegister(AutoEventSubscriber.class, annotationData -> {
            subscribeFromEmptyObj(annotationData);
            subscribeFromMethod(annotationData);
        });
    }

    public static void autoSubscribeFMLCommonSetupEvent(FMLCommonSetupEvent event) {
        autoSubscribeParallelEvent(AutoFMLCommonSetupEvent.class, event);
    }

    public static <T extends Annotation, E extends ParallelDispatchEvent> void autoSubscribeParallelEvent(Class<T> clazz, E event) {
        EventAutoRegisterHelper.autoRegister(clazz, annotationData -> {
            event.enqueueWork(() -> {
                switch (annotationData.targetType()) {
                    case TYPE -> subscribeFromObj(annotationData, event);
                    case METHOD -> subscribeFromMethod(annotationData);
                }
            });
        });
    }

    private static void subscribeFromObj(ModFileScanData.AnnotationData annotationData, Event event) {
        if (subscribeFromEmptyObj(annotationData)) {
            return;
        }
        subscribeFromEventObj(annotationData, event);
    }

    private static boolean subscribeFromEmptyObj(ModFileScanData.AnnotationData annotationData) {
        try {
            String clazzName = annotationData.memberName();
            Class<?> asmClazz = Class.forName(clazzName);
            Constructor<?> constructor = asmClazz.getDeclaredConstructor();
            Object o = constructor.newInstance();
            MinecraftForge.EVENT_BUS.register(o);
            return true;
        } catch (ClassNotFoundException | InvocationTargetException |
                 InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private static void subscribeFromEventObj(ModFileScanData.AnnotationData annotationData, Event event) {
        try {
            String clazzName = annotationData.memberName();
            Class<?> asmClazz = Class.forName(clazzName);
            Constructor<?> constructor = asmClazz.getDeclaredConstructor(event.getClass());
            Object o = constructor.newInstance(event);
            MinecraftForge.EVENT_BUS.register(o);
        } catch (ClassNotFoundException | InvocationTargetException  |
                 InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException ignored) {
        }
    }

    // @TODO
    private static void subscribeFromMethod(ModFileScanData.AnnotationData annotationData) {
        try {
            String clazzName = annotationData.clazz().getClassName();
            Class<?> clazz = Class.forName(clazzName);
            MinecraftForge.EVENT_BUS.register(clazz);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static <T extends Annotation> void autoRegister(Class<T> eventAnnotationClazz, Consumer<ModFileScanData.AnnotationData> consumer) {
        AnnotationHelper.read(eventAnnotationClazz, annotationData -> {
            String modsStr = AnnotationHelper.getEnumHolderValue(annotationData, "mods");
            if (modsStr != null && !Mods.by(modsStr).versionLoad())
                return;

            @SuppressWarnings("unchecked")
            final List<ModAnnotation.EnumHolder> sidesValue = (List<ModAnnotation.EnumHolder>)annotationData.annotationData().
                    getOrDefault("value", Arrays.asList(new ModAnnotation.EnumHolder(null, "CLIENT"), new ModAnnotation.EnumHolder(null, "DEDICATED_SERVER")));
            final EnumSet<Dist> sides = sidesValue.stream().map(eh -> Dist.valueOf(eh.getValue())).
                    collect(Collectors.toCollection(() -> EnumSet.noneOf(Dist.class)));
            if (!sides.contains(FMLEnvironment.dist))
                return;

            consumer.accept(annotationData);
        });
    }
}
