package com.github.wallev.maidsoulkitchen.compat.msm.drinkbeer.mixed;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.nbtcustom.INbtCookingGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.nbtcustom.NbtItemTagGen;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.EmptyUseStepUtil;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import lekavar.lma.drinkbeer.blockentities.BartendingTableBlockEntity;
import lekavar.lma.drinkbeer.items.BeerMugItem;
import lekavar.lma.drinkbeer.items.MixedBeerBlockItem;
import lekavar.lma.drinkbeer.items.SpiceBlockItem;
import lekavar.lma.drinkbeer.managers.MixedBeerManager;
import lekavar.lma.drinkbeer.registries.ItemRegistry;
import lekavar.lma.drinkbeer.utils.beer.Beers;
import lekavar.lma.drinkbeer.utils.mixedbeer.Spices;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import studio.fantasyit.maid_storage_manager.craft.CollectCraftEvent;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;
import studio.fantasyit.maid_storage_manager.craft.generator.algo.ICachableGeneratorGraph;
import studio.fantasyit.maid_storage_manager.data.InventoryItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static lekavar.lma.drinkbeer.managers.MixedBeerManager.getBeerId;

@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_DB_MIXED_BEER)
public class GeneratorDrinkBeerMixedGuide extends INbtCookingGuideGenerator {

    @NbtItemTagGen(TaskInfo.MSM_DB_MIXED_BEER)
    public static final Item NBT_ITEM = ItemRegistry.MIXED_BEER.get();

    public GeneratorDrinkBeerMixedGuide(CollectCraftEvent event) {
        super(event);
    }

    @Override
    public boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return true;
    }

    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof BartendingTableBlockEntity;
    }

    @Override
    public boolean isSameItemStack(ItemStack stack, ItemStack target) {
        return ItemStack.isSameItemSameTags(stack, target);
    }

    @Override
    public Item getNbtItemStackItem() {
        return NBT_ITEM;
    }

    @Override
    public Ingredient getAllIngredient() {
        return createAllIngredient(item ->
                item instanceof SpiceBlockItem ||
                        item instanceof BeerMugItem ||
                        item instanceof MixedBeerBlockItem
        );
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
        CraftGuideOperator2.forEachItem(inputs, craftGuide::addItemUse);

        CraftGuideStepData failStep = EmptyUseStepUtil.makeOptionalStep(pos);
        craftGuide.addEmptyUseIfFail(results, failStep);
    }

    @Override
    public List<ItemStack> getInputsWithItemStack(ItemStack itemStack) {
        Beers beer = Beers.byId(getBeerId(itemStack));
        ItemStack beerItem = beer.getBeerItem().getDefaultInstance();

        List<Integer> spiceList = MixedBeerManager.getSpiceList(itemStack);
        List<ItemStack> spiceItems = spiceList.stream()
                .map(id -> Spices.byId(id).getSpiceItem().getDefaultInstance())
                .toList();

        List<ItemStack> allInputs = new ArrayList<>();
        allInputs.add(beerItem);
        allInputs.addAll(spiceItems);

        return allInputs;
    }

    @Override
    @NotNull
    public ResourceLocation getType() {
        return VResourceLocation.createTypeMod(Mods.DB, "beer_mixed");
    }

    @Override
    public List<ItemStack> getOutputs(ItemStack recipe, RegistryAccess registryAccess) {
        return List.of(recipe);
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ItemRegistry.BARTENDING_TABLE.get();
    }
}
