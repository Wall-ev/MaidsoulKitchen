package com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SmokingRecipe;

import java.util.Collections;
import java.util.List;

public record MaidRec(Recipe<?> recipe, int time, ItemStack result, int amount, ItemStack oil, ItemStack tool, ItemStack container, List<MaidItem> maidItems, MaidItem fluidItem) {
    public static ResourceLocation EMPTY_RES = new ResourceLocation(MaidsoulKitchen.MOD_ID, "empty_rec");
    public static SmokingRecipe EMPTY_REC = new SmokingRecipe(EMPTY_RES, "", CookingBookCategory.MISC, Ingredient.EMPTY, ItemStack.EMPTY, 0f, 0);

    public static final MaidRec EMPTY = new MaidRec(EMPTY_REC, ItemStack.EMPTY, 0, Collections.emptyList(), MaidItem.EMPTY);
    public static final Codec<MaidRec> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            Codec.INT.fieldOf("time").forGetter(o -> {
                return o.time;
            }),
            ItemStack.CODEC.fieldOf("result").forGetter(o -> {
                return o.result;
            }),
            Codec.INT.fieldOf("amount").forGetter(o -> {
                return o.amount;
            }),
            ItemStack.CODEC.fieldOf("oil").forGetter(o -> {
                return o.oil;
            }),
            ItemStack.CODEC.fieldOf("tool").forGetter(o -> {
                return o.tool;
            }),
            ItemStack.CODEC.fieldOf("container").forGetter(o -> {
                return o.container;
            }),
            MaidItem.CODEC.listOf().fieldOf("maidItems").forGetter(o -> {
                return o.maidItems;
            }),
            MaidItem.CODEC.fieldOf("fluidItem").forGetter(o -> {
                return o.fluidItem;
            })
    ).apply(ins, (time0, result, amount, oil, tool, container, maidItems, fluidItem) -> {
        return new MaidRec(null, time0, result, amount, oil, tool, container, maidItems, fluidItem);
    }));

    public MaidRec(Recipe<?> recipe, ItemStack result, int amount, ItemStack tool, ItemStack container, List<MaidItem> maidItems, MaidItem fluidItem) {
        this(recipe, 0, result, amount, ItemStack.EMPTY, tool, container, maidItems, fluidItem);
    }
    public MaidRec(Recipe<?> recipe, int time, ItemStack result, int amount, ItemStack tool, ItemStack container, List<MaidItem> maidItems, MaidItem fluidItem) {
        this(recipe, time, result, amount, ItemStack.EMPTY, tool, container, maidItems, fluidItem);
    }

    public MaidRec(Recipe<?> recipe, ItemStack result, int amount, List<MaidItem> maidItems, MaidItem fluidItem) {
        this(recipe, result, amount, ItemStack.EMPTY, ItemStack.EMPTY, maidItems, fluidItem);
    }

    public MaidRec(Recipe<?> recipe, int time, ItemStack result, int amount, List<MaidItem> maidItems, MaidItem fluidItem) {
        this(recipe, time, result, amount, ItemStack.EMPTY, ItemStack.EMPTY, maidItems, fluidItem);
    }

    public MaidRec(Recipe<?> recipe, ItemStack result, int amount, List<MaidItem> maidItems) {
        this(recipe, result, amount, maidItems, MaidItem.EMPTY);
    }

    public MaidRec(Recipe<?> recipe, int time, ItemStack result, int amount, List<MaidItem> maidItems) {
        this(recipe, time, result, amount, maidItems, MaidItem.EMPTY);
    }

    public MaidRec(Recipe<?> recipe, ItemStack result, int amount, ItemStack tool, List<MaidItem> maidItems) {
        this(recipe, result, amount, tool, ItemStack.EMPTY, maidItems, MaidItem.EMPTY);
    }

    public MaidRec(Recipe<?> recipe, int time, ItemStack result, int amount, ItemStack tool, List<MaidItem> maidItems) {
        this(recipe, time, result, amount, tool, ItemStack.EMPTY, maidItems, MaidItem.EMPTY);
    }

    public MaidRec(Recipe<?> recipe, ItemStack result, int amount, ItemStack tool, ItemStack container, List<MaidItem> maidItems) {
        this(recipe, result, amount, tool, container, maidItems, MaidItem.EMPTY);
    }

    public MaidRec(Recipe<?> recipe, int time, ItemStack result, int amount, ItemStack tool, ItemStack container, List<MaidItem> maidItems) {
        this(recipe, time, result, amount, tool, container, maidItems, MaidItem.EMPTY);
    }

    @SuppressWarnings("unchecked")
    public <R extends Recipe<? extends Container>> R recCast() {
        return (R) this.recipe();
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }
}
