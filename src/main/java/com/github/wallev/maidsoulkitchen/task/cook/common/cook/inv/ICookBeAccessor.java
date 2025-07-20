package com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMccMixinInterface;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface ICookBeAccessor extends IMccMixinInterface {

    /**
     * 判断厨具内部的原料是否可以烹饪
     * <br>即有符合配方的原料
     * <br>但不会检测额外条件
     * <br>比如：需要燃料，加水等
     *
     * @return 是否可以烹饪
     */
    boolean kl$canCook();

    default boolean kl$matchCookState() {
        return false;
    }

    default <R extends Recipe<? extends Container>> boolean kl$canCook(IItemHandlerModifiable inv, Function<RecipeWrapper, Optional<R>> recMatchGet, Predicate<R> recCanCook) {
        return canCook(inv, recMatchGet, recCanCook);
    }

    default <R extends Recipe<? extends Container>> boolean kl$canCook(RecipeWrapper inv, Function<RecipeWrapper, Optional<R>> recMatchGet, Predicate<R> recCanCook) {
        return canCook(inv, recMatchGet, recCanCook);
    }

    static <R extends Recipe<? extends Container>> boolean canCook(IItemHandlerModifiable inv, Function<RecipeWrapper, Optional<R>> recMatchGet, Predicate<R> recCanCook) {
        RecipeWrapper recWrapper = new RecipeWrapper(inv);
        return canCook(recWrapper, recMatchGet, recCanCook);
    }

    static <R extends Recipe<? extends Container>> boolean canCook(RecipeWrapper recWrapper, Function<RecipeWrapper, Optional<R>> recMatchGet, Predicate<R> recCanCook) {
        return recMatchGet.apply(recWrapper)
                .map(recCanCook::test)
                .orElse(false);
    }

    default void kl$awardExp(Entity entity) {
        Map<ResourceLocation, Integer> usedRecs = kl$usedRecipeTracker();
        if (usedRecs.isEmpty()) {
            return;
        }

        int usedRecsCount = 0;
        for (Map.Entry<ResourceLocation, Integer> resourceLocationIntegerEntry : usedRecs.entrySet()) {
            usedRecsCount+=resourceLocationIntegerEntry.getValue();
        }

        if (usedRecsCount > 5) {
            this.kl$awardExperience(entity, this::kl$getUsedRecipesAndPopExperience, this.kl$usedRecipeTracker());
        }

    }

    default void kl$awardExperience(Entity entity, BiConsumer<Level, Vec3> getUsedRecipesAndPopExperience, Map<ResourceLocation, Integer> usedRecipeTracker) {
        getUsedRecipesAndPopExperience.accept(entity.level, entity.position);
        usedRecipeTracker.clear();
    }

    default void kl$getUsedRecipesAndPopExperience(Level level, Vec3 pos) {
    }

    default Map<ResourceLocation, Integer> kl$usedRecipeTracker() {
        return Map.of();
    }

    @SuppressWarnings("all")
    default <OBE> OBE kl$cast() {
        return (OBE) (Object) this;
    }

}
