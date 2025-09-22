package com.github.wallev.maidsoulkitchen.mixin.compat.farmersdelight;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMskMixinInterface;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import vectorwing.farmersdelight.common.crafting.DoughRecipe;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ForgeTags;

@Mixin(value = DoughRecipe.class, remap = false)
public abstract class DoughRecipeMixin extends CustomRecipe implements IMskMixinInterface {

    public DoughRecipeMixin(ResourceLocation pId, CraftingBookCategory pCategory) {
        super(pId, pCategory);
    }

//    @Override
//    public ResourceLocation getId() {
//        return new ResourceLocation("minecraft", "farmer_delight/dough");
//    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> withCapacity = NonNullList.createWithCapacity(2);
        withCapacity.add(Ingredient.of(Items.WHEAT));
        withCapacity.add(Ingredient.of(ForgeTags.BUCKETS_WATER));

        return withCapacity;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return new ItemStack(ModItems.WHEAT_DOUGH.get());
    }
}
