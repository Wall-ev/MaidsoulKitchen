package com.github.wallev.maidsoulkitchen.task.cook.v1.farmersrespite;

import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.init.registry.tlm.RegisterData;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.handler.MaidRecipesManager;
import com.github.wallev.maidsoulkitchen.task.cook.v1.common.TaskFdPot;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
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
import java.util.stream.Stream;


public class TaskFrKettle1 extends TaskFdPot<KettleBlockEntity, KettleRecipe> {
    private static final Map<KettleRecipe, List<Ingredient>> KEY_RECIPES = new HashMap<>();
    private static final Map<KettleRecipe, Ingredient> KEY_RECIPES_FLUID = new HashMap<>();
    private static final Map<Fluid, List<ItemStack>> FLUID_CONTAINERS = new HashMap<>();

    private static final Map<KettleRecipe, KettleRecipeWrapper> KETTLE_UNION_RECIPES = new HashMap<>();

    @Override
    public boolean shouldMoveTo(ServerLevel level, EntityMaid maid, KettleBlockEntity be, MaidRecipesManager<KettleRecipe> manager) {
        ItemStackHandler itemStackHandler = getItemStackHandler(be);

        return super.shouldMoveTo(level, maid, be, manager);
    }

    @SuppressWarnings("all")
    @Override
    public List<KettleRecipe> getRecipes(Level level) {
        List<KettleRecipe> KettleRecipes = super.getRecipes(level);

        if (KEY_RECIPES.isEmpty()) {
            Map<Fluid, Stream<ItemStack>> FLUID_CONTAINER = new HashMap<>();

            assert Minecraft.getInstance().level != null;
            List<KettlePouringRecipe> kegPouringRecipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(FRRecipeTypes.KETTLE_POURING.get());

            for (KettleRecipe KettleRecipe : KettleRecipes) {
                FluidStack fluidIngredientIn = KettleRecipe.getFluidIn();
                if (fluidIngredientIn != null && !fluidIngredientIn.isEmpty()) {
                    Stream<ItemStack> fluidItems = FLUID_CONTAINER.computeIfAbsent(fluidIngredientIn.getFluid(), fluid -> {
                        List<KettlePouringRecipe> matchKegPouringRecipes = kegPouringRecipes.stream().filter(pouringRecipe -> pouringRecipe.getFluid().isSame(fluid)).toList();

                        List<ItemStack> containerStacks = matchKegPouringRecipes.stream().map(kegPouringRecipe -> {
                            ItemStack container = kegPouringRecipe.getContainer();
                            int amount = kegPouringRecipe.getAmount();
                            int amountTotal = fluidIngredientIn.getAmount();
                            container.setCount(amountTotal / amount);
                            return container;
                        }).toList();
                        FLUID_CONTAINERS.put(fluidIngredientIn.getFluid(), containerStacks);

                        return matchKegPouringRecipes.stream().map(kegPouringRecipe -> {
                            ItemStack output = kegPouringRecipe.getOutput();
                            int amount = kegPouringRecipe.getAmount();
                            int amountTotal = fluidIngredientIn.getAmount();
                            output.setCount(amountTotal / amount);
                            return output;
                        });
                    });



                    NonNullList<Ingredient> ingres = NonNullList.create();
                    ingres.addAll(KettleRecipe.getIngredients());
                    Ingredient inputFluidIngredient = Ingredient.of(fluidItems);
                    ingres.add(0, inputFluidIngredient);

                    KEY_RECIPES_FLUID.put(KettleRecipe, inputFluidIngredient);
                    KEY_RECIPES.put(KettleRecipe, ingres);
                } else {
                    NonNullList<Ingredient> ingres = NonNullList.create();
                    ingres.addAll(KettleRecipe.getIngredients());
                    Ingredient inputFluidIngredient = Ingredient.EMPTY;
                    ingres.add(inputFluidIngredient);

                    KEY_RECIPES_FLUID.put(KettleRecipe, inputFluidIngredient);
                    KEY_RECIPES.put(KettleRecipe, ingres);
                }

                FluidStack fluidIngredientOut = KettleRecipe.getFluidOut();
                if (fluidIngredientOut != null && !fluidIngredientOut.isEmpty()) {
                    Stream<ItemStack> fluidItems = FLUID_CONTAINER.computeIfAbsent(fluidIngredientOut.getFluid(), fluid -> {
                        List<KettlePouringRecipe> matchKegPouringRecipes = kegPouringRecipes.stream().filter(pouringRecipe -> pouringRecipe.getFluid().isSame(fluid)).toList();

                        List<ItemStack> containerStacks = matchKegPouringRecipes.stream().map(kegPouringRecipe -> {
                            ItemStack container = kegPouringRecipe.getContainer();
                            int amount = kegPouringRecipe.getAmount();
                            int amountTotal = fluidIngredientOut.getAmount();
                            container.setCount(amountTotal / amount);
                            return container;
                        }).toList();
                        FLUID_CONTAINERS.put(fluidIngredientOut.getFluid(), containerStacks);

                        return matchKegPouringRecipes.stream().map(kegPouringRecipe -> {
                            ItemStack output = kegPouringRecipe.getOutput();
                            int amount = kegPouringRecipe.getAmount();
                            int amountTotal = fluidIngredientOut.getAmount();
                            output.setCount(amountTotal / amount);
                            return output;
                        });
                    });

                    Ingredient outputFluidIngredient = Ingredient.of(fluidItems);
                    KEY_RECIPES_FLUID.put(KettleRecipe, outputFluidIngredient);
                }
            }


            int i = 0;
            for (KettleRecipe recipe : KettleRecipes) {
                List<KettlePouringRecipe> pours = level.getRecipeManager().getAllRecipesFor((RecipeType) FRRecipeTypes.KETTLE_POURING.get());
                List<KettleRecipe> recipes = super.getRecipes(level);
                KETTLE_UNION_RECIPES.put(recipe, new KettleRecipeWrapper(pours.get(i).getOutput(), pours.get(i).getContainer(), pours.get(i).getContainer()));
                i++;
            }
        }

        return KettleRecipes;
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
    public ItemStackHandler getBeInv(KettleBlockEntity kettleBlockEntity) {
        return kettleBlockEntity.getInventory();
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.FR_KETTLE.uid;
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
            protected Pair<List<Integer>, List<Item>> getAmountIngredient(KettleRecipe recipe, Map<Item, Integer> available) {
                return super.getAmountIngredient(recipe, available);
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
    public void insertInputsStack(ItemStackHandler beInv, IItemHandlerModifiable maidInv, KettleBlockEntity be, Pair<List<Integer>, List<List<ItemStack>>> ingredientPair) {
        List<Integer> amounts = ingredientPair.getFirst();
        List<List<ItemStack>> ingredients = ingredientPair.getSecond();

        if (hasEnoughIngredient(amounts, ingredients)) {
            for (int i = getInputStartSlot() + 1, j = 0; i < ingredients.size() + getInputStartSlot(); i++, j++) {
                insertAndShrink(beInv, amounts.get(j), ingredients, j, i);
            }
            be.setChanged();
        }

        KettleBlockEntity kettleBlockEntity = (KettleBlockEntity) be;
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
