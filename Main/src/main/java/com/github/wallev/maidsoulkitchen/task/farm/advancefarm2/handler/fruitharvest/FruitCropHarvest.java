package com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.handler.fruitharvest;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.handler.ICropHarvest;
import com.github.wallev.maidsoulkitchen.util.InvUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import static com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.handler.RightClickCropHarvest.*;

@AutoCropHarvestRegister
public class FruitCropHarvest implements ICropHarvest {
    public static final ResourceLocation DEFAULT_UID = new ResourceLocation("maidsoulkitchen", "fruit_crop_harvest");

    public static final FruitCropHarvest MAX_AGE_CLICK_HARVEST = new FruitCropHarvest(
            IFruitCropCanHarvest.MaxAgeCanHarvestRule.INSTANCE,
            IFruitCropHarvest.EmptyHarvestRule.INSTANCE
    );

    protected final BoundingBox checkRange = new BoundingBox(-1, -3, -1, 1, 3, 1);
    private final IFruitCropCanHarvest canHarvestRule;
    private final IFruitCropHarvest harvestRule;
    private final ResourceLocation uid;

    public FruitCropHarvest(IFruitCropCanHarvest canHarvestRule, IFruitCropHarvest harvestRule) {
        this.canHarvestRule = canHarvestRule;
        this.harvestRule = harvestRule;
        this.uid = DEFAULT_UID;
    }

    public FruitCropHarvest(IFruitCropCanHarvest canHarvestRule, IFruitCropHarvest harvestRule, ResourceLocation uid) {
        this.canHarvestRule = canHarvestRule;
        this.harvestRule = harvestRule;
        this.uid = uid;
    }

    @Override
    public boolean isFarmCrop(Block block) {
        return FruitHarvestHandler.FRUIT_BLOCKS.contains(block);
    }

    @Override
    public ResourceLocation getUid() {
        return this.uid;
    }

    @Override
    public boolean hasAviShape(ServerLevel worldIn, EntityMaid maid, BlockPos cropPos) {
        return true;
    }

    @Override
    public Result canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        return this.canHarvestRule.canHarvest(maid, cropPos, cropState);
    }

    @Override
    public Result harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        return this.harvestRule.harvest(maid, cropPos, cropState);
    }

    @Override
    public boolean checkPathReach(EntityMaid maid, MaidPathFindingBFS pathFinding, BlockPos pos, HarvestData data) {
        int maidY = maid.getOnPos().getY();
        BlockPos onMaidYPos = pos.atY(maidY);

        for (int x = checkRange.minX(); x <= checkRange.maxX(); x++) {
            for (int y = checkRange.minY(); y <= checkRange.maxY(); y++) {
                for (int z = checkRange.minZ(); z <= checkRange.maxZ(); z++) {
                    if (pathFinding.canPathReach(onMaidYPos.offset(x, y, z))) {
                        data.setWalkAndLookPos(onMaidYPos.offset(x, y, z), pos);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isSeed(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        return false;
    }

    @Override
    public ItemStack plant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        return seed;
    }

    public interface IFruitCropCanHarvest {
        Result canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState);

        class MaxAgeCanHarvestRule implements IFruitCropCanHarvest {
            public static final MaxAgeCanHarvestRule INSTANCE = new MaxAgeCanHarvestRule();

            @Override
            public Result canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
                Block block = cropState.getBlock();
                FruitHarvestHandler.AgeProperty ageProperty = FruitHarvestHandler.AGE_PROPERTIES.get(block);
                if (ageProperty != null) {
                    return ageProperty.isMature(cropState) ? Result.SUCCESS : Result.FAILURE;
                }

                IntegerProperty age = getAge(cropState);
                return age != null && isMature(cropState, age) ? Result.SUCCESS : Result.FAILURE;
            }
        }

        record PropertyWithToolCanHarvestRule(Item tool, IFruitCropProperty<?, ?> property) implements IFruitCropCanHarvest {
            @Override
            public Result canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
                Property<?> propertyKey = property.getProperty();
                if (!cropState.hasProperty(propertyKey)) {
                    return Result.FAILURE;
                }
                return cropState.getValue(propertyKey) == property.needValue() && InvUtil.hasStack(maid.getAvailableInv(true), tool) ? Result.SUCCESS : Result.FAILURE;
            }
        }

        interface IFruitCropProperty<P extends Property<?>, V> {
            P getProperty();

            V needValue();

            record BoolProperty(BooleanProperty property) implements IFruitCropProperty<BooleanProperty, Boolean> {

                @Override
                public BooleanProperty getProperty() {
                    return property;
                }

                @Override
                public Boolean needValue() {
                    return true;
                }
            }

            record IntegerPropertyWithValue(IntegerProperty property, int value) implements IFruitCropProperty<IntegerProperty, Integer> {
                @Override
                public IntegerProperty getProperty() {
                    return property;
                }

                @Override
                public Integer needValue() {
                    return value;
                }
            }
        }
    }

    public interface IFruitCropHarvest {
        Result harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState);

        public static class EmptyHarvestRule implements IFruitCropHarvest {
            public static final EmptyHarvestRule INSTANCE = new EmptyHarvestRule();

            public Result harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
                return harvestWithoutTool(maid, cropPos, cropState) ? Result.SUCCESS : Result.FAILURE;
            }
        }

        public record ToolHarvestRule(Item tool) implements IFruitCropHarvest {
            public Result harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
                return harvestWithTool(maid, cropPos, cropState, stack -> stack.is(tool)) ? Result.SUCCESS : Result.FAILURE;
            }

            public static ToolHarvestRule of(Item tool) {
                return new ToolHarvestRule(tool);
            }
        }

        public static class DestroyHarvestRule implements IFruitCropHarvest {
            public static final DestroyHarvestRule INSTANCE = new DestroyHarvestRule();
            public Result harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
                return maid.destroyBlock(cropPos) ? Result.SUCCESS : Result.FAILURE;
            }
        }
    }


}
