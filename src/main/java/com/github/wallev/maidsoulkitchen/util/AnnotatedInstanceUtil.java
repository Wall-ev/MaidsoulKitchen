package com.github.wallev.maidsoulkitchen.util;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.handler.api.IMaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.handler.api.TLMKPlugin;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import java.util.*;

/**
 * Copy from https://github.com/mezz/JustEnoughItems/blob/1.16/src/main/java/mezz/jei/util/AnnotatedInstanceUtil.java
 */
public final class AnnotatedInstanceUtil {
    public static List<IMaidsoulKitchen> getModExtensions() {
        return getInstances(TLMKPlugin.class, IMaidsoulKitchen.class);
    }

    @SuppressWarnings("SameParameterValue")
    private static <T> List<T> getInstances(Class<?> annotationClass, Class<T> instanceClass) {
        Type annotationType = Type.getType(annotationClass);
        List<ModFileScanData> allScanData = ModList.get().getAllScanData();
        Set<String> extensionClassNames = new LinkedHashSet<>();
        for (ModFileScanData scanData : allScanData) {
            for (ModFileScanData.AnnotationData data : scanData.getAnnotations()) {
                if (Objects.equals(data.annotationType(), annotationType)) {
                    String memberName = data.memberName();
                    extensionClassNames.add(memberName);
                }
            }
        }
        List<T> instances = new ArrayList<>();
        for (String className : extensionClassNames) {
            try {
                Class<?> asmClass = Class.forName(className);
                Class<? extends T> asmInstanceClass = asmClass.asSubclass(instanceClass);
                T instance = asmInstanceClass.newInstance();
                instances.add(instance);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | LinkageError e) {
                MaidsoulKitchen.LOGGER.error("Failed to load: {}", className, e);
            }
        }
        return instances;
    }
}
