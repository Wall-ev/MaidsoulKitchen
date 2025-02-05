package com.github.wallev.maidsoulkitchen.task.cook.v1.bnc;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.init.registry.tlm.RegisterData;
import com.github.wallev.maidsoulkitchen.mixin.bnc.KegBlockEntityOfficeAccessor;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.handler.MaidRecipesManager;
import com.github.wallev.maidsoulkitchen.task.cook.v1.common.TaskFdPot;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;
import umpaz.brewinandchewin.common.crafting.KegFermentingRecipe;
import umpaz.brewinandchewin.common.crafting.KegPouringRecipe;
import umpaz.brewinandchewin.common.registry.BnCBlocks;
import umpaz.brewinandchewin.common.registry.BnCRecipeTypes;

import java.util.*;
import java.util.stream.Stream;


public class TaskBncKeyOffice extends TaskFdPot<KegBlockEntity, KegFermentingRecipe> {
    private static final Map<KegFermentingRecipe, List<Ingredient>> KEY_RECIPES = new HashMap<>();
    private static final Map<KegFermentingRecipe, Ingredient> KEY_RECIPES_FLUID = new HashMap<>();
    private static final Map<Fluid, List<ItemStack>> KEY_RECIPES_FLUIDS = new HashMap<>();

    @Override
    public ItemStackHandler getItemStackHandler(KegBlockEntity be) {
        return be.getInventory();
    }

    @Override
    public Optional<KegFermentingRecipe> getMatchingRecipe(KegBlockEntity be, RecipeWrapper recipeWrapper) {
        return ((KegBlockEntityOfficeAccessor) be).callGetMatchingRecipe(((KegBlockEntityOfficeAccessor) be).getRecipeWrapper());
    }

    @Override
    public boolean canCook(KegBlockEntity be, KegFermentingRecipe recipe) {
        return ((KegBlockEntityOfficeAccessor) be).callCanFerment(recipe, be);
    }

    @Override
    public int getOutputSlot() {
        return KegBlockEntity.OUTPUT_SLOT;
    }

    @Override
    public int getInputSize() {
        return 4;
    }

    @Override
    public ItemStackHandler getBeInv(KegBlockEntity kegBlockEntity) {
        return kegBlockEntity.getInventory();
    }

    @Override
    public int getMealStackSlot() {
        return 0;
    }

    @Override
    public int getContainerStackSlot() {
        return KegBlockEntity.CONTAINER_SLOT;
    }

    @Override
    public ItemStack getFoodContainer(KegBlockEntity blockEntity) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isHeated(KegBlockEntity be) {
        return true;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof KegBlockEntity;
    }

    @Override
    public RecipeType<KegFermentingRecipe> getRecipeType() {
        return BnCRecipeTypes.FERMENTING.get();
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.BNC_KEY.uid;
    }

    @Override
    public ItemStack getIcon() {
        return BnCBlocks.KEG.get().asItem().getDefaultInstance();
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.BNC_KEY;
    }

    @Override
    public List<KegFermentingRecipe> getRecipes(Level level) {
        List<KegFermentingRecipe> kegFermentingRecipes = super.getRecipes(level);

        if (KEY_RECIPES.isEmpty()) {
            Map<Fluid, Stream<ItemStack>> FLUID_CONTAINER = new HashMap<>();

            assert Minecraft.getInstance().level != null;
            List<KegPouringRecipe> kegPouringRecipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(BnCRecipeTypes.KEG_POURING.get());

            for (KegFermentingRecipe kegFermentingRecipe : kegFermentingRecipes) {
                FluidStack fluidIngredient = kegFermentingRecipe.getFluidIngredient();
                if (fluidIngredient != null && !fluidIngredient.isEmpty()) {
                    Stream<ItemStack> fluidItems = FLUID_CONTAINER.computeIfAbsent(fluidIngredient.getFluid(), fluid -> {
                        List<KegPouringRecipe> matchKegPouringRecipes = kegPouringRecipes.stream().filter(pouringRecipe -> pouringRecipe.getRawFluid().isSame(fluid)).toList();

                        List<ItemStack> containerStacks = matchKegPouringRecipes.stream().map(kegPouringRecipe -> {
                            ItemStack container = kegPouringRecipe.getContainer();
                            int amount = kegPouringRecipe.getAmount();
                            int amountTotal = kegFermentingRecipe.getAmount();
                            container.setCount(amountTotal / amount);
                            return container;
                        }).toList();
                        KEY_RECIPES_FLUIDS.put(fluidIngredient.getFluid(), containerStacks);

                        return matchKegPouringRecipes.stream().map(kegPouringRecipe -> {
                            ItemStack output = kegPouringRecipe.getOutput();
                            int amount = kegPouringRecipe.getAmount();
                            int amountTotal = kegFermentingRecipe.getAmount();
                            output.setCount(amountTotal / amount);
                            return output;
                        });
                    });

                    NonNullList<Ingredient> ingres = NonNullList.create();
                    ingres.addAll(kegFermentingRecipe.getIngredients());
                    Ingredient inputFluidIngredient = Ingredient.of(fluidItems);
                    ingres.add(inputFluidIngredient);

                    KEY_RECIPES_FLUID.put(kegFermentingRecipe, inputFluidIngredient);
                    KEY_RECIPES.put(kegFermentingRecipe, ingres);
                } else {
                    NonNullList<Ingredient> ingres = NonNullList.create();
                    ingres.addAll(kegFermentingRecipe.getIngredients());
                    Ingredient inputFluidIngredient = Ingredient.EMPTY;
                    ingres.add(inputFluidIngredient);

                    KEY_RECIPES_FLUID.put(kegFermentingRecipe, inputFluidIngredient);
                    KEY_RECIPES.put(kegFermentingRecipe, ingres);
                }
            }
        }

        return kegFermentingRecipes;
    }

    @Override
    public NonNullList<Ingredient> getIngredients(Recipe<?> recipe) {
        return (NonNullList<Ingredient>) KEY_RECIPES.get((KegFermentingRecipe) recipe);
    }

    @Override
    public MaidRecipesManager<KegFermentingRecipe> getRecipesManager(EntityMaid maid) {
        return new MaidRecipesManager<>(maid, this, false) {
            protected Pair<List<Integer>, List<Item>> getAmountIngredient(KegFermentingRecipe recipe, Map<Item, Integer> available) {
                List<Ingredient> ingredients = recipe.getIngredients();
                List<Item> invIngredient = new ArrayList<>();
                Map<Item, Integer> itemTimes = new HashMap<>();
                boolean[] canMake = {true};
                boolean[] single = {false};

                for (Ingredient ingredient : ingredients) {
                    boolean hasIngredient = false;
                    for (Item item : available.keySet()) {
                        ItemStack stack = item.getDefaultInstance();
                        if (ingredient.test(stack)) {
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
                        break;
                    }
                }

                if (!canMake[0] || itemTimes.entrySet().stream().anyMatch(entry -> available.get(entry.getKey()) < entry.getValue())) {
                    return Pair.of(Collections.emptyList(), Collections.emptyList());
                }

                int maxCount = 64;
                if (single[0]) {
                    maxCount = 1;
                } else {
                    for (Item item : itemTimes.keySet()) {
                        maxCount = Math.min(maxCount, item.getDefaultInstance().getMaxStackSize());
                        maxCount = Math.min(maxCount, available.get(item) / itemTimes.get(item));
                    }
                }

                List<Integer> countList = new ArrayList<>();
                for (Item item : invIngredient) {
                    countList.add(maxCount);
                    available.put(item, available.get(item) - maxCount);
                }

                Ingredient inputFluidIngredient = KEY_RECIPES_FLUID.get(recipe);
                if (!inputFluidIngredient.isEmpty()) {
                    single[0] = true;
                    ItemStack[] fluidIngredientItems = inputFluidIngredient.getItems();
                    for (ItemStack fluidIngredientItem : fluidIngredientItems) {
                        Item fluidItem = fluidIngredientItem.getItem();
                        int amount = fluidIngredientItem.getCount();
                        if (available.getOrDefault(fluidItem, 0) >= amount) {
                            countList.add(amount);
                            available.put(fluidItem, available.get(fluidItem) - maxCount);
                            return Pair.of(countList, invIngredient);
                        }
                    }

                    return Pair.of(Collections.emptyList(), Collections.emptyList());
                } else {
                    countList.add(null);
                    available.put(null, 0);

                    return Pair.of(countList, invIngredient);
                }
            }
        };
    }

    @Override
    public boolean maidShouldMoveTo(ServerLevel level, EntityMaid maid, KegBlockEntity keg, MaidRecipesManager<KegFermentingRecipe> manager) {
        ItemStackHandler inventory = keg.getInventory();

        // 完全烹饪好的食物，待取出，不需要任何附加条件才能取出
        if (inventory.getStackInSlot(5).isEmpty()) {
            return true;
        }

        // 有待取出流体和流体容器
        FluidTank fluidTank = keg.getFluidTank();
        Fluid fluid = fluidTank.getFluid().getFluid();
        if (fluid != null) {
            List<ItemStack> fluidStacks = KEY_RECIPES_FLUIDS.get(fluid);
            if (!fluidStacks.isEmpty()) {
                boolean hasFluidContainerStack = manager.hasOutputAdditionItem(itemStack -> {
                    for (ItemStack fluidStack : fluidStacks) {
                        if (fluidStack.is(itemStack.getItem()) && itemStack.getCount() >= fluidStack.getCount()) {
                            return true;
                        }
                    }
                    return false;
                });

                if (hasFluidContainerStack) {
                    return true;
                }
            }
        }

        //
        return false;
    }

    @Override
    public void maidCookMake(ServerLevel serverLevel, EntityMaid entityMaid, KegBlockEntity blockEntity, MaidRecipesManager<KegFermentingRecipe> maidRecipesManager) {
        super.maidCookMake(serverLevel, entityMaid, blockEntity, maidRecipesManager);
    }
}
