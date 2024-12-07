package com.github.wallev.farmsoulkitchen.task.cook.handler.v2;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IChestType;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.chest.ChestManager;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.wallev.farmsoulkitchen.api.task.v1.cook.ICookTask;
import com.github.wallev.farmsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.farmsoulkitchen.init.InitItems;
import com.github.wallev.farmsoulkitchen.inventory.container.item.BagType;
import com.github.wallev.farmsoulkitchen.item.ItemCulinaryHub;
import com.github.wallev.farmsoulkitchen.task.cook.handler.CookBagInventory;
import com.github.wallev.farmsoulkitchen.task.cook.handler.ICookInventory;
import com.github.wallev.farmsoulkitchen.task.cook.handler.MaidInventory;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MaidRecipesManager<R extends Recipe<? extends Container>> {
    private final EntityMaid maid;
    protected final List<R> rec = new ArrayList<>();
    protected final List<R> currentRecs = new ArrayList<>();
    private final MaidInventory maidInventory;
    private final ICookTask<?, R> task;
    private final boolean single;
    private String lastTaskRule;
    private List<String> recipeIds;
    private int repeatTimes = 0;
    private List<Pair<List<Integer>, List<List<ItemStack>>>> recipesIngredients = new ArrayList<>();
    private ICookInventory lastInv;
    private int tryTime = 0;

    public MaidRecipesManager(EntityMaid maid, ICookTask<?, R> task, boolean single) {
        this(maid, task, single, true);
    }

    public MaidRecipesManager(EntityMaid maid, ICookTask<?, R> task, boolean single, boolean createRecIng) {
        this.maid = maid;
        this.maidInventory = new MaidInventory(maid);
        this.single = single;
        this.task = task;

        this.initLastInv(maid);

        if (createRecIng) {
            this.initTaskData(maid);
            this.createRecipesIngredients(maid);
        }
    }

    public static ItemStack findCulinaryHub(EntityMaid maid) {
        ItemStack culinaryHubItem = maid.getMaidInv().getStackInSlot(4);
        if (culinaryHubItem.is(InitItems.CULINARY_HUB.get())) return culinaryHubItem;
        return ItemStack.EMPTY;
    }

    private static void tranCookBag2Chest(EntityMaid maid, BagType bagType, boolean requireHasItem) {
        ItemStack culinaryHub = findCulinaryHub(maid);
        if (!culinaryHub.isEmpty()) return;

        List<BlockPos> ingredientPos = ItemCulinaryHub.getBindModePoses(culinaryHub, bagType.name);
        if (ingredientPos.isEmpty()) return;

        Map<BagType, ItemStackHandler> containers = ItemCulinaryHub.getContainers(culinaryHub);
        if (!containers.containsKey(bagType)) return;
        ItemStackHandler itemStackHandler = containers.get(bagType);

        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            ItemStack stack = itemStackHandler.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            for (BlockPos ingredientPo : ingredientPos) {
                BlockEntity blockEntity = maid.level.getBlockEntity(ingredientPo);
                if (stack.isEmpty()) break;

                if (!SophistorageCompat.insert(stack, blockEntity, requireHasItem) && blockEntity != null) {
                    for (IChestType type : ChestManager.getAllChestTypes()) {
                        if (!type.isChest(blockEntity)) continue;
                        if (type.getOpenCount(maid.level, ingredientPo, blockEntity) > 0) continue;

                        blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(beInv -> {
                            ItemStack leftStack = ItemHandlerHelper.insertItemStacked(beInv, stack.copy(), false);
                            stack.shrink(stack.getCount() - leftStack.getCount());
                        });
                        makeChanged(blockEntity);
                        break;
                    }
                }
            }
        }
        ItemCulinaryHub.setContainer(culinaryHub, containers);
    }

    public static void makeChanged(BlockEntity tile) {
        tile.setChanged();
        Level world = tile.getLevel();
        if (world != null) {
            world.sendBlockUpdated(tile.getBlockPos(), tile.getBlockState(), tile.getBlockState(), 3);
        }
    }

    private static boolean hasAdditionStackFromHub(ItemStack findItem, List<BlockPos> bindModePoses, Level level, ItemStackHandler availableInv) {
        for (BlockPos bindModePose : bindModePoses) {
            BlockEntity blockEntity = level.getBlockEntity(bindModePose);

            if (blockEntity != null) {
                LazyOptional<IItemHandler> capability = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null);

                if (capability.isPresent()) {
                    IItemHandler beInv = capability.resolve().get();
                    return ItemsUtil.findStackSlot(beInv, stack -> stack.is(findItem.getItem())) > -1;
                }

            }

        }
        return false;
    }

    @Nullable
    private static ItemStack getAdditionStackFromHub(ItemStack findItem, List<BlockPos> bindModePoses, Level level, ItemStackHandler availableInv) {
        for (BlockPos bindModePose : bindModePoses) {
            BlockEntity blockEntity = level.getBlockEntity(bindModePose);

            if (blockEntity != null) {
                LazyOptional<IItemHandler> capability = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null);

                if (capability.isPresent()) {
                    IItemHandler beInv = capability.resolve().get();

                    int stackSlot = ItemsUtil.findStackSlot(beInv, stack -> stack.is(findItem.getItem()));

                    if (stackSlot > -1) {
                        return beInv.extractItem(stackSlot, 64, false).copy();
                    }

                }

            }

        }
        return null;
    }

    public boolean isSingle() {
        return single;
    }

    public MaidInventory getMaidInventory() {
        return maidInventory;
    }

    private List<R> getRecs(EntityMaid maid) {
        List<R> list = new ArrayList<>(this.rec);
        shuffle(list);
        return list;
    }

    public List<Pair<List<Integer>, List<List<ItemStack>>>> getRecipesIngredients() {
        return recipesIngredients;
    }

    public Pair<List<Integer>, List<List<ItemStack>>> getRecipeIngredient() {
//        FarmsoulKitchen.LOGGER.info("MaidRecipesManager.getRecipeIngredient: ");
//        FarmsoulKitchen.LOGGER.info(recipesIngredients);
        if (recipesIngredients.isEmpty()) return null;
        int size = recipesIngredients.size();
        Pair<List<Integer>, List<List<ItemStack>>> integerListPair = recipesIngredients.get(0);
        List<Pair<List<Integer>, List<List<ItemStack>>>> pairs = recipesIngredients.subList(1, size);
        recipesIngredients = pairs;
        return integerListPair;
    }

    public void checkAndCreateRecipesIngredients(EntityMaid maid) {
        this.initTaskData(maid);
        // 缓存的配方原料没了
        if (!recipesIngredients.isEmpty()) return;
        // 是否为上一次的背包以及手上的物品
        boolean lastInv = this.isLastInv(maid);
        if (lastInv && tryTime++ < 10) return;
        tryTime = 0;
        this.createRecipesIngredients(maid);
    }

    @SuppressWarnings("unchecked")
    private void initTaskData(EntityMaid maid) {
        if (lastTaskRule == null || recipeIds == null) {
            ICookTask<?, R> cookTask = (ICookTask<?, R>) maid.getTask();
            CookData cookData = cookTask.getTaskData(maid);
            this.lastTaskRule = cookData.mode();
            this.recipeIds = cookData.recs();
            this.rec.clear();

            List<R> allRecipesFor = getValidRecipesFor(maid.level);
            this.rec.addAll(allRecipesFor);
        }
    }

    private List<R> getValidRecipesFor(Level level) {
        List<R> allRecipesFor;
        if (CookData.Mode.SELECT.name.equals(this.lastTaskRule)) {
            allRecipesFor = task.getRecipes(level).stream().filter(r -> recipeIds.contains(r.getId().toString())).toList();
        } else {
            allRecipesFor = task.getRecipes(level);
        }
        return allRecipesFor;
    }

    private boolean isLastInv(EntityMaid maid) {
        CombinedInvWrapper availableInv = maid.getAvailableInv(true);
        ICookInventory lastInv1 = this.getLastInv();

        List<ItemStack> lastInvStack = lastInv1.getLastInvStack();

        ItemStack culinaryHub = findCulinaryHub(maid);
        if (!culinaryHub.isEmpty()) {
            ItemStackHandler orDefault = ItemCulinaryHub.getContainers(culinaryHub).getOrDefault(BagType.INGREDIENT, new ItemStackHandler(BagType.INGREDIENT.size * 9));
            if (orDefault.getSlots() != lastInvStack.size()) return false;

            for (int i = 0; i < orDefault.getSlots(); i++) {
                ItemStack stackInSlot = orDefault.getStackInSlot(i);
                ItemStack cacheStack = lastInvStack.get(i);
                if (!(stackInSlot.is(cacheStack.getItem()) && stackInSlot.getCount() == cacheStack.getCount())) {
                    return false;
                }
            }
        } else {
            if (availableInv.getSlots() != lastInvStack.size()) return false;

            for (int i = 0; i < availableInv.getSlots(); i++) {
                ItemStack stackInSlot = availableInv.getStackInSlot(i);
                ItemStack cacheStack = lastInvStack.get(i);
                if (!(stackInSlot.is(cacheStack.getItem()) && stackInSlot.getCount() == cacheStack.getCount())) {
                    return false;
                }
            }
        }

        return true;
    }

    public ItemStack getItemStack(Item item) {
        Map<Item, List<ItemStack>> inventoryStack = maidInventory.getInventoryStack();
        List<ItemStack> itemStacks = inventoryStack.get(item);
        for (ItemStack itemStack : itemStacks) {
            if (!itemStack.isEmpty()) {
                return itemStack;
            }
        }
        return ItemStack.EMPTY;
    }

    protected List<R> filterRecipes(List<R> recipes) {
        return recipes;
    }

    public void mapChestIngredient(EntityMaid maid) {
        ItemStack culinaryHub = findCulinaryHub(maid);
        if (culinaryHub.isEmpty()) return;

        List<BlockPos> ingredientPos = ItemCulinaryHub.getBindModePoses(culinaryHub, BagType.INGREDIENT.name);
        if (ingredientPos.isEmpty()) return;

        Map<BagType, ItemStackHandler> containers = ItemCulinaryHub.getContainers(culinaryHub);
        if (!containers.containsKey(BagType.INGREDIENT)) return;
        ItemStackHandler inventory = containers.get(BagType.INGREDIENT);

        ServerLevel level = (ServerLevel) maid.level;

        Map<Item, Integer> available = new HashMap<>();
        Map<Item, List<ItemStack>> ingredientAmount = new HashMap<>();

        Map<ItemStack, Pair<IItemHandler, Integer>> stackContentHandler = new HashMap<>();


        // 汇集所有箱子的原料
        for (BlockPos ingredientPo : ingredientPos) {
            BlockEntity blockEntity = level.getBlockEntity(ingredientPo);
            if (!SophistorageCompat.storageItemData(blockEntity, stackContentHandler, available, ingredientAmount) && blockEntity != null) {
                for (IChestType type : ChestManager.getAllChestTypes()) {
                    if (!type.isChest(blockEntity) || type.getOpenCount(maid.level, ingredientPo, blockEntity) > 0)
                        continue;
                    blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(beInv -> {
                        for (int i = 0; i < beInv.getSlots(); i++) {
                            ItemStack stackInSlot = beInv.getStackInSlot(i);
                            Item item = stackInSlot.getItem();

                            if (stackInSlot.isEmpty()) continue;

                            stackContentHandler.put(stackInSlot, Pair.of(beInv, i));

                            available.merge(item, stackInSlot.getCount(), Integer::sum);

                            List<ItemStack> itemStacks = ingredientAmount.get(item);
                            if (itemStacks == null) {
                                ingredientAmount.put(item, Lists.newArrayList(stackInSlot));
                            } else {
                                itemStacks.add(stackInSlot);
                            }
                        }
                    });
                    break;
                }
            }
        }

        List<Pair<List<Integer>, List<Item>>> _make = this.createIngres(available, maid, false);
        if (_make.isEmpty()) return;

        // 转移箱子原料至CookBag
        for (Pair<List<Integer>, List<Item>> listListPair : _make) {
            List<Integer> first = listListPair.getFirst();
            List<Item> second = listListPair.getSecond();

            for (int i = 0; i < first.size(); i++) {
                Integer i1 = first.get(i);
                Item item = second.get(i);

                List<ItemStack> itemStacks = ingredientAmount.get(item);
                for (ItemStack itemStack : itemStacks) {
                    if (itemStack.isEmpty()) continue;
                    int count = itemStack.getCount();
                    // 减去当前物品的数量还是大于0，证明还没满足，就整体移动

                    Pair<IItemHandler, Integer> iTrackedContentsItemHandlerIntegerPair = stackContentHandler.get(itemStack);

                    IItemHandler first1 = iTrackedContentsItemHandlerIntegerPair.getFirst();
                    Integer second1 = iTrackedContentsItemHandlerIntegerPair.getSecond();

                    if (i1 - count > 0) {
                        ItemStack copy = itemStack.copy();

                        ItemStack itemStack1 = ItemHandlerHelper.insertItemStacked(inventory, copy, false);
                        first1.extractItem(second1, itemStack.getCount() - itemStack1.getCount(), false);
                    } else {
                        ItemStack copy = itemStack.copyWithCount(i1);

                        ItemStack itemStack1 = ItemHandlerHelper.insertItemStacked(inventory, copy, false);
                        first1.extractItem(second1, i1 - itemStack1.getCount(), false);
                        break;
                    }
                    i1 -= count;
                }
            }
        }
        // 更新所有箱子的状态
        for (BlockPos ingredientPo : ingredientPos) {
            BlockEntity blockEntity = level.getBlockEntity(ingredientPo);
            if (blockEntity != null) {
                makeChanged(blockEntity);
            }
        }
        // 更新CookBag的inventory
        ItemCulinaryHub.setContainer(culinaryHub, containers);
    }

    private void createRecipesIngredients(EntityMaid maid) {
        if (lastTaskRule == null || recipeIds == null) {
            this.initTaskData(maid);
        }

        this.currentRecs.clear();
        this.currentRecs.addAll(getRecs(maid));
        // 将CookBag里无用的配方原料放回原料箱子
        this.tranUnIngre2Chest(maid);
        // 获取原料箱子配方原料并置入CookBag
        this.mapChestIngredient(maid);
        this.maidInventory.refreshInv();
        this.createIngres(maid, true);
        this.currentRecs.clear();

    }

    public void tranOutput2Chest(EntityMaid maid) {
        tranCookBag2Chest(maid, BagType.OUTPUT, false);
    }

    public void tranUnIngre2Chest(EntityMaid maid) {
        tranCookBag2Chest(maid, BagType.INGREDIENT, true);
    }

    private void createIngres(EntityMaid maid, boolean setRecipeIngres) {
        Map<Item, Integer> maidAvailableInv = this.getMaidAvailableInv(maid);
        this.createIngres(maidAvailableInv, maid, setRecipeIngres);
    }

    protected List<Pair<List<Integer>, List<Item>>> createIngres(Map<Item, Integer> available, EntityMaid maid, boolean setRecipeIngres) {
        List<Pair<List<Integer>, List<Item>>> _make = getRecIngreMake(available);

        if (setRecipeIngres) {
            setRecIngres(maid, _make, available);
        }

        return _make;
    }

    @NotNull
    private List<Pair<List<Integer>, List<Item>>> getRecIngreMake(Map<Item, Integer> available) {
        List<Pair<List<Integer>, List<Item>>> _make = new ArrayList<>();
        List<Item> invIngredient = new ArrayList<>();
        Map<Item, Integer> itemTimes = new HashMap<>();
        for (R r : this.currentRecs) {
            invIngredient.clear();
            itemTimes.clear();
            Pair<List<Integer>, List<Item>> maxCount = this.getAmountIngredient(invIngredient, itemTimes, r, available);
            if (!maxCount.getFirst().isEmpty()) {
                _make.add(Pair.of(maxCount.getFirst(), maxCount.getSecond()));
            }
        }
        repeat(_make, available, this.repeatTimes);
        return _make;
    }

    protected void setRecIngres(EntityMaid maid, List<Pair<List<Integer>, List<Item>>> _make, Map<Item, Integer> available) {
        if (_make.isEmpty()) return;
        this.recipesIngredients = transform(maid, _make, available);
    }

    @NotNull
    protected Map<Item, Integer> getMaidAvailableInv(EntityMaid maid) {
        return new HashMap<>(getIngredientInv(maid).getInventoryItem());
    }


    public ICookInventory getIngredientInv(EntityMaid maid) {
        this.initLastInv(maid);
        return this.getLastInv();
    }

    private void initLastInv(EntityMaid maid) {
        ItemStack culinaryHub = findCulinaryHub(maid);
        if (!culinaryHub.isEmpty()) {
            this.setLastInv(new CookBagInventory(culinaryHub));
        } else {
            this.setLastInv(this.maidInventory);
        }
    }

    public ICookInventory getLastInv() {
        return lastInv;
    }

    private void setLastInv(ICookInventory lastInv) {
        this.lastInv = lastInv;
    }

    protected void repeat(List<Pair<List<Integer>, List<Item>>> oriList, Map<Item, Integer> available, int times) {
        ArrayList<Pair<List<Integer>, List<Item>>> oriPairs = new ArrayList<>(oriList);
        for (int l = 0; l < times; l++) {
            for (Pair<List<Integer>, List<Item>> listListPair : oriPairs) {
                List<Integer> first = listListPair.getFirst();
                List<Item> second = listListPair.getSecond();

                boolean canRepeat = true;
                for (int i = 0; i < second.size(); i++) {
                    Integer availableCount = available.get(second.get(i));
                    if (availableCount < first.get(i)) {
                        canRepeat = false;
                        break;
                    }
                }

                if (canRepeat) {
                    for (int i = 0; i < second.size(); i++) {
                        Item item = second.get(i);
                        available.put(item, available.get(item) - first.get(i));
                    }
                    oriList.add(listListPair);
                }
            }
        }
    }

    protected List<Pair<List<Integer>, List<List<ItemStack>>>> transform(EntityMaid maid, List<Pair<List<Integer>, List<Item>>> oriList, Map<Item, Integer> available) {
        Map<Item, List<ItemStack>> inventoryStack = this.getIngredientInv(maid).getInventoryStack();
//        return oriList.stream().map(p -> Pair.of(p.getFirst(), p.getSecond().stream().map(inventoryStack::get).toList())).toList();

        List<Pair<List<Integer>, List<List<ItemStack>>>> list1 = oriList.stream().map(p -> {
            List<List<ItemStack>> list = p.getSecond().stream().map(item -> {
                return inventoryStack.get(item);
//                return inventoryStack.getOrDefault(item, new ArrayList<>());
            }).toList();
            return Pair.of(p.getFirst(), list);
        }).toList();
        return list1;
    }

    protected Pair<List<Integer>, List<Item>> getAmountIngredient(List<Item> invIngredient, Map<Item, Integer> itemTimes, R recipe, Map<Item, Integer> available) {
        List<Ingredient> ingredients = task.getIngredients(recipe);
        boolean[] canMake = {true};
        boolean[] single = {false};

        extraStartRecipe(recipe, available, canMake, single, itemTimes, invIngredient);

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

        extraEndRecipe(recipe, available, canMake, single, itemTimes, invIngredient);

//        if (!canMake[0] || invIngredient.stream().anyMatch(item -> available.get(item) <= 0)) {
        if (!canMake[0] || itemTimes.entrySet().stream().anyMatch(entry -> available.get(entry.getKey()) < entry.getValue())) {
            return Pair.of(Collections.emptyList(), Collections.emptyList());
        }

        int maxCount = 64;
        if (single[0] || this.single) {
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

        return Pair.of(countList, new ArrayList<>(invIngredient));
    }

    protected boolean extraStartRecipe(R recipe, Map<Item, Integer> available, boolean[] single, boolean[] canMake, Map<Item, Integer> itemTimes, List<Item> invIngredient) {
        return true;
    }

    protected boolean extraEndRecipe(R recipe, Map<Item, Integer> available, boolean[] single, boolean[] canMake, Map<Item, Integer> itemTimes, List<Item> invIngredient) {
        return true;
    }

    private void shuffle(List<R> recipes) {
        Collections.shuffle(recipes);
    }

    public int getRepeatTimes() {
        return repeatTimes;
    }

    public void setRepeatTimes(int repeatTimes) {
        this.repeatTimes = repeatTimes;
    }

    public IItemHandlerModifiable getOutputAdditionInv(EntityMaid maid) {
        return this.getBagContainerInv(maid, BagType.OUTPUT_ADDITION);
    }

    public boolean hasOutputAdditionItem(EntityMaid maid, ItemStack findItem) {
        ItemStack culinaryHub = findCulinaryHub(maid);
        if (!culinaryHub.isEmpty()) {
            IItemHandlerModifiable availableInv = this.getOutputAdditionInv(maid);
            int additionSlot = ItemsUtil.findStackSlot(availableInv, stack -> stack.is(findItem.getItem()));

            if (additionSlot > -1) {
                return true;
            } else {
                Level level = maid.level;
                List<BlockPos> bindModePoses = ItemCulinaryHub.getBindModePoses(culinaryHub, BagType.OUTPUT_ADDITION.name);
                return hasAdditionStackFromHub(findItem, bindModePoses, level, (ItemStackHandler) availableInv);
            }

        } else {
            return ItemsUtil.findStackSlot(maid.getAvailableInv(true), stack -> stack.is(findItem.getItem())) > -1;
        }
    }

    public ItemStack findOutputAdditionItem(EntityMaid maid, ItemStack findItem) {
        ItemStack culinaryHub = findCulinaryHub(maid);
        if (!culinaryHub.isEmpty()) {
            IItemHandlerModifiable availableInv = this.getOutputAdditionInv(maid);
            int additionSlot = ItemsUtil.findStackSlot(availableInv, stack -> stack.is(findItem.getItem()));

            if (additionSlot > -1) {
                ItemStack itemStack = availableInv.extractItem(additionSlot, 64, false);
                this.getLastInv().syncInv();
                return itemStack.copy();
            } else {
                Level level = maid.level;
                List<BlockPos> bindModePoses = ItemCulinaryHub.getBindModePoses(culinaryHub, BagType.OUTPUT_ADDITION.name);
                ItemStack beInv = getAdditionStackFromHub(findItem, bindModePoses, level, (ItemStackHandler) availableInv);
                if (beInv != null) return beInv;
            }

        } else {
            CombinedInvWrapper maidInv = maid.getAvailableInv(true);
            int additionSlot = ItemsUtil.findStackSlot(maidInv, stack -> stack.is(findItem.getItem()));

            if (additionSlot > -1) {
                ItemStack stackInSlot = maidInv.getStackInSlot(additionSlot);
                ItemStack copy = stackInSlot.copy();
                stackInSlot.setCount(0);
                return copy;

            }

        }

        return ItemStack.EMPTY;
    }

    public IItemHandlerModifiable getOutputInv(EntityMaid maid) {
        return this.getBagContainerInv(maid, BagType.OUTPUT);
    }

    public IItemHandlerModifiable getIngreInv(EntityMaid maid) {
        return this.getBagContainerInv(maid, BagType.INGREDIENT);
    }

    public IItemHandlerModifiable getBagContainerInv(EntityMaid maid, BagType bagType) {
        ItemStack culinaryHub = findCulinaryHub(maid);

        if (!culinaryHub.isEmpty()) return maid.getMaidInv();
        ;

        return this.getLastInv().getAvailableInv(maid, bagType);
    }
}
