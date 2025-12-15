package com.github.wallev.maidsoulkitchen.compat.msm.immortalers_delight.enchantal_cooler;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base.EnchantCommonSplitItemAction;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import com.renyigesai.immortalers_delight.block.enchantal_cooler.EnchantalCoolerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOption;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideData;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;
import studio.fantasyit.maid_storage_manager.craft.work.CraftLayer;
import studio.fantasyit.maid_storage_manager.storage.Target;

public class EnchantalCoolerSideSpitItemAction extends EnchantCommonSplitItemAction {

    public static final ResourceLocation TYPE = VResourceLocation.ofMod("enchantal_cooler_side_spit");

    public enum SpiltType {
        FUEL,
        CONTAINER
    }

    public static final ActionOption<SpiltType> OPTION_SPLIT_TYPE = new ActionOption<>(
            VResourceLocation.ofMod("enchantal_cooler_side"),
            new Component[]{
                    Component.translatable("gui.maid_storage_manager.craft_guide.common.use_single"),
                    Component.translatable("gui.maid_storage_manager.craft_guide.common.use_long")
            },
            new ResourceLocation[]{
                    new ResourceLocation("maid_storage_manager:textures/gui/craft/option/use_single.png"),
                    new ResourceLocation("maid_storage_manager:textures/gui/craft/option/use_long.png")
            },
            "",
            new ActionOption.BiConverter<>(
                    i -> SpiltType.values()[i], Enum::ordinal
            ),
            ActionOption.ValuePredicatorOrGetter.getter(t ->
                    switch (t) {
                        case FUEL -> Component.translatable("gui.maid_storage_manager.craft_guide.common.use_long");
                        case CONTAINER ->
                                Component.translatable("gui.maid_storage_manager.craft_guide.common.use_single");
                    }
            )
    );

    public EnchantalCoolerSideSpitItemAction(EntityMaid maid, CraftGuideData craftGuideData, CraftGuideStepData craftGuideStepData, CraftLayer layer) {
        super(maid, craftGuideData, craftGuideStepData, layer);
    }

    @Override
    public Result start() {
        this.correctStorage();
        return super.start();
    }

    private void correctStorage() {
        SpiltType spiltType = craftGuideStepData.getOptionSelection(OPTION_SPLIT_TYPE).orElseThrow();

        BlockPos pos = craftGuideStepData.getStorage().getPos();
        Direction direction = maid.level.getBlockState(pos).getValue(EnchantalCoolerBlock.FACING);
        Target storage = craftGuideStepData.storage;
        Direction directSide = direction;
        if (spiltType == SpiltType.CONTAINER) {
            for (Direction value : Direction.values()) {
                if (value == Direction.DOWN || value == Direction.UP || value == direction) {
                    continue;
                }
                directSide = value;
                break;
            }
        }
        craftGuideStepData.storage = new Target(storage.type, pos, directSide);

    }
}
