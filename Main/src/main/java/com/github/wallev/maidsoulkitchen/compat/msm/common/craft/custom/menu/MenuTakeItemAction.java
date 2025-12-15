package com.github.wallev.maidsoulkitchen.compat.msm.common.craft.custom.menu;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideData;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;
import studio.fantasyit.maid_storage_manager.craft.work.CraftLayer;
import studio.fantasyit.maid_storage_manager.util.ItemStackUtil;

@TaskClassAnalyzer(TaskInfo.MSM_CORE)
public class MenuTakeItemAction extends AbstractMenuActionContext {
    public static final ResourceLocation TYPE = VResourceLocation.ofMod("menu_take_item");

    public MenuTakeItemAction(EntityMaid maid, CraftGuideData craftGuideData, CraftGuideStepData craftGuideStepData, CraftLayer layer) {
        super(maid, craftGuideData, craftGuideStepData, layer);
    }

    @Override
    public Result thisTick() {
        if (allDone()) return Result.SUCCESS;
        boolean hasChange = false;
        boolean reTryStart = false;

        ingredientIndex++;
        IWrapMenu<?> wrapMenu = IWrapMenu.get(menuType);
        AbstractContainerMenu menu = wrapMenu.createMenu(0, fakePlayer.getInventory());
        NonNullList<Slot> slots = menu.slots;
        ItemStack stepItem = craftGuideStepData.getNonEmptyOutput().get(ingredientIndex);
        {
            ItemStack beTakeItem = ItemStack.EMPTY;
            int count = 0;
            for (slot = 36; slot < slots.size(); slot++) {
                if (!wrapMenu.isValidSlot(side, slot - 36))
                    continue;

                //物品匹配且还需继续放入
                @NotNull ItemStack item = slots.get(slot).getItem();
                if (item.isEmpty()) continue;
                if (count++ > 10) break;
                if (ItemStackUtil.isSameInCrafting(stepItem, item)) {
                    if (craftLayer.getCurrentStepCount(ingredientIndex) < stepItem.getCount()) {
                        beTakeItem = item;
                        break;
                    }
                }
            }

            if (!beTakeItem.isEmpty()) {
                int placed = craftLayer.getCurrentStepCount(ingredientIndex);
                int required = stepItem.getCount();
                int pick = Math.min(
                        required - placed,
                        beTakeItem.getCount()
                );
                ItemStack rest = this.takeItem(menu, slot);
//                item.shrink(pick - rest.getCount());
                craftLayer.addCurrentStepPlacedCounts(ingredientIndex, pick - rest.getCount());
                if (pick - rest.getCount() != 0) {
                    hasChange = true;
                } else if (craftLayer.getStep() == 1)
                    slot++;
            }

            if (craftLayer.getCurrentStepCount(ingredientIndex) >= stepItem.getCount()) {
                ingredientIndex++;
                slot = 0;
            } else if (slot >= slots.size()) {
                if (craftGuideStepData.isOptional())//尽力满足输入，而非必须全部输入
                    ingredientIndex++;
                else
                    reTryStart = true;
                slot = 0;
            }
            menu.removed(fakePlayer);
            if (ingredientIndex >= craftGuideStepData.getNonEmptyInput().size()) {
                return this.checkAndGetResult();
            }
        }

        if (reTryStart)
            return hasChange ? Result.CONTINUE_INTERRUPTABLE : Result.NOT_DONE_INTERRUPTABLE;
        return hasChange ? Result.CONTINUE : Result.NOT_DONE;
    }


    public ItemStack takeItem(AbstractContainerMenu menu, int slotIndex) {
        menu.clicked(slotIndex, 0, ClickType.PICKUP, fakePlayer);

        NonNullList<Slot> slots = menu.slots;
        int tmpIndex = -1;
        for (int i = 36; i < slots.size(); i++) {
            if (slots.get(i).getItem().isEmpty()) {
                menu.clicked(tmpIndex = i, 0, ClickType.PICKUP, fakePlayer);
                break;
            }
        }

        if (tmpIndex > -1) {
            int lastPlaceIndex = 0;
            menu.clicked(tmpIndex, 0, ClickType.PICKUP, fakePlayer);
            while (!menu.getCarried().isEmpty() && lastPlaceIndex < 36) {
                for (int i = lastPlaceIndex; i < 36; i++) {
                    lastPlaceIndex = i + 1;
                    Slot slot1 = slots.get(i);
                    if (!slot1.mayPlace(menu.getCarried()))
                        continue;

                    if (ItemStack.isSameItemSameTags(slot1.getItem(), menu.getCarried())) {
                        menu.clicked(i, 0, ClickType.PICKUP, fakePlayer);
                        if (menu.getCarried().isEmpty())
                            break;
                    }
                }
            }

            lastPlaceIndex = 0;
            while (!menu.getCarried().isEmpty() && lastPlaceIndex < 36) {
                for (int i = lastPlaceIndex; i < 36; i++) {
                    lastPlaceIndex = i + 1;
                    Slot slot1 = slots.get(i);
                    if (!slot1.mayPlace(menu.getCarried()))
                        continue;

                    if (slot1.getItem().isEmpty()) {
                        menu.clicked(i, 0, ClickType.PICKUP, fakePlayer);
                        if (menu.getCarried().isEmpty())
                            break;
                    }
                }
            }
        }

        return menu.getCarried();
    }
}
