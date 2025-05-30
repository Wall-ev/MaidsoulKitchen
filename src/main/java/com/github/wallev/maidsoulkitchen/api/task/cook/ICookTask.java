package com.github.wallev.maidsoulkitchen.api.task.cook;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.github.wallev.maidsoulkitchen.api.IMaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.api.entry.TaskBookEntryType;
import com.github.wallev.maidsoulkitchen.api.event.MaidMkTaskEnableEvent;
import com.github.wallev.maidsoulkitchen.api.task.IDataTask;
import com.github.wallev.maidsoulkitchen.client.tooltip.RecipeDataTooltip.IngredientSourceType;
import com.github.wallev.maidsoulkitchen.client.tooltip.RecipeDataTooltip.IngredientType;
import com.github.wallev.maidsoulkitchen.client.tooltip.RecipeDataTooltip.TooltipRecIngredient;
import com.github.wallev.maidsoulkitchen.client.tooltip.RecipeDataTooltip.TooltipRecipeData;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.init.MkEntities;
import com.github.wallev.maidsoulkitchen.init.MkItems;
import com.github.wallev.maidsoulkitchen.inventory.container.maid.CookConfigContainer;
import com.github.wallev.maidsoulkitchen.task.cook.common.ai.MaidCookMakeTask;
import com.github.wallev.maidsoulkitchen.task.cook.common.ai.MaidCookMoveTask;
import com.github.wallev.maidsoulkitchen.task.cook.common.cbaccessor.IRecipeExperinceAward;
import com.github.wallev.maidsoulkitchen.task.cook.common.inventory.MaidRecipesManager;
import com.github.wallev.verhelper.server.ai.VBehaviorControl;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface ICookTask<B extends BlockEntity, R extends Recipe<? extends Container>> extends IMaidsoulKitchenTask, IDataTask<CookData> {
    static void awardExperience(BlockEntity blockEntity, EntityMaid maid) {
        if (blockEntity instanceof IRecipeExperinceAward iRecipeExperinceAward) {
            iRecipeExperinceAward.tlmk$awardExperience(maid);
        }
    }

    @Override
    default List<Pair<Integer, VBehaviorControl>> vCreateBrainTasks(EntityMaid maid) {
        if (maid.level.isClientSide) {
            return Collections.emptyList();
        }

        MaidRecipesManager<R> cookingPotRecipeMaidRecipesManager = getRecipesManager(maid);
        MaidCookMoveTask<B, R> maidCookMoveTask = new MaidCookMoveTask<>(this, cookingPotRecipeMaidRecipesManager);
        MaidCookMakeTask<B, R> maidCookMakeTask = new MaidCookMakeTask<>(this, cookingPotRecipeMaidRecipesManager);
        return Lists.newArrayList(Pair.of(5, maidCookMoveTask), Pair.of(6, maidCookMakeTask));
    }

    default MaidRecipesManager<R> getRecipesManager(EntityMaid maid) {
        return new MaidRecipesManager<>(maid, this, false);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default List<R> getRecipes(Level level) {
        return level.getRecipeManager().getAllRecipesFor((RecipeType) getRecipeType());
    }

    @Nullable
    @Override
    default SoundEvent getAmbientSound(EntityMaid maid) {
        return SoundUtil.environmentSound(maid, InitSounds.MAID_FURNACE.get(), 0.5f);
    }

    default double getCloseEnoughDist() {
        return 3.2;
    }

    default List<Pair<String, Predicate<EntityMaid>>> getEnableConditionDesc(EntityMaid maid) {
        MaidMkTaskEnableEvent maidMkTaskEnableEvent = new MaidMkTaskEnableEvent(maid, this);
        boolean eventPosted = MinecraftForge.EVENT_BUS.post(maidMkTaskEnableEvent);
        if (eventPosted) {
            return maidMkTaskEnableEvent.getEnableConditionDesc();
        }

        return Lists.newArrayList(Pair.of("has_enough_favor", this::hasEnoughFavor));
    }

    @Override
    default boolean isEnable(EntityMaid maid) {
        MaidMkTaskEnableEvent maidMkTaskEnableEvent = new MaidMkTaskEnableEvent(maid, this);
        boolean eventPosted = MinecraftForge.EVENT_BUS.post(maidMkTaskEnableEvent);
        if (eventPosted) {
            return maidMkTaskEnableEvent.isEnable();
        }

        return hasEnoughFavor(maid);
    }

    @Override
    default MenuProvider getTaskConfigGuiProvider(EntityMaid maid) {
        final int entityId = maid.getId();
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.literal("Maid Cook Config Container2");
            }

            @Override
            public AbstractContainerMenu createMenu(int index, Inventory playerInventory, Player player) {
                return new CookConfigContainer(index, playerInventory, entityId);
            }
        };
    }

    default boolean hasEnoughFavor(EntityMaid maid) {
        return maid.getFavorabilityManager().getLevel() >= 1;
    }

    boolean isCookBE(BlockEntity blockEntity);

    RecipeType<R> getRecipeType();

    boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid maid, B blockEntity, MaidRecipesManager<R> recManager);

    void processCookMake(ServerLevel serverLevel, EntityMaid maid, B blockEntity, MaidRecipesManager<R> recManager);

    @Override
    default TaskBookEntryType getBookEntryType() {
        return TaskBookEntryType.COOK;
    }

    @Override
    default CookData getDefaultData() {
        return new CookData();
    }

    default NonNullList<Ingredient> getIngredients(Recipe<?> recipe) {
        return recipe.getIngredients();
    }

    default ItemStack getResultItem(Recipe<?> recipe, RegistryAccess pRegistryAccess) {
        return recipe.getResultItem(pRegistryAccess);
    }

    @OnlyIn(Dist.CLIENT)
    default Optional<TooltipComponent> getRecClientAmountTooltip(Recipe<?> recipe, boolean modeIsBlacklist, boolean overSize, CookData cookData, EntityMaid maid) {
        List<Ingredient> ingres = this.getIngredients(recipe);

        List<List<IngredientSourceType>> source = new ArrayList<>();
        source.add(List.of(IngredientSourceType.MAIN_HAND, IngredientSourceType.OFF_HAND, IngredientSourceType.MAID_BACKPACK));
        source.add(List.of(IngredientSourceType.HUB_INGREDIENT));
        int ruleMatchIndex = maid.getMaidInv().getStackInSlot(4).is(MkItems.CULINARY_HUB.get()) ? 1 : 0;
        TooltipRecIngredient tooltipRecIngredient = new TooltipRecIngredient(ingres, source, IngredientType.MANDATORY, ruleMatchIndex);

        TooltipRecIngredient tooltipRecResultIngredient = getTooltipRecResultIngredient(recipe, maid);
        TooltipRecipeData tooltipRecipeData = new TooltipRecipeData(cookData, recipe.getId().toString(), List.of(tooltipRecIngredient), tooltipRecResultIngredient, modeIsBlacklist, overSize);
        return Optional.of(tooltipRecipeData);
    }

    @OnlyIn(Dist.CLIENT)
    default TooltipRecIngredient getTooltipRecResultIngredient(Recipe<?> recipe, EntityMaid maid) {
        List<List<IngredientSourceType>> result = new ArrayList<>();
        result.add(List.of(IngredientSourceType.MAIN_HAND, IngredientSourceType.OFF_HAND, IngredientSourceType.MAID_BACKPACK));
        result.add(List.of(IngredientSourceType.HUB_OUTPUT));
        int resultRuleMatchIndex = maid.getMaidInv().getStackInSlot(4).is(MkItems.CULINARY_HUB.get()) ? 1 : 0;
        TooltipRecIngredient tooltipRecResultIngredient = new TooltipRecIngredient(List.of(Ingredient.of(this.getResultItem(recipe, maid.level.registryAccess()))), result, IngredientType.OUTPUT, resultRuleMatchIndex);
        return tooltipRecResultIngredient;
    }

    @OnlyIn(Dist.CLIENT)
    default List<Component> getWarnComponent() {
        return Collections.emptyList();
    }

    @Override
    default boolean enableLookAndRandomWalk(EntityMaid maid) {
        // 工作中禁止游走
        return !maid.getBrain().hasMemoryValue(MkEntities.WORK_POS.get());
    }

    @Override
    default boolean enableEating(EntityMaid maid) {
        // 工作中禁止吃饭
        return !maid.getBrain().hasMemoryValue(MkEntities.WORK_POS.get());
    }
}
