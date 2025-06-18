package com.github.wallev.maidsoulkitchen.task.cook.common.inv.itemdown;

import com.github.wallev.maidsoulkitchen.inventory.container.item.BagType;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemDefinition;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.ItemAmount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HubItemDown extends IItemDown {
    private final int availableSlot = BagType.INPUT.endIndex;
    private final int slotLimitCount = 64;

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public boolean read(Map<ItemDefinition, ItemAmount> itemUse) {
        if (useSlot > availableSlot) {
            return false;
        }

        int recUseCount = 0;
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
            int recAmount = itemAmount.getRecAmount();
            int needCount = recAmount * itemAmount.needCount();
            if (!isStackable) {
                useSlot += needCount;
                if (useSlot > availableSlot) {
                    canPut = false;
                    break;
                }
                useItemDef.merge(itemDefinition, needCount, Integer::sum);
            } else {
                int maxStackSize = itemDefinition.getMaxStackSize();
                int amountSlot = needCount / maxStackSize;
                useSlot += amountSlot;
                if (useSlot > availableSlot) {
                    canPut = false;
                    break;
                }

                int existCount = this.useItemDef.getOrDefault(itemDefinition, 0) + useItemDef.getOrDefault(itemDefinition, 0);
                int existSlotAmount = existCount % maxStackSize;
                int leftAmount = needCount % maxStackSize;
                int allLeftAmount = existSlotAmount + leftAmount;
                int i = allLeftAmount / maxStackSize;
                useSlot += i;
                if (useSlot > availableSlot) {
                    canPut = false;
                    break;
                }
                recUseCount = recAmount;
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
        this.recLimitIndex += recUseCount;
        return true;
    }
}
