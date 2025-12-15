package com.github.wallev.maidsoulkitchen.task.farm.advancefarm;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.wallev.maidsoulkitchen.util.fakeplayer.WrappedMaidFakePlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static com.github.wallev.maidsoulkitchen.api.task.farm.ICompatFarmTask.BLACK_LIST;
import static com.github.wallev.maidsoulkitchen.vhelper.IModInfo.LOGGER;

public abstract class IHarvestCrop<B extends Block> {

    static final Map<Block, List<IHarvestCrop<?>>> MAP = new HashMap<>();

    static final List<IHarvestCrop<?>> RULE = new ArrayList<>();

    static void init() {
        ForgeRegistries.BLOCKS.getValues().forEach(block -> {
            RULE.forEach(rule -> {
                if (rule.isFarmCrop(block)) {
                    MAP.computeIfAbsent(block, k -> new ArrayList<>()).add(rule);
                }
            });
        });
    }

    public static List<IHarvestCrop<?>> getRules(Block block) {
        return MAP.getOrDefault(block, List.of());
    }

    public abstract ResourceLocation getUid();

    public abstract boolean isFarmCrop(Block block);

    public abstract boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState);

    public abstract boolean harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState);


    protected final boolean harvestWithTool(EntityMaid maid, BlockPos cropPos, BlockState cropState, Predicate<ItemStack> predicate) {
        if (this.canHarvest(maid, cropPos, cropState)) {
            ItemStack toolStack = ItemsUtil.getStack(maid.getAvailableInv(true), predicate);
            if (!toolStack.isEmpty()) {
                InteractionResult result = WrappedMaidFakePlayer.get(maid).useOnByItem(cropPos, toolStack);
                if (result == InteractionResult.PASS) {
                    BLACK_LIST.add(cropState.getBlock());
                    LOGGER.warn(BLACK_LIST.toString());
                }
                return true;
            }
        }
        return false;
    }

    protected final boolean harvestWithoutTool(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        if (this.canHarvest(maid, cropPos, cropState)) {
            InteractionResult result = WrappedMaidFakePlayer.get(maid).useOnByHand(cropPos);
            if (result == InteractionResult.PASS) {
                BLACK_LIST.add(cropState.getBlock());
                LOGGER.warn(BLACK_LIST.toString());
            }
            return true;
        }
        return false;
    }
}
