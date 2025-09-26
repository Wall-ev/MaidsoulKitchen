package com.github.wallev.maidsoulkitchen.compat.msm.common.util;

import com.github.wallev.maidsoulkitchen.compat.msm.common.storage.ContainerStorage;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import net.minecraft.resources.ResourceLocation;
import studio.fantasyit.maid_storage_manager.MaidStorageManager;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOption;
import studio.fantasyit.maid_storage_manager.craft.action.CraftAction;
import studio.fantasyit.maid_storage_manager.craft.action.PathTargetLocator;
import studio.fantasyit.maid_storage_manager.craft.context.common.*;
import studio.fantasyit.maid_storage_manager.craft.type.*;

@TaskClassAnalyzer(TaskInfo.MSM_CORE)
public class CraftActionTypes {
    public static final CraftActionTypes INSTANCE = new CraftActionTypes();
    public final ResourceLocation UNAVAILABLE = new ResourceLocation(MaidStorageManager.MODID, "unavailable");

    public final ResourceLocation CRAFT_TYPE_COMMON = CommonType.TYPE;
    public final ResourceLocation CRAFT_TYPE_ALTAR = AltarType.TYPE;
    public final ResourceLocation CRAFT_TYPE_ANVIL = AnvilType.TYPE;
    public final ResourceLocation CRAFT_TYPE_BREWING = BrewingType.TYPE;
    public final ResourceLocation CRAFT_TYPE_CRAFTING = CraftingType.TYPE;
    public final ResourceLocation CRAFT_TYPE_FURNACE = FurnaceType.TYPE;
    public final ResourceLocation CRAFT_TYPE_SMITHING = SmithingType.TYPE;
    public final ResourceLocation CRAFT_TYPE_STONECUTTING = StoneCuttingType.TYPE;

    public final ResourceLocation CRAFT_ACTION_COMMON_TAKE_ITEM = CommonTakeItemAction.TYPE;
    public final ResourceLocation CRAFT_ACTION_COMMON_ATTACK = CommonAttackAction.TYPE;
    public final ResourceLocation CRAFT_ACTION_COMMON_PLACE_ITEM = CommonPlaceItemAction.TYPE;
    public final ResourceLocation CRAFT_ACTION_COMMON_SPLIT_ITEM = CommonSplitItemAction.TYPE;
    public final ResourceLocation CRAFT_ACTION_COMMON_USE = CommonUseAction.TYPE;
    public final ResourceLocation CRAFT_ACTION_COMMON_THROW = CommonTakeItemAction.TYPE;
    public final ResourceLocation CRAFT_ACTION_COMMON_PICKUP = CommonPickupItemAction.TYPE;
    public final ResourceLocation CRAFT_ACTION_ALTAR = AltarType.TYPE;
    public final ResourceLocation CRAFT_ACTION_ANVIL = AnvilType.TYPE;
    public final ResourceLocation CRAFT_ACTION_BREWING = BrewingType.TYPE;
    public final ResourceLocation CRAFT_ACTION_CRAFTING = CraftingType.TYPE;
    public final ResourceLocation CRAFT_ACTION_FURNACE = FurnaceType.TYPE;
    public final ResourceLocation CRAFT_ACTION_SMITHING = SmithingType.TYPE;
    public final ResourceLocation CRAFT_ACTION_STONECUTTING = StoneCuttingType.TYPE;

    public final ActionOption<Boolean> ACTION_OPTION_OPTIONAL = ActionOption.OPTIONAL;
    public final ActionOption<Boolean> ACTION_OPTION_WAIT = CommonIdleAction.OPTION_WAIT;
    public final ActionOption<CommonUseAction.USE_TYPE> ACTION_OPTION_USE_METHOD = CommonUseAction.OPTION_USE_METHOD;
    public final ActionOption<CommonAttackAction.USE_TYPE> ACTION_OPTION_ATTACK_METHOD = CommonAttackAction.OPTION_USE_METHOD;

    public final long ACTION_MARK_HAND_RELATED = CraftAction.MARK_HAND_RELATED;
    public final long ACTION_MARK_NO_OCCUPATION = CraftAction.MARK_NO_OCCUPATION;
    public final long ACTION_MARK_NO_MARKS = CraftAction.MARK_NO_MARKS;

    public final CraftAction.CraftActionPathFindingTargetProvider PATH_FINDING_BESIDE_OR_EXACTLY = PathTargetLocator::besidePosOrExactlyPos;
    public final CraftAction.CraftActionPathFindingTargetProvider PATH_FINDING_COMMON = PathTargetLocator::commonNearestAvailablePos;
    public final CraftAction.CraftActionPathFindingTargetProvider PATH_FINDING_EXACTLY = PathTargetLocator::exactlySidedPos;
    public final CraftAction.CraftActionPathFindingTargetProvider PATH_FINDING_TOUCH = PathTargetLocator::touchPos;
    public final CraftAction.CraftActionPathFindingTargetProvider PATH_FINDING_THROW_ITEM = PathTargetLocator::throwItemPos;
    public final CraftAction.CraftActionPathFindingTargetProvider PATH_FINDING_NO_LIMITATION = PathTargetLocator::nearByNoLimitation;

    public final ResourceLocation CRAFT_ACTION_IDLE = CommonIdleAction.TYPE;

    public final ResourceLocation STORAGE_CONTAINER = ContainerStorage.TYPE;

    protected CraftActionTypes() {
    }

    public boolean isAvailable(ResourceLocation type) {
        return !type.equals(UNAVAILABLE);
    }
}
