package com.github.wallev.maidsoulkitchen.compat.msm.bakeries.rollcake;

import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.nbtcustom.INbtCookingGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.nbtcustom.NbtItemTagGen;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base.EnchantCommonAttackAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.TypeLang;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import com.google.common.collect.Lists;
import com.renyigesai.bakeries.init.BakeriesItems;
import com.renyigesai.bakeries.item.CakeRollItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import studio.fantasyit.maid_storage_manager.craft.CollectCraftEvent;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;
import studio.fantasyit.maid_storage_manager.craft.generator.algo.ICachableGeneratorGraph;
import studio.fantasyit.maid_storage_manager.data.InventoryItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_BAKERIES_CAKE_ROLL)
public class GeneratorBakerRollCakeGuide extends INbtCookingGuideGenerator {

    @NbtItemTagGen(TaskInfo.MSM_BAKERIES_CAKE_ROLL)
    public static final Item NBT_ITEM = BakeriesItems.CAKE_ROLL.get();

    public GeneratorBakerRollCakeGuide(CollectCraftEvent event) {
        super(event);
    }

    @Override
    public boolean isSameItemStack(ItemStack stack, ItemStack target) {
        List<ItemStack> stackInvItems = CakeRollItem.getInventoryList(stack);
        List<ItemStack> targetInvItems = CakeRollItem.getInventoryList(target);
        for (int i = 0; i < stackInvItems.size(); i++) {
            ItemStack stackInvItem = stackInvItems.get(i);
            ItemStack targetInvItem = targetInvItems.get(i);
            if (!ItemStack.isSameItemSameTags(stackInvItem, targetInvItem)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Item getNbtItemStackItem() {
        return NBT_ITEM;
    }

    @Override
    public Ingredient getAllIngredient() {
        Stream<ItemStack> itemStream = ForgeRegistries.ITEMS
                .getValues()
                .stream()
                .filter(Item::isEdible)
                .map(Item::getDefaultInstance);
        return Ingredient.of(itemStream);
    }

    @Override
    public void generateStep(List<InventoryItem> inventory,
                             Level level,
                             BlockPos pos,
                             ICachableGeneratorGraph graph,
                             Map<ResourceLocation, List<BlockPos>> recognizedTypePositions,
                             CraftGuideOperator2 craftGuide,
                             List<ItemStack> results,
                             List<ItemStack> inputs) {
        ArrayList<ItemStack> itemStacks = Lists.newArrayList(inputs);
        craftGuide.addItemUse(pos, itemStacks.remove(0));

        itemStacks.forEach(item -> craftGuide.addItemUse(pos.above(), item));
        for (int i = 0; i < 2; i++) {
            craftGuide.addEmptyUse(pos.above());
        }

        CraftGuideStepData failStep = EnchantCommonAttackAction.createStep(pos.above());
        craftGuide.addItemPickupIfFail(pos.above(), results, failStep);
    }

    @TypeLang(
            en_us = "Make cake roll",
            zh_cn = "制作蛋糕卷"
    )
    @Override
    @NotNull
    public ResourceLocation getType() {
        return VResourceLocation.createTypeMod(Mods.BAKERIES, "cake_roll");
    }

    public List<ItemStack> getInputsWithItemStack(ItemStack itemStack) {
        List<ItemStack> inventoryItems = CakeRollItem.getInventoryList(itemStack);

        List<ItemStack> allIngredients = new ArrayList<>();
        allIngredients.add(BakeriesItems.SILICONE_PAPER.get().getDefaultInstance());
        allIngredients.add(BakeriesItems.CUT_CAKE_BASE.get().getDefaultInstance());
        List<ItemStack> beInvItems = inventoryItems.stream()
                .map(itemstack -> new ItemStack(itemstack.getItem(), 1))
                .toList();
        allIngredients.addAll(beInvItems);
        return allIngredients;
    }

    @Override
    public List<ItemStack> getOutputs(ItemStack recipe, RegistryAccess registryAccess) {
        return Collections.singletonList(recipe.copyWithCount(2));
    }

    @Override
    public Item getBlockItemForTranslate() {
        return NBT_ITEM;
    }
}
