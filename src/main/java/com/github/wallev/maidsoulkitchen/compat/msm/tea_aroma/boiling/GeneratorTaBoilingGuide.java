package com.github.wallev.maidsoulkitchen.compat.msm.tea_aroma.boiling;

import cn.foggyhillside.tea_aroma.blocks.entities.KettleEntity;
import cn.foggyhillside.tea_aroma.items.KettleItem;
import cn.foggyhillside.tea_aroma.registry.ModItems;
import cn.foggyhillside.tea_aroma.registry.ModTags;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.ICookingGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base.EnchantCommonIdleAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base.EnchantCommonUseAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.util.FailCraftGuideStepData;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.TargetUtil;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.TypeLang;
import com.github.wallev.maidsoulkitchen.compat.msm.tea_aroma.util.TeaBrewingFoamHelper;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOptionSet;
import studio.fantasyit.maid_storage_manager.craft.context.common.CommonIdleAction;
import studio.fantasyit.maid_storage_manager.craft.context.common.CommonUseAction;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_TA_BOILING)
public class GeneratorTaBoilingGuide implements ICookingGuideGenerator<GeneratorTaBoilingGuide.KettleIngredient> {
    @Override
    public boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return true;
    }

    @TypeLang(
            en_us = "Boiling water/milk",
            zh_cn = "烧水/热奶"
    )
    @Override
    @NotNull
    public ResourceLocation getType() {
        return VResourceLocation.createTypeMod(Mods.TA, "boiling");
    }

    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return level.getBlockState(pos).isAir() && KettleEntity.isHeated(level, pos);
//        return level.getBlockState(pos).isAir() && level.getBlockState(pos.below()).getOptionalValue(BlockStateProperties.LIT).orElse(false);
    }

    @Override
    public void generateSteps(BlockPos pos, Level level, KettleIngredient recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        CraftGuideOperator2.forEachSingleItem(realItems, itemStack -> {
            ItemStack craftingRemainingItem = itemStack.getCraftingRemainingItem();
            if (!craftingRemainingItem.isEmpty()) {
                craftGuide.addItemUse(itemStack, craftingRemainingItem);
            } else {
                craftGuide.addItemUse(itemStack);
            }
        });
        craftGuide.addStep(new CraftGuideStepData(
                TargetUtil.makeTargetVirtualNoSide(pos),
                List.of(),
                List.of(),
                EnchantCommonIdleAction.TYPE,
                ActionOptionSet.with(EnchantCommonIdleAction.OPTION_WAIT, true, "200")
        ));

        CraftGuideStepData failQueryKettle = new CraftGuideStepData(
                TargetUtil.makeTargetVirtualNoSide(pos),
                List.of(),
                List.of(),
                CommonUseAction.TYPE
        );
        CraftGuideStepData idle = new CraftGuideStepData(
                TargetUtil.makeTargetVirtualNoSide(pos),
                List.of(),
                List.of(),
                CommonIdleAction.TYPE
        );
        CompoundTag compoundTag = FailCraftGuideStepData.toCompoundTag(failQueryKettle, idle);
        craftGuide.addStep(new CraftGuideStepData(
                TargetUtil.makeTargetVirtualNoSide(pos),
                List.of(),
                outputs,
                EnchantCommonUseAction.TYPE,
                compoundTag
        ));

//        craftGuide.addStep(new CraftGuideStepData(
//                TargetUtil.makeTargetVirtualNoSide(pos),
//                List.of(),
//                List.of(),
//                EmptyAction.TYPE
//        ));


//        craftGuide.addEmptyUse(outputs);
    }

    @Override
    public void consumeRecipes(RecipeManager manager, Consumer<KettleIngredient> recipeConsumer) {
        Ingredient waterKettleInput = TeaBrewingFoamHelper.createNeedBoilingWaterKettle();
        recipeConsumer.accept(new KettleIngredient(waterKettleInput, KettleType.WATER));

        Ingredient milkKettleInput = TeaBrewingFoamHelper.createNeedBoilingMilkKettle();
        recipeConsumer.accept(new KettleIngredient(milkKettleInput, KettleType.MILK));

        Ingredient emptyWaterKettleInput = Ingredient.of(KettleItem.getEmptyKettle());
        recipeConsumer.accept(new KettleIngredient(emptyWaterKettleInput, KettleType.EMPTY_WATER));
        Ingredient emptyMilkKettleInput = Ingredient.of(KettleItem.getEmptyKettle());
        recipeConsumer.accept(new KettleIngredient(emptyMilkKettleInput, KettleType.EMPTY_MILK));
    }

    @Override
    public ResourceLocation getRecipeId(KettleIngredient recipe) {
        return VResourceLocation.createMod("boiling_" + recipe.type.name().toLowerCase(Locale.ENGLISH));
    }

    @Override
    public List<Ingredient> getInputs(KettleIngredient recipe) {
        List<Ingredient> inputs = switch (recipe.type) {
            case WATER, MILK -> List.of(recipe.ingredient);
            case EMPTY_WATER -> List.of(recipe.ingredient, TeaBrewingFoamHelper.getWaters());
            case EMPTY_MILK -> List.of(recipe.ingredient, Ingredient.of(ModTags.MILK));
        };

        return inputs;
    }

    @Override
    public List<ItemStack> getOutputs(KettleIngredient recipe, RegistryAccess registryAccess) {
        ItemStack output = switch (recipe.type) {
            case EMPTY_WATER, WATER -> KettleItem.getBoilingWaterKettle();
            case EMPTY_MILK, MILK -> KettleItem.getBoilingMilkKettle();
        };

        return List.of(output);
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ModItems.KETTLE.get();
    }

    public enum KettleType {
        WATER,
        MILK,
        EMPTY_WATER,
        EMPTY_MILK,
        ;
    }

    public record KettleIngredient(Ingredient ingredient, KettleType type) {
    }
}
