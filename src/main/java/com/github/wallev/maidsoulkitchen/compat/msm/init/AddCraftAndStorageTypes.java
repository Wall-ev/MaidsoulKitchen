package com.github.wallev.maidsoulkitchen.compat.msm.init;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base.*;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.custom.*;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.custom.menu.MenuPlaceItemAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.custom.menu.MenuTakeItemAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.special.StoneCutterRecipeAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.storage.ContainerStorage;
import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.InvHandlersHelper;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.ITaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.manager.BaseClazzCheckManager;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskModClazzManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.moddiscovery.ModAnnotation;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.objectweb.asm.Type;
import studio.fantasyit.maid_storage_manager.craft.CollectCraftEvent;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOption;
import studio.fantasyit.maid_storage_manager.craft.action.CraftAction;
import studio.fantasyit.maid_storage_manager.craft.action.PathTargetLocator;
import studio.fantasyit.maid_storage_manager.craft.context.common.CommonAttackAction;
import studio.fantasyit.maid_storage_manager.craft.context.common.CommonIdleAction;
import studio.fantasyit.maid_storage_manager.craft.context.common.CommonPlaceItemAction;
import studio.fantasyit.maid_storage_manager.craft.context.common.CommonUseAction;
import studio.fantasyit.maid_storage_manager.craft.generator.type.base.IAutoCraftGuideGenerator;
import studio.fantasyit.maid_storage_manager.storage.CollectStorageEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;

public class AddCraftAndStorageTypes {
    private static final Marker MARKER = MarkerManager.getMarker("MaidStorageManagerCompat");

    AddCraftAndStorageTypes() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @SubscribeEvent
    public void registerStorageTypes(CollectStorageEvent event) {
        event.getStorages().add(new ContainerStorage());
        InvHandlersHelper.init();
    }

    @SubscribeEvent
    public void registerCraftTypes(CollectCraftEvent event) {
        event.addAction(
                JumpAction.TYPE,
                JumpAction::new,
                PathTargetLocator::nearByNoLimitation,
                CraftAction.PathEnoughLevel.CLOSER.value,
                false,
                4,
                4,
                List.of(JumpAction.JUMP_COUNT)
        );
        event.addAction(
                LimitIdleAction.TYPE,
                LimitIdleAction::new,
                PathTargetLocator::exactlySidedPos,
                CraftAction.PathEnoughLevel.CLOSER.value,
                false,
                0,
                4,
                List.of(LimitIdleAction.OPTION_WAIT)
        );
        event.addAction(
                SneakCommonUseAction.TYPE,
                SneakCommonUseAction::new,
                PathTargetLocator::touchPos,
                CraftAction.PathEnoughLevel.CLOSER.value,
                false,
                CraftAction.MARK_HAND_RELATED,
                2,
                2,
                List.of(ActionOption.OPTIONAL, SneakCommonUseAction.OPTION_USE_METHOD, SneakCommonUseAction.SNEAK)
        );
        event.addAction(
                EmptyAction.TYPE,
                EmptyAction::new,
                PathTargetLocator::touchPos,
                CraftAction.PathEnoughLevel.CLOSER.value,
                false,
                2,
                2,
                List.of()
        );
        event.addAction(
                FailAction.TYPE,
                FailAction::new,
                PathTargetLocator::touchPos,
                CraftAction.PathEnoughLevel.CLOSER.value,
                false,
                2,
                2,
                List.of()
        );
        event.addAction(
                FailTakeAction.TYPE,
                FailTakeAction::new,
                PathTargetLocator::commonNearestAvailablePos,
                CraftAction.PathEnoughLevel.NORMAL.value,
                false,
                0,
                0,
                List.of(ActionOption.OPTIONAL)
        );
        event.addAction(
                StoneCutterRecipeAction.TYPE,
                StoneCutterRecipeAction::new,
                PathTargetLocator::commonNearestAvailablePos,
                CraftAction.PathEnoughLevel.NORMAL.value,
                false, true,
                1,
                1,
                List.of()
        );

        event.addAction(
                EnchantCommonAttackAction.TYPE,
                EnchantCommonAttackAction::new,
                PathTargetLocator::touchPos,
                CraftAction.PathEnoughLevel.CLOSER.value,
                false,
                CraftAction.MARK_HAND_RELATED,
                2,
                2,
                List.of(ActionOption.OPTIONAL, CommonAttackAction.OPTION_USE_METHOD)
        );
        event.addAction(
                EnchantCommonIdleAction.TYPE,
                EnchantCommonIdleAction::new,
                PathTargetLocator::nearByNoLimitation,
                CraftAction.PathEnoughLevel.CLOSER.value,
                false,
                0,
                4,
                List.of(CommonIdleAction.OPTION_WAIT)
        );
        event.addAction(
                EnchantCommonPickupItemAction.TYPE,
                EnchantCommonPickupItemAction::new,
                PathTargetLocator::besidePosOrExactlyPos,
                CraftAction.PathEnoughLevel.VERY_CLOSE.value,
                false,
                0,
                4,
                List.of(ActionOption.OPTIONAL)
        );
        event.addAction(
                EnchantCommonPlaceItemAction.TYPE,
                EnchantCommonPlaceItemAction::new,
                PathTargetLocator::commonNearestAvailablePos,
                CraftAction.PathEnoughLevel.NORMAL.value,
                false,
                4,
                4,
                List.of(ActionOption.OPTIONAL, CommonPlaceItemAction.OPTION_SPLIT)
        );
        event.addAction(
                EnchantCommonSplitItemAction.TYPE,
                EnchantCommonSplitItemAction::new,
                PathTargetLocator::commonNearestAvailablePos,
                CraftAction.PathEnoughLevel.CLOSER.value,
                false,
                CraftAction.MARK_HAND_RELATED,
                4,
                4,
                List.of(ActionOption.OPTIONAL, SneakCommonUseAction.OPTION_USE_METHOD, SneakCommonUseAction.SNEAK)
        );
        event.addAction(
                EnchantCommonTakeItemAction.TYPE,
                EnchantCommonTakeItemAction::new,
                PathTargetLocator::commonNearestAvailablePos,
                CraftAction.PathEnoughLevel.NORMAL.value,
                false,
                0,
                4,
                List.of(ActionOption.OPTIONAL)
        );
        event.addAction(
                EnchantCommonThrowItemAction.TYPE,
                EnchantCommonThrowItemAction::new,
                PathTargetLocator::throwItemPos,
                CraftAction.PathEnoughLevel.CLOSER.value,
                false,
                4,
                0,
                List.of(ActionOption.OPTIONAL)
        );
        event.addAction(
                EnchantCommonUseAction.TYPE,
                EnchantCommonUseAction::new,
                PathTargetLocator::touchPos,
                CraftAction.PathEnoughLevel.CLOSER.value,
                false,
                CraftAction.MARK_HAND_RELATED,
                2,
                4,
                List.of(ActionOption.OPTIONAL, CommonUseAction.OPTION_USE_METHOD, EnchantCommonUseAction.SNEAK)
        );

        event.addAction(
                MenuPlaceItemAction.TYPE,
                MenuPlaceItemAction::new,
                PathTargetLocator::nearByNoLimitation,
                CraftAction.PathEnoughLevel.CLOSER.value,
                false,
                4,
                4,
                List.of()
        );
        event.addAction(
                MenuTakeItemAction.TYPE,
                MenuTakeItemAction::new,
                PathTargetLocator::nearByNoLimitation,
                CraftAction.PathEnoughLevel.CLOSER.value,
                false,
                4,
                4,
                List.of()
        );

        register(event, TaskModClazzManager.getCheckManager());
    }

    @SuppressWarnings("SameParameterValue")
    private static void register(CollectCraftEvent event, BaseClazzCheckManager<?, ?> checkManager) {
        Type annotationType = Type.getType(AutoCraftGuideGeneratorRegister.class);
        MaidsoulKitchen.LOGGER.info(MARKER, "Start to register");
        ModList.get().getAllScanData().stream().flatMap(scanData -> scanData.getAnnotations().stream())
                .filter(annotationData -> Objects.equals(annotationData.annotationType(), annotationType))
                .forEach(data -> {
                    try {
                        ITaskInfo<?> task = checkManager.taskInfoByKey(getEnumHolderValue(data, "value"));
                        if (task == null) {
                            MaidsoulKitchen.LOGGER.error(MARKER, "Failed to load task: {}", task);
                            return;
                        }

                        if (task.canLoad()) {
                            String clazzName = data.memberName();

                            Class<?> asmClazz = Class.forName(clazzName);

                            boolean eventConstructor = tryInstantiateWithEventConstructor(asmClazz, event);
                            if (!eventConstructor) {
                                tryInstantiateEmptyConstructor(asmClazz, event);
                            }
                        } else {
                            MaidsoulKitchen.LOGGER.error(MARKER, "task can not load: {}", task);
                        }
                    } catch (ClassNotFoundException | LinkageError e) {
                        MaidsoulKitchen.LOGGER.error(MARKER, "Failed to load class: {}", data, e);
                    }
                });
    }

    private static boolean tryInstantiateWithEventConstructor(Class<?> clazz, CollectCraftEvent event) {
        try {
            Constructor<?> constructor = clazz.getConstructor(CollectCraftEvent.class);
            constructor.newInstance(event);
            MaidsoulKitchen.LOGGER.info(MARKER, "Successfully added auto_craft_guides: {}", clazz);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            MaidsoulKitchen.LOGGER.error(MARKER, "Failed to add auto_craft_guides instance: {}", clazz, e);
            return false;
        }
    }

    private static void tryInstantiateEmptyConstructor(Class<?> clazz, CollectCraftEvent event) {
        try {
            IAutoCraftGuideGenerator instance = (IAutoCraftGuideGenerator) clazz.getDeclaredConstructor().newInstance();
            event.addAutoCraftGuideGenerator(instance);

            MaidsoulKitchen.LOGGER.info(MARKER, "Successfully added auto_craft_guides: {}", clazz);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            MaidsoulKitchen.LOGGER.error(MARKER, "Failed to add auto_craft_guides instance: {}", clazz, e);
        }
    }

    private static String getEnumHolderValue(ModFileScanData.AnnotationData data, String name) {
        Object o = data.annotationData().get(name);
        return ((ModAnnotation.EnumHolder) o).getValue();
    }

}
