package com.github.wallev.maidsoulkitchen.compat.msm.common.craft.custom.menu;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideData;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;
import studio.fantasyit.maid_storage_manager.craft.work.CraftLayer;
import studio.fantasyit.maid_storage_manager.util.ItemStackUtil;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;

@TaskClassAnalyzer(TaskInfo.MSM_CORE)
public class MenuPlaceItemAction extends AbstractMenuActionContext {
    public static final ResourceLocation TYPE = VResourceLocation.ofMod("menu_place_item");

    public MenuPlaceItemAction(EntityMaid maid, CraftGuideData craftGuideData, CraftGuideStepData craftGuideStepData, CraftLayer layer) {
        super(maid, craftGuideData, craftGuideStepData, layer);
    }

    @Override
    public Result thisTick() {
        if (allDone()) return Result.SUCCESS;
        boolean hasChange = false;
        boolean reTryStart = false;
        CombinedInvWrapper inv = maid.getAvailableInv(false);
        ingredientIndex++;
        ItemStack stepItem = craftGuideStepData.getNonEmptyInput().get(ingredientIndex);
        {
            boolean shouldDoPlace = false;
            int count = 0;
            for (; slot < inv.getSlots(); slot++) {
                //物品匹配且还需继续放入
                @NotNull ItemStack item = inv.getStackInSlot(slot);
                if (item.isEmpty()) continue;
                if (count++ > 10) break;
                if (ItemStackUtil.isSameInCrafting(stepItem, item)) {
                    if (craftLayer.getCurrentStepCount(ingredientIndex) < stepItem.getCount()) {
                        shouldDoPlace = true;
                        break;
                    }
                }
            }
            if (shouldDoPlace) {
                @NotNull ItemStack item = inv.getStackInSlot(slot);
                int placed = craftLayer.getCurrentStepCount(ingredientIndex);
                int required = stepItem.getCount();
                int pick = Math.min(
                        required - placed,
                        item.getCount()
                );
                ItemStack copy = item.copyWithCount(pick);
                ItemStack rest = this.placeItem(copy);
                item.shrink(pick - rest.getCount());
                craftLayer.addCurrentStepPlacedCounts(ingredientIndex, pick - rest.getCount());
                if (pick - rest.getCount() != 0) {
                    hasChange = true;
                } else if (craftLayer.getStep() == 1)
                    slot++;
            }

            if (craftLayer.getCurrentStepCount(ingredientIndex) >= stepItem.getCount()) {
                ingredientIndex++;
                slot = 0;
            } else if (slot >= inv.getSlots()) {
                if (craftGuideStepData.isOptional())//尽力满足输入，而非必须全部输入
                    ingredientIndex++;
                else
                    reTryStart = true;
                slot = 0;
            }
            if (ingredientIndex >= craftGuideStepData.getNonEmptyInput().size()) {
                return Result.SUCCESS;
            }
        }

        if (reTryStart)
            return hasChange ? Result.CONTINUE_INTERRUPTABLE : Result.NOT_DONE_INTERRUPTABLE;
        return hasChange ? Result.CONTINUE : Result.NOT_DONE;
    }

    public ItemStack placeItem(ItemStack itemStack) {
        IWrapMenu<?> wrapMenu = IWrapMenu.get(menuType);
        AbstractContainerMenu menu = wrapMenu.createMenu(0, fakePlayer.getInventory());
        menu.setCarried(itemStack);

        NonNullList<Slot> slots = menu.slots;
        for (int i = 36; i < slots.size(); i++) {
            if (!wrapMenu.isValidSlot(side, slot - 36))
                continue;

            Slot slot1 = slots.get(i);
            if (slot1.getItem().isEmpty()) {
                menu.clicked(i, 0, ClickType.PICKUP, fakePlayer);
                break;
            }
        }

        ItemStack carried = menu.getCarried();
        menu.removed(fakePlayer);
        return carried;
    }
}
