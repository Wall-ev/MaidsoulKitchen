package com.github.wallev.maidsoulkitchen.task.cook.common.inv.itemdown;

import com.github.wallev.maidsoulkitchen.inventory.container.item.BagType;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemDefinition;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.ItemAmount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HubItemDown extends IItemDown {
    private int availableSlot = BagType.INPUT.endIndex - 5;
    private int slotLimitCount = 64;

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public boolean read(RecDataUse recDataUse) {
        if (useSlot >= availableSlot) {
            return false;
        }

        Map<ItemDefinition, ItemAmount> itemUse = recDataUse.getItemUse();
        int recAmount = recDataUse.getRecipeRepeat();

        Map<ItemDefinition, Integer> useItemDef = new HashMap<>();
        List<ItemDefinition> newPuts = new ArrayList<>();
        boolean canPut = true;
        for (Map.Entry<ItemDefinition, ItemAmount> entry : itemUse.entrySet()) {
            ItemDefinition itemDefinition = entry.getKey();
            ItemAmount itemAmount = entry.getValue();

            boolean tool = itemAmount.isTool();
            if (tool) {
                Integer amount = this.useItemDef.get(itemDefinition);
                if (amount == null) {
                    if (++useSlot > availableSlot) {
                        break;
                    }

                    // 作为工具的话一般都是不可堆叠把，或者在这么多的配方中都是通用的，所以占用一个槽位
                    this.useItemDef.put(itemDefinition, 1);
                    newPuts.add(itemDefinition);
                }
                continue;
            }

            boolean isStackable = itemDefinition.isStackable();
            int needCount = recAmount * itemAmount.needCount() * itemAmount.getRecAmount();
            if (!isStackable) {
                useSlot += needCount;
                if (useSlot > availableSlot) {
                    canPut = false;
                    break;
                }
                useItemDef.merge(itemDefinition, needCount, Integer::sum);
            } else {
                int maxStackSize = itemDefinition.getMaxStackSize();

                int thisCount = 0;
                boolean thisHas = false;
                if (this.useItemDef.get(itemDefinition) != null) {
                    thisCount = this.useItemDef.get(itemDefinition);
                    thisHas = true;
                }
                int inLineCount = 0;
                boolean inLineHas = false;
                if (useItemDef.get(itemDefinition) != null) {
                    inLineCount = useItemDef.get(itemDefinition);
                    inLineHas = true;
                }

                if (thisHas) {
                    if (inLineHas) {
                        int leftUseSlot = (inLineCount + thisCount + needCount) % maxStackSize == 0 ? 1 : 0;;;
                        int useSlot0 = ((inLineCount + needCount) / maxStackSize) + leftUseSlot;
                        useSlot += useSlot0;
                    } else {
                        int useSlot0 = (needCount / maxStackSize) + (needCount % maxStackSize > 0 ? 1 : 0);
                        useSlot += useSlot0;
                    }
                } else {
                    if (inLineHas) {
                        int leftUseSlot = (inLineCount + needCount) % maxStackSize == 0 ? 1 : 0;;;
                        int useSlot0 = (needCount / maxStackSize) + leftUseSlot;
                        useSlot += useSlot0;
                    } else {
                        int useSlot0 = (needCount / maxStackSize) + (needCount % maxStackSize > 0 ? 1 : 0);
                        useSlot += useSlot0;
                    }
                }

                if (useSlot > availableSlot) {
                    canPut = false;
                    break;
                }

                useItemDef.merge(itemDefinition, needCount, Integer::sum);

            }
        }

        if (!canPut) {
            for (ItemDefinition newPut : newPuts) {
                this.useItemDef.remove(newPut);
            }
            return false;
        }

        useItemDef.forEach((itemDefinition, count) -> this.useItemDef.merge(itemDefinition, count, Integer::sum));
        this.recLimitIndex += recAmount;
        return true;
    }
}
