package com.github.wallev.maidsoulkitchen.compat.msm.common.craft.special;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.RecipeFinderUtil;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import studio.fantasyit.maid_storage_manager.craft.WorkBlockTags;
import studio.fantasyit.maid_storage_manager.craft.context.AbstractCraftActionContext;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideData;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;
import studio.fantasyit.maid_storage_manager.craft.work.CraftLayer;
import studio.fantasyit.maid_storage_manager.util.InvUtil;
import studio.fantasyit.maid_storage_manager.util.ItemStackUtil;

import java.util.Optional;

@TaskClassAnalyzer(TaskInfo.MSM_CORE)
public class StoneCutterRecipeAction extends AbstractCraftActionContext {
    public static final ResourceLocation TYPE = VResourceLocation.createMod("stone_cutter");

    public record Cutter(ResourceLocation recType) {
        public static final Codec<Cutter> CODEC = RecordCodecBuilder.create(ins -> ins.group(
                ResourceLocation.CODEC.fieldOf("recType").forGetter(Cutter::recType)
        ).apply(ins, Cutter::new));

        public CompoundTag toCompoundTag() {
            return CODEC.encodeStart(NbtOps.INSTANCE, this)
                    .result()
                    .map(tag -> (CompoundTag) tag)
                    .orElseGet(CompoundTag::new);
        }

        public static CompoundTag toCompoundTag(ResourceLocation recType) {
            return new Cutter(recType).toCompoundTag();
        }

        public static Cutter from(CompoundTag compoundTag) {
            return CODEC.parse(NbtOps.INSTANCE, compoundTag)
                    .result()
                    .orElseThrow();
        }
    }

    protected ResourceLocation recipeType;

    public StoneCutterRecipeAction(EntityMaid maid, CraftGuideData craftGuideData, CraftGuideStepData craftGuideStepData, CraftLayer layer) {
        super(maid, craftGuideData, craftGuideStepData, layer);
    }

    @Override
    public Result start() {
        if (craftGuideStepData.getStorage() == null)
            return Result.FAIL;

        CompoundTag extraData = craftGuideStepData.getExtraData();
        this.recipeType = Cutter.from(extraData).recType();

        return Result.CONTINUE;
    }

    @Override
    public Result tick() {
        Level level = maid.level();
        CombinedInvWrapper inv = maid.getAvailableInv(false);
        ItemStack input = craftGuideStepData.getInput().get(0);
        ItemStack output = craftGuideStepData.getOutput().get(0);
        ItemStack t1 = InvUtil.tryExtractForCrafting(inv, input);
        if (ItemStackUtil.isSameInCrafting(t1, input)) {
            Optional<Recipe<? extends Container>> first = RecipeFinderUtil.getCutterRecipe(level, recipeType, t1)
                    .stream()
                    .filter(recipe ->
                            ItemStackUtil.isSameInCrafting(recipe.getResultItem(level.registryAccess()), output)
                    ).findFirst();
            if (first.isPresent()) {
                ItemStack tmpResult = first.get().getResultItem(level.registryAccess());
                ItemStack result = tmpResult.copyWithCount(tmpResult.getCount() * input.getCount());
                if (ItemStackUtil.isSameInCrafting(result, output)) {
                    craftLayer.addCurrentStepPlacedCounts(0, result.getCount());

                    int maxCanPlace = InvUtil.maxCanPlace(inv, result);
                    if (maxCanPlace >= result.getCount()) {
                        InvUtil.tryPlace(inv, result);
                        level.playSound(null, craftGuideStepData.storage.pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
                        return Result.SUCCESS;
                    }
                }
            }
        }
        InvUtil.tryPlace(inv, t1);
        return Result.FAIL;
    }

    @Override
    public void stop() {

    }
}


