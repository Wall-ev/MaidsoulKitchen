package com.github.wallev.maidsoulkitchen.compat.msm.tea_aroma.util;

import cn.foggyhillside.tea_aroma.blocks.states.KettleLiquid;
import cn.foggyhillside.tea_aroma.items.KettleItem;
import cn.foggyhillside.tea_aroma.registry.ModTags;
import com.github.wallev.maidsoulkitchen.mixin.compat.tea_aroma.KettleItemAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TeaBrewingFoamHelper {

    public static Ingredient createNeedBoilingWaterKettle() {
        return createNeedBoilingWaterKettle(TeaBrewingFoamHelper::getNeedBoilingWaterKettle);
    }

    public static Ingredient createNeedBoilingMilkKettle() {
        return createNeedBoilingWaterKettle(TeaBrewingFoamHelper::getNeedBoilingMilkKettle);
    }

    private static Ingredient createNeedBoilingWaterKettle(Supplier<ItemStack> kettle) {
        int minBoil = getBoil();
        Stream<ItemStack> kettlesStream = IntStream.range(0, minBoil).boxed()
                .map(progress -> {
                    ItemStack defaultInstance = kettle.get();
                    setStackBoilProgress(defaultInstance, progress);
                    return defaultInstance;
                });

        return Ingredient.of(kettlesStream);
    }

    public static Ingredient getWaters() {
        Stream<ItemStack> modTagsWaterStream = Arrays.stream(Ingredient.of(ModTags.WATER).getItems());
        ItemStack waterPotion = Items.POTION.getDefaultInstance();
        PotionUtils.setPotion(waterPotion, Potions.WATER);
        Stream<ItemStack> allWaters = Stream.concat(modTagsWaterStream, Stream.of(waterPotion));
        return Ingredient.of(allWaters);
    }

    public static LiquidType forLiquidType(String liquidType) {
        return LiquidType.valueOf(liquidType.toUpperCase(Locale.ENGLISH));
    }

    public static Ingredient forLiquidIngredient(String liquidType) {
        return forLiquidType(liquidType).getIngredient();
    }

    public static Ingredient waterLiquidIngredient() {
        return LiquidType.BOILING_WATER.getIngredient();
    }

    public static Ingredient milkLiquidIngredient() {
        return LiquidType.BOILING_MILK.getIngredient();
    }

    public static ItemStack leftTeaFluidBase(ItemStack teaBase) {
        int amount = KettleItem.getStackAmount(teaBase);
        amount = Math.max(0, amount - 1);
        KettleItem.setStackAmount(teaBase, amount);
        if (amount == 0) {
            KettleItem.setStackLiquid(teaBase, KettleLiquid.NONE.toString());
        }
        return teaBase;
    }

    public static void setStackBoilProgress(ItemStack stack, int boilProgress) {
        KettleItemAccessor.msk$setStackBoilProgress(stack, boilProgress);
    }

    public static int getStackBoilProgress(ItemStack stack) {
        return KettleItemAccessor.msk$getStackBoilProgress(stack);
    }

    public static ItemStack getNeedBoilingMilkKettle() {
        ItemStack boilingMilkKettle = KettleItem.getBoilingMilkKettle();
        setStackBoilProgress(boilingMilkKettle, 0);
        KettleItem.setStackLiquid(boilingMilkKettle, KettleLiquid.MILK.toString());
        return boilingMilkKettle;
    }

    public static ItemStack getNeedBoilingWaterKettle() {
        ItemStack boilingWaterKettle = KettleItem.getBoilingWaterKettle();
        setStackBoilProgress(boilingWaterKettle, 0);
        KettleItem.setStackLiquid(boilingWaterKettle, KettleLiquid.WATER.toString());
        return boilingWaterKettle;
    }

    public static ItemStack getEmptyBoilingKettle() {
        return KettleItem.getEmptyKettle();
    }

    public static int getBoil() {
        return KettleItemAccessor.msk$getBoil();
    }

    public static int getMaxProgress() {
        return KettleItemAccessor.msk$getMaxProgress();
    }

    public enum LiquidType {
        BOILING_WATER,
        BOILING_MILK,
        ;

        public Ingredient getIngredient() {
            Stream<ItemStack> kettlesStream = IntStream.range(1, 4).boxed()
                    .flatMap(i -> {
                        int min = getBoil();
                        int max = getMaxProgress();
                        return IntStream.range(min, max + 1).boxed()
                                .map(progress -> {
                                    ItemStack defaultInstance = KettleItem.getBoilingWaterKettle();
                                    KettleItem.setStackLiquid(defaultInstance, this.toString());
                                    KettleItem.setStackAmount(defaultInstance, i);
                                    setStackBoilProgress(defaultInstance, progress);

                                    return defaultInstance;
                                });
                    });
            return Ingredient.of(kettlesStream);
        }

        @Override
        public String toString() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }

}
