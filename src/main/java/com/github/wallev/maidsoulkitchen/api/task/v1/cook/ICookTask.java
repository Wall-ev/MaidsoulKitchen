package com.github.wallev.maidsoulkitchen.api.task.v1.cook;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.github.wallev.maidsoulkitchen.api.ILittleMaidTask;
import com.github.wallev.maidsoulkitchen.api.TaskBookEntryType;
import com.github.wallev.maidsoulkitchen.api.task.IDataTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.inventory.container.maid.CookConfigContainer;
import com.github.wallev.maidsoulkitchen.inventory.tooltip.AmountTooltip;
import com.github.wallev.maidsoulkitchen.task.ai.MaidCookMakeTask;
import com.github.wallev.maidsoulkitchen.task.ai.MaidCookMoveTask;
import com.github.wallev.maidsoulkitchen.task.cook.handler.MaidRecipesManager;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
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
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface ICookTask<B extends BlockEntity, R extends Recipe<? extends Container>> extends ILittleMaidTask, IDataTask<CookData> {

    default List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
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
        return Lists.newArrayList(Pair.of("has_enough_favor", this::hasEnoughFavor));
    }

    @Override
    default boolean isEnable(EntityMaid maid) {
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

    default Optional<TooltipComponent> getRecClientAmountTooltip(Recipe<?> recipe, boolean modeRandom, boolean overSize) {
        List<Ingredient> ingres = this.getIngredients(recipe);
        return ingres.isEmpty() ? Optional.empty() : Optional.of(new AmountTooltip(ingres, modeRandom, overSize));
    }

    default List<Component> getWarnComponent() {
        return Collections.emptyList();
    }
}
