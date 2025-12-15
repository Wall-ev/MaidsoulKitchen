package com.github.wallev.maidsoulkitchen.compat.msm.common.craft.custom.menu;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.util.IFailGuideUseActionContext;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import studio.fantasyit.maid_storage_manager.craft.context.AbstractCraftActionContext;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideData;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;
import studio.fantasyit.maid_storage_manager.craft.work.CraftLayer;
import studio.fantasyit.maid_storage_manager.util.InvUtil;
import studio.fantasyit.maid_storage_manager.util.ItemStackUtil;
import studio.fantasyit.maid_storage_manager.util.MemoryUtil;
import studio.fantasyit.maid_storage_manager.util.WrappedMaidFakePlayer;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMenuActionContext extends AbstractCraftActionContext implements IFailGuideUseActionContext {

    public record Context(ResourceLocation menuType, @Nullable Direction side) {

        public static final ResourceLocation UNKNOWN = VResourceLocation.ofMod("unknown");

        public static final Codec<Context> CODEC = RecordCodecBuilder.create(ins -> ins.group(
                ResourceLocation.CODEC.fieldOf("menuType").forGetter(Context::menuType),
                Direction.CODEC.optionalFieldOf("side", null).forGetter(Context::side)
        ).apply(ins, Context::new));

        public static CompoundTag to(Context context) {
            return CODEC.encodeStart(NbtOps.INSTANCE, context)
                    .result()
                    .map(tag -> (CompoundTag) tag)
                    .orElseGet(CompoundTag::new);
        }

        public static CompoundTag to(ResourceLocation menuType, @Nullable Direction side) {
            return CODEC.encodeStart(NbtOps.INSTANCE, new Context(menuType, side))
                    .result()
                    .map(tag -> (CompoundTag) tag)
                    .orElseGet(CompoundTag::new);
        }

        public static CompoundTag to(ResourceLocation menuType) {
            return to(menuType, null);
        }

        public static Context from(CompoundTag compoundTag) {
            return CODEC.parse(NbtOps.INSTANCE, compoundTag)
                    .result()
                    .orElse(new Context(UNKNOWN, null));
        }
    }

    protected WrappedMaidFakePlayer fakePlayer;
    int storedSlotMainHand = -1;
    int storedSlotOffHand = -1;
    int slot = 0;
    int ingredientIndex = 0;
    ResourceLocation menuType;
    @Nullable
    Direction side;
    public AbstractMenuActionContext(EntityMaid maid, CraftGuideData craftGuideData, CraftGuideStepData craftGuideStepData, CraftLayer layer) {
        super(maid, craftGuideData, craftGuideStepData, layer);
    }

    @Override
    public Result start() {
        return thisStart();
    }

    public Result thisStart() {
        fakePlayer = WrappedMaidFakePlayer.get(maid);
        if (fakePlayer.isUsingItem())
            fakePlayer.stopUsingItem();
        maid.getNavigation().stop();
        ItemStack targetItem = craftGuideStepData.getInput().get(0);
        ItemStack targetItem2 = craftGuideStepData.getInput().get(1);
        storedSlotMainHand = InvUtil.getTargetIndexInCrafting(maid, targetItem, 1);
        if (storedSlotMainHand == -1) {
            return Result.FAIL;
        }
        storedSlotOffHand = InvUtil.getTargetIndexInCrafting(maid, targetItem2, 2, storedSlotMainHand);
        if (storedSlotOffHand == -1)
            return Result.FAIL;

        InvUtil.swapHandAndSlot(maid, InteractionHand.MAIN_HAND, storedSlotMainHand);
        InvUtil.swapHandAndSlot(maid, InteractionHand.OFF_HAND, storedSlotOffHand);
        MemoryUtil.getCrafting(maid).setSwappingHandWhenCrafting(true);

        Context context = Context.from(craftGuideStepData.getExtraData());
        menuType = context.menuType();
        side = context.side();

        return Result.CONTINUE;
    }

    @Override
    public Result tick() {
        return thisTick();
    }

    public abstract Result thisTick();

    @Override
    public void stop() {
        thisStp();
    }

    public void thisStp() {
        if (storedSlotOffHand != -1)
            InvUtil.swapHandAndSlot(maid, InteractionHand.OFF_HAND, storedSlotOffHand);
        if (storedSlotMainHand != -1)
            InvUtil.swapHandAndSlot(maid, InteractionHand.MAIN_HAND, storedSlotMainHand);

        if (fakePlayer.isUsingItem())
            fakePlayer.stopUsingItem();
        MemoryUtil.getCrafting(maid).setSwappingHandWhenCrafting(false);
    }

    boolean allDone() {
        if (craftGuideStepData == null) return false;
        List<ItemStack> items = craftGuideStepData.getOutput();
        for (int i = 0; i < items.size(); i++) {
            if (craftLayer.getCurrentStepCount(i) < items.get(i).getCount()) {
                return false;
            }
        }
        return true;
    }

    private List<ItemStack> getAndClearFakePlayerInventory() {
        Inventory inventory = fakePlayer.getInventory();
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (!inventory.getItem(i).isEmpty()) {
                ItemStackUtil.addToList(items, inventory.getItem(i), true);
                inventory.setItem(i, ItemStack.EMPTY);
            }
        }
        return items;
    }

    Result checkAndGetResult() {
        List<ItemStack> ret = getAndClearFakePlayerInventory();

        int resultPlaced = 0;
        //物品栏新增的物品
        for (ItemStack itemStack : ret) {
            ItemStack itemStack1 = InvUtil.tryPlace(maid.getAvailableInv(false), itemStack);
            int realPlaced = itemStack.getCount() - itemStack1.getCount();
            if (!itemStack1.isEmpty()) {
                InvUtil.throwItem(maid, itemStack1);
            }
            if (ItemStackUtil.isSameInCrafting(itemStack, craftGuideStepData.getOutput().get(0))) {
                resultPlaced += realPlaced;
            }
        }
        //如果主手包含目标物品，也视为返回
        if (ItemStackUtil.isSameInCrafting(craftGuideStepData.getOutput().get(0), fakePlayer.getMainHandItem())) {
            resultPlaced += fakePlayer.getMainHandItem().getCount();
        }
        //如果副手包含目标物品，也视为返回
        if (ItemStackUtil.isSameInCrafting(craftGuideStepData.getOutput().get(0), fakePlayer.getOffhandItem())) {
            resultPlaced += fakePlayer.getOffhandItem().getCount();
        }

        if (resultPlaced >= craftGuideStepData.getOutput().get(0).getCount()) {
            return Result.SUCCESS;
        } else {
            if (craftGuideStepData.isOptional())
                return Result.SUCCESS;
            else
                return Result.FAIL;
        }
    }
}
