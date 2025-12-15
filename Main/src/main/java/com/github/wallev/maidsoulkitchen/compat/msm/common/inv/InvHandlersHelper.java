package com.github.wallev.maidsoulkitchen.compat.msm.common.inv;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.ITaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.manager.BaseClazzCheckManager;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskModClazzManager;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModAnnotation;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@TaskClassAnalyzer(TaskInfo.MSM_CORE)
public class InvHandlersHelper {
    static final Map<BlockEntityType<?>, IInvHandlerFactory<?>> INV_HANDLERS = new HashMap<>();

    @Nullable
    public static IInvHandlerFactory<?> get(BlockEntityType<?> type) {
        return INV_HANDLERS.get(type);
    }

    public static void init() {
        INV_HANDLERS.clear();

        register(InvHandlerRegister.class, TaskModClazzManager.getCheckManager());
    }

    @SuppressWarnings("SameParameterValue")
    private static void register(Class<?> annotationClass, BaseClazzCheckManager<?, ?> checkManager) {
        Type annotationType = Type.getType(annotationClass);

        ModList.get().getAllScanData().stream().flatMap(scanData -> scanData.getAnnotations().stream())
                .filter(annotationData -> Objects.equals(annotationData.annotationType(), annotationType))
                .forEach(data -> {
                    try {
                        ITaskInfo<?> task = checkManager.taskInfoByKey(getEnumHolderValue(data, "value"));
                        if (task != null && task.canLoad()) {
                            String clazzName = data.memberName();
                            Class<?> asmClazz = Class.forName(clazzName);
                            asmClazz.getDeclaredConstructor().newInstance();
                        }
                    } catch (ClassNotFoundException | LinkageError | InstantiationException |
                             IllegalAccessException  | InvocationTargetException | NoSuchMethodException e) {
                        MaidsoulKitchen.LOGGER.error("Failed to load class: {}", data, e);
                    }
                });
    }

    private static String getEnumHolderValue(ModFileScanData.AnnotationData data, String name) {
        Object o = data.annotationData().get(name);
        return ((ModAnnotation.EnumHolder) o).getValue();
    }
}
