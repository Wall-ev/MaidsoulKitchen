package com.github.wallev.farmsoulkitchen.task.cook.v1.fr;

import com.github.wallev.farmsoulkitchen.FarmsoulKitchen;
import com.github.wallev.farmsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.farmsoulkitchen.init.registry.tlm.RegisterData;
import com.github.wallev.farmsoulkitchen.task.cook.handler.v2.MaidRecipesManager;
import com.github.wallev.farmsoulkitchen.task.cook.v1.common.TaskFdPot;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import umpaz.farmersrespite.common.block.entity.KettleBlockEntity;
import umpaz.farmersrespite.common.crafting.KettlePouringRecipe;
import umpaz.farmersrespite.common.crafting.KettleRecipe;
import umpaz.farmersrespite.common.registry.FRItems;
import umpaz.farmersrespite.common.registry.FRRecipeTypes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class TaskFrKettle extends TaskFdPot<KettleBlockEntity, KettleRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(FarmsoulKitchen.MOD_ID, "fr_kettle");
    private static final Map<KettleRecipe, KettleRecipeWrapper> KETTLE_UNION_RECIPES = new HashMap<>();

    @Override
    public boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid maid, KettleBlockEntity blockEntity, MaidRecipesManager<KettleRecipe> recManager) {
        ItemStackHandler itemStackHandler = getItemStackHandler(blockEntity);

        return super.shouldMoveTo(serverLevel, maid, blockEntity, recManager);
    }

    @SuppressWarnings("all")
    @Override
    public List<KettleRecipe> getRecipes(Level level) {
        if (KETTLE_UNION_RECIPES.isEmpty()) {
            List<KettlePouringRecipe> pours = level.getRecipeManager().getAllRecipesFor((RecipeType) FRRecipeTypes.KETTLE_POURING.get());
            List<KettleRecipe> recipes = super.getRecipes(level);
            int i = 0;
            for (KettleRecipe recipe : recipes) {
                KETTLE_UNION_RECIPES.put(recipe, new KettleRecipeWrapper(pours.get(i).getOutput(), pours.get(i).getContainer(), pours.get(i).getContainer()));
                i++;
            }
        }
        return KETTLE_UNION_RECIPES.keySet().stream().toList();
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof KettleBlockEntity;
    }

    @Override
    public RecipeType<KettleRecipe> getRecipeType() {
        return FRRecipeTypes.BREWING.get();
    }

    @Override
    public int getOutputSlot() {
        return KettleBlockEntity.OUTPUT_SLOT;
    }

    @Override
    public int getInputSize() {
        return 2;
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return FRItems.KETTLE.get().getDefaultInstance();
    }

    @Override
    public int getMealStackSlot() {
        return KettleBlockEntity.CONTAINER_SLOT;
    }

    @Override
    public int getContainerStackSlot() {
        return KettleBlockEntity.DRINK_DISPLAY_SLOT;
    }

    @Override
    public @NotNull ItemStack getBeInvMealStack(KettleBlockEntity be, ItemStackHandler inventory) {
        return be.getFluidTank().isEmpty() ? ItemStack.EMPTY : Items.GLASS_BOTTLE.getDefaultInstance();
    }

    @Override
    public MaidRecipesManager<KettleRecipe> getRecipesManager(EntityMaid maid) {
        return new MaidRecipesManager<>(maid, this, false) {
            @Override
            protected boolean extraStartRecipe(KettleRecipe recipe, Map<Item, Integer> available, boolean[] single, boolean[] canMake, Map<Item, Integer> itemTimes, List<Item> invIngredient) {
                return tExtraStartRecipe(recipe, available, single, canMake, itemTimes, invIngredient);
            }

            @Override
            protected boolean extraEndRecipe(KettleRecipe recipe, Map<Item, Integer> available, boolean[] single, boolean[] canMake, Map<Item, Integer> itemTimes, List<Item> invIngredient) {
                return super.extraEndRecipe(recipe, available, single, canMake, itemTimes, invIngredient);
            }

            @Override
            protected Pair<List<Integer>, List<Item>> getAmountIngredient(List<Item> invIngredient, Map<Item, Integer> itemTimes, KettleRecipe recipe, Map<Item, Integer> available) {
                return super.getAmountIngredient(invIngredient, itemTimes, recipe, available);
            }
        };
    }

    protected boolean tExtraStartRecipe(KettleRecipe recipe, Map<Item, Integer> available, boolean[] single, boolean[] canMake, Map<Item, Integer> itemTimes, List<Item> invIngredient) {
//        KettleBlockEntity.fluidExtract ...
        ItemStack extraItem = KETTLE_UNION_RECIPES.get(recipe).container;
        boolean hasIngredient = false;
        for (Item item : available.keySet()) {
            ItemStack stack = item.getDefaultInstance();
            if (stack.is(extraItem.getItem())) {
                invIngredient.add(item);
                hasIngredient = true;

                if (stack.getMaxStackSize() == 1) {
                    single[0] = true;
                    itemTimes.put(item, 1);
                } else {
                    itemTimes.merge(item, 1, Integer::sum);
                }

                break;
            }
        }

        if (!hasIngredient) {
            canMake[0] = false;
            itemTimes.clear();
            invIngredient.clear();

            return false;
        }

        return true;
    }

    @Override
    public ItemStack getFoodContainer(KettleBlockEntity kettle) {
        int mealStackSlot = getMealStackSlot();
        ItemStackHandler itemStackHandler = getItemStackHandler(kettle);
        ItemStack mealStack = itemStackHandler.getStackInSlot(mealStackSlot);
        Optional<KettlePouringRecipe> pouringRecipe = kettle.getPouringRecipe(mealStack.getItem(), kettle.getFluidTank().getFluid());
        if (pouringRecipe.isPresent()) {
            return pouringRecipe.get().getContainer();
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void insertInputStack(ItemStackHandler beInv, IItemHandlerModifiable availableInv, BlockEntity blockEntity, Pair<List<Integer>, List<List<ItemStack>>> ingredientPair) {
        List<Integer> amounts = ingredientPair.getFirst();
        List<List<ItemStack>> ingredients = ingredientPair.getSecond();

        if (hasEnoughIngredient(amounts, ingredients)) {
            for (int i = getInputStartSlot() + 1, j = 0; i < ingredients.size() + getInputStartSlot(); i++, j++) {
                insertAndShrink(beInv, amounts.get(j), ingredients, j, i);
            }
            blockEntity.setChanged();
        }

        KettleBlockEntity kettleBlockEntity = (KettleBlockEntity) blockEntity;
        List<ItemStack> itemStacks = ingredients.get(0);
        for (ItemStack itemStack : itemStacks) {

        }

        updateIngredient(ingredientPair);
    }

    @Override
    public ItemStackHandler getItemStackHandler(KettleBlockEntity be) {
        return be.getInventory();
    }

    @Override
    public boolean isHeated(KettleBlockEntity be) {
        return be.isHeated();
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.FR_KETTLE;
    }

    @Override
    public ItemStack getResultItem(Recipe<?> recipe, RegistryAccess pRegistryAccess) {
        return KETTLE_UNION_RECIPES.get((KettleRecipe) recipe).output;
    }

    public static class KettleRecipeWrapper {
        public final ItemStack output;
        public final ItemStack container;
        public final ItemStack fluidStack;

//        public final ItemStack meal;

        public KettleRecipeWrapper(ItemStack output, ItemStack container, ItemStack fluidStack) {
            this.output = output;
            this.container = container;
//            this.meal = meal;
            this.fluidStack = fluidStack;
        }
    }
}
