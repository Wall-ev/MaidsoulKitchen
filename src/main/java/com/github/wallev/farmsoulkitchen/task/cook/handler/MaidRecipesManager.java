package com.github.wallev.farmsoulkitchen.task.cook.handler;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IChestType;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.chest.ChestManager;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.wallev.farmsoulkitchen.api.task.v1.cook.ICookTask;
import com.github.wallev.farmsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.farmsoulkitchen.init.InitItems;
import com.github.wallev.farmsoulkitchen.inventory.container.item.BagType;
import com.github.wallev.farmsoulkitchen.item.ItemCulinaryHub;
import com.github.wallev.farmsoulkitchen.task.cook.handler.compat.InventoryCompat;
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
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MaidRecipesManager<R extends Recipe<? extends Container>> {
    protected final List<R> rec = new ArrayList<>();
    protected final List<R> currentRecs = new ArrayList<>();
    private final EntityMaid maid;
    private final Level level;
    private final ICookTask<?, R> task;
    private final boolean single;
    private ICookInventory cookInv;
    private boolean hasCulinaryHub;
    private Map<BagType, List<BlockPos>> bindingPoses;
    private String lastTaskRule;
    private List<String> recipeIds;
    private int repeatTimes = 0;
    private List<Pair<List<Integer>, List<List<ItemStack>>>> recipesIngredients = new ArrayList<>();
    private int tryTime = 0;

    public MaidRecipesManager(EntityMaid maid, ICookTask<?, R> task, boolean single) {
        this(maid, task, single, true);
    }

    public MaidRecipesManager(EntityMaid maid, ICookTask<?, R> task, boolean single, boolean createRecIng) {
        this.maid = maid;
        this.level = maid.level;
        this.single = single;
        this.task = task;

        if (createRecIng) {
            this.createRecipesIngredients();
        }
    }

    public static void makeChanged(BlockEntity tile) {
        tile.setChanged();
        Level world = tile.getLevel();
        if (world != null) {
            world.sendBlockUpdated(tile.getBlockPos(), tile.getBlockState(), tile.getBlockState(), 3);
        }
    }

    private boolean initInvData() {
        if (this.cookInv == null || this.bindingPoses == null || (!this.hasCulinaryHub && !this.findCulinaryHub().isEmpty())) {
            this.hasCulinaryHub = !this.findCulinaryHub().isEmpty();
            this.bindingPoses = ItemCulinaryHub.getBindPoses(this.findCulinaryHub());
            //@todo
            this.cookInv = this.enableHub() ? this.initCookInv() : new MaidInventory(maid);

            return true;
        }
        return false;
    }

    private ICookInventory initCookInv() {
        ItemStack culinaryHub = this.findCulinaryHub();
        return culinaryHub.isEmpty() ? new MaidInventory(maid) : new CookBagInventory(culinaryHub);
    }

    public ItemStack findCulinaryHub() {
        ItemStack culinaryHubItem = this.maid.getMaidInv().getStackInSlot(4);
        if (culinaryHubItem.is(InitItems.CULINARY_HUB.get())) return culinaryHubItem;
        return ItemStack.EMPTY;
    }

    private void tranCookBag2Chest(BagType bagType, boolean requireHasItem) {
        if (!this.hasCulinaryHub) return;

        List<BlockPos> ingredientPos = getBindingTypePoses(bagType);
        if (ingredientPos.isEmpty()) return;

        IItemHandlerModifiable itemStackHandler = this.getBagContainerInv(bagType);

        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            ItemStack stack = itemStackHandler.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            for (BlockPos ingredientPo : ingredientPos) {
                if (isPosZone(ingredientPo)) continue;

                BlockEntity blockEntity = maid.level.getBlockEntity(ingredientPo);
                if (blockEntity == null) continue;
                if (stack.isEmpty()) break;

                // 原版
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
                // 精妙存储
                if (InventoryCompat.insertSopBe(stack, blockEntity, requireHasItem)) {
                    break;
                }
            }
        }
        this.getCookInv().syncInv();
    }

    private List<BlockPos> getBindingTypePoses(BagType bagType) {
        return this.bindingPoses.getOrDefault(bagType, Collections.emptyList());
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public boolean isSingle() {
        return single;
    }

    private List<R> getRecs() {
        List<R> list = new ArrayList<>(this.rec);
        shuffle(list);
        return list;
    }

    public List<Pair<List<Integer>, List<List<ItemStack>>>> getRecipesIngredients() {
        return recipesIngredients;
    }

    public Pair<List<Integer>, List<List<ItemStack>>> getRecipeIngredient() {
        if (recipesIngredients.isEmpty()) return Pair.of(Collections.emptyList(), Collections.emptyList());
        int size = recipesIngredients.size();
        Pair<List<Integer>, List<List<ItemStack>>> integerListPair = recipesIngredients.get(0);
        List<Pair<List<Integer>, List<List<ItemStack>>>> pairs = recipesIngredients.subList(1, size);
        recipesIngredients = pairs;
        return integerListPair;
    }

    public boolean checkAndCreateRecipesIngredients() {
        //预防隙间转移走烹饪中枢
        if (this.hasCulinaryHub && this.findCulinaryHub().isEmpty() && this.level instanceof ServerLevel serverLevel) {
            this.recipesIngredients = Collections.emptyList();
            this.maid.refreshBrain(serverLevel);
            return false;
        }
        this.init();
        // 缓存的配方原料没了
        if (!recipesIngredients.isEmpty()) return true;
        // 是否为上一次的背包以及手上的物品
        boolean lastInv = this.isLastCookInv();
        if (lastInv && tryTime++ < 10) return true;
        tryTime = 0;
        this.createRecipesIngredients();
        return true;
    }

    @SuppressWarnings("unchecked")
    private boolean initTaskData() {
        if (lastTaskRule == null || recipeIds == null) {
            ICookTask<?, R> cookTask = (ICookTask<?, R>) maid.getTask();
            CookData cookData = cookTask.getTaskData(maid);
            this.lastTaskRule = cookData.mode();
            this.recipeIds = cookData.getRecs();
            this.rec.clear();

            List<R> allRecipesFor = this.getValidRecipesFor();
            this.rec.addAll(allRecipesFor);

            return true;
        }

        return false;
    }

    private List<R> getValidRecipesFor() {
        List<R> allRecipesFor;
        if (this.lastTaskRule.equals(CookData.Mode.WHITELIST.name)) {
            allRecipesFor = task.getRecipes(level).stream().filter(r -> recipeIds.contains(r.getId().toString())).toList();
        } else {
            allRecipesFor = task.getRecipes(level).stream().filter(r -> !recipeIds.contains(r.getId().toString())).toList();
        }
        return allRecipesFor;
    }

    private boolean isLastCookInv() {
        CombinedInvWrapper availableInv = this.maid.getAvailableInv(true);

        List<ItemStack> lastInvStack = this.cookInv.getLastInvStack();

        ItemStack culinaryHub = this.findCulinaryHub();
        if (!culinaryHub.isEmpty()) {
            IItemHandlerModifiable availableInv1 = this.getCookInv().getAvailableInv(maid, BagType.INGREDIENT);
            if (availableInv1.getSlots() != lastInvStack.size()) return false;

            for (int i = 0; i < availableInv1.getSlots(); i++) {
                ItemStack stackInSlot = availableInv1.getStackInSlot(i);
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

    public void mapChestIngredient() {
        if (!hasCulinaryHub) return;

        List<BlockPos> ingredientPos = getBindingTypePoses(BagType.INGREDIENT);
        if (ingredientPos.isEmpty()) return;

        IItemHandlerModifiable inventory = this.getCookInv().getAvailableInv(maid, BagType.INGREDIENT);

        Map<Item, Integer> available = new HashMap<>();
        Map<Item, List<ItemStack>> ingredientAmount = new HashMap<>();

        Map<ItemStack, Pair<IItemHandler, Integer>> stackContentHandler = new HashMap<>();

        // 汇集所有箱子的原料
        for (BlockPos ingredientPo : ingredientPos) {
            if (isPosZone(ingredientPo)) continue;

            BlockEntity blockEntity = level.getBlockEntity(ingredientPo);
            if (blockEntity == null) continue;

            // 原版
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
            // 精妙存储
            if (InventoryCompat.sopStorageItemData(blockEntity, stackContentHandler, available, ingredientAmount)) {
                break;
            }
        }

        List<Pair<List<Integer>, List<Item>>> _make = this.createIngres(available, false);
        if (_make.isEmpty()) return;

        // 转移箱子原料至CookBag
        for (Pair<List<Integer>, List<Item>> listListPair : _make) {
            List<Integer> first = listListPair.getFirst();
            List<Item> second = listListPair.getSecond();

            for (int i = 0; i < first.size(); i++) {
                Integer i1 = first.get(i);
                Item item = second.get(i);
                if (i1 <= 0 || item == null) continue;

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
            if (isPosZone(ingredientPo)) continue;

            BlockEntity blockEntity = level.getBlockEntity(ingredientPo);
            if (blockEntity != null) {
                makeChanged(blockEntity);
            }
        }
        // 更新CookBag的inventory
        this.getCookInv().syncInv();
    }

    private boolean isPosZone(BlockPos ingredientPo) {
        float maxDistance = maid.getRestrictRadius();
        if (maid.distanceToSqr(ingredientPo.getX(), ingredientPo.getY(), ingredientPo.getZ()) > (maxDistance * maxDistance)) {
            return true;
        }
        return false;
    }

    private void init() {
        boolean initTaskData = this.initTaskData();
        boolean initInvData = this.initInvData();
        if (initTaskData || initInvData) {
            this.recipesIngredients = Collections.emptyList();
        }
    }

    private void createRecipesIngredients() {
        this.init();

        this.currentRecs.clear();
        this.currentRecs.addAll(this.getRecs());
        // 将CookBag里无用的配方原料放回原料箱子
        this.tranUnIngre2Chest();
        // 获取原料箱子配方原料并置入CookBag
        this.mapChestIngredient();
        this.cookInv.refreshInv();
        this.createIngres(true);
        this.currentRecs.clear();

    }

    public void tranOutput2Chest() {
        this.tranCookBag2Chest(BagType.OUTPUT, false);
    }

    public void tranUnIngre2Chest() {
        this.tranCookBag2Chest(BagType.INGREDIENT, true);
    }

    private void createIngres(boolean setRecipeIngres) {
        Map<Item, Integer> maidAvailableInv = this.getMaidAvailableInv();
        this.createIngres(maidAvailableInv, setRecipeIngres);
    }

    protected List<Pair<List<Integer>, List<Item>>> createIngres(Map<Item, Integer> available, boolean setRecipeIngres) {
        List<Pair<List<Integer>, List<Item>>> _make = getRecIngreMake(available);

        if (setRecipeIngres) {
            setRecIngres(_make, available);
        }

        return _make;
    }

    @NotNull
    private List<Pair<List<Integer>, List<Item>>> getRecIngreMake(Map<Item, Integer> available) {
        List<Pair<List<Integer>, List<Item>>> _make = new ArrayList<>();
        for (R r : this.currentRecs) {
            Pair<List<Integer>, List<Item>> maxCount = this.getAmountIngredient(r, available);
            if (!maxCount.getFirst().isEmpty()) {
                _make.add(Pair.of(maxCount.getFirst(), maxCount.getSecond()));
            }
        }
        repeat(_make, available, this.repeatTimes);
        return _make;
    }

    protected void setRecIngres(List<Pair<List<Integer>, List<Item>>> _make, Map<Item, Integer> available) {
        if (_make.isEmpty()) return;
        this.recipesIngredients = transform(_make, available);
    }

    @NotNull
    protected Map<Item, Integer> getMaidAvailableInv() {
        return new HashMap<>(getCookInv().getInventoryItem());
    }

    public ICookInventory getCookInv() {
        return this.cookInv;
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

    protected List<Pair<List<Integer>, List<List<ItemStack>>>> transform(List<Pair<List<Integer>, List<Item>>> oriList, Map<Item, Integer> available) {
        Map<Item, List<ItemStack>> inventoryStack = this.getCookInv().getInventoryStack();
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

    protected Pair<List<Integer>, List<Item>> getAmountIngredient(R recipe, Map<Item, Integer> available) {
        List<Ingredient> ingredients = task.getIngredients(recipe);
        List<Item> invIngredient = new ArrayList<>();
        Map<Item, Integer> itemTimes = new HashMap<>();
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

        return Pair.of(countList, invIngredient);
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

    public void shrinkOutputAdditionItem(ItemStack findItem, int count) {
        if (hasCulinaryHub) {
            IItemHandlerModifiable availableInv = this.getOutputAdditionInv();
            int additionSlot = ItemsUtil.findStackSlot(availableInv, stack -> stack.is(findItem.getItem()));

            if (additionSlot > -1) {
                availableInv.extractItem(additionSlot, count, false);
                this.cookInv.syncInv();
            } else {
                List<BlockPos> bindModePoses = getBindingTypePoses(BagType.OUTPUT_ADDITION);
                this.shrinkAdditionStackFromHub(findItem, bindModePoses, this.level, count);
            }

        } else {
            CombinedInvWrapper maidInv = this.maid.getAvailableInv(true);
            int additionSlot = ItemsUtil.findStackSlot(maidInv, stack -> stack.is(findItem.getItem()));

            if (additionSlot > -1) {
                maidInv.extractItem(additionSlot, count, false);
            }

        }
    }

    public int getOutputAdditionItemCount(ItemStack findItem) {
        if (hasCulinaryHub) {
            IItemHandlerModifiable availableInv = this.getOutputAdditionInv();
            int additionSlot = ItemsUtil.findStackSlot(availableInv, stack -> stack.is(findItem.getItem()));

            if (additionSlot > -1) {
                return availableInv.getStackInSlot(additionSlot).getCount();
            } else {
                List<BlockPos> bindModePoses = getBindingTypePoses(BagType.OUTPUT_ADDITION);
                return getAdditionStackFromHubCount(findItem, bindModePoses, this.level);
            }

        } else {
            CombinedInvWrapper maidInv = this.maid.getAvailableInv(true);
            int additionSlot = ItemsUtil.findStackSlot(maidInv, stack -> stack.is(findItem.getItem()));

            if (additionSlot > -1) {
                return maidInv.getStackInSlot(additionSlot).getCount();
            }

        }

        return 0;
    }

    public boolean hasOutputAdditionItem(ItemStack findItem) {
        if (this.hasCulinaryHub) {
            IItemHandlerModifiable availableInv = this.getOutputAdditionInv();
            int additionSlot = ItemsUtil.findStackSlot(availableInv, stack -> stack.is(findItem.getItem()));

            if (additionSlot > -1) {
                return true;
            } else {
                List<BlockPos> bindModePoses = getBindingTypePoses(BagType.OUTPUT_ADDITION);
                return hasAdditionStackFromHub(findItem, bindModePoses, level);
            }
        } else {
            return ItemsUtil.findStackSlot(maid.getAvailableInv(true), stack -> stack.is(findItem.getItem())) > -1;
        }
    }

    public ItemStack findOutputAdditionItem(ItemStack findItem) {
        if (hasCulinaryHub) {
            IItemHandlerModifiable availableInv = this.getOutputAdditionInv();
            int additionSlot = ItemsUtil.findStackSlot(availableInv, stack -> stack.is(findItem.getItem()));

            if (additionSlot > -1) {
                ItemStack itemStack = availableInv.extractItem(additionSlot, 64, false);
                this.cookInv.syncInv();
                return itemStack.copy();
            } else {
                List<BlockPos> bindModePoses = getBindingTypePoses(BagType.OUTPUT_ADDITION);
                return getAdditionStackFromHub(findItem, bindModePoses, this.level);
            }

        } else {
            CombinedInvWrapper maidInv = this.maid.getAvailableInv(true);
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

    private boolean hasAdditionStackFromHub(ItemStack findItem, List<BlockPos> bindModePoses, Level level) {
        for (BlockPos bindModePose : bindModePoses) {
            if (isPosZone(bindModePose)) continue;

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

    private int getAdditionStackFromHubCount(ItemStack findItem, List<BlockPos> bindModePoses, Level level) {
        for (BlockPos bindModePose : bindModePoses) {
            if (isPosZone(bindModePose)) continue;

            BlockEntity blockEntity = level.getBlockEntity(bindModePose);

            if (blockEntity != null) {
                LazyOptional<IItemHandler> capability = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null);

                if (capability.isPresent()) {
                    IItemHandler beInv = capability.resolve().get();

                    int stackSlot = ItemsUtil.findStackSlot(beInv, stack -> stack.is(findItem.getItem()));

                    if (stackSlot > -1) {
                        return beInv.getStackInSlot(stackSlot).getCount();
                    }

                }

            }

        }
        return 0;
    }

    private void shrinkAdditionStackFromHub(ItemStack findItem, List<BlockPos> bindModePoses, Level level, int count) {
        for (BlockPos bindModePose : bindModePoses) {
            if (isPosZone(bindModePose)) continue;

            BlockEntity blockEntity = level.getBlockEntity(bindModePose);

            if (blockEntity != null) {
                LazyOptional<IItemHandler> capability = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null);

                if (capability.isPresent()) {
                    IItemHandler beInv = capability.resolve().get();

                    int stackSlot = ItemsUtil.findStackSlot(beInv, stack -> stack.is(findItem.getItem()));

                    if (stackSlot > -1) {
                        beInv.extractItem(stackSlot, count, false).copy();
                        return ;
                    }

                }

            }

        }
    }


    private ItemStack getAdditionStackFromHub(ItemStack findItem, List<BlockPos> bindModePoses, Level level) {
        for (BlockPos bindModePose : bindModePoses) {
            if (isPosZone(bindModePose)) continue;

            BlockEntity blockEntity = level.getBlockEntity(bindModePose);

            if (blockEntity != null) {
                LazyOptional<IItemHandler> capability = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null);

                if (capability.isPresent()) {
                    IItemHandler beInv = capability.resolve().get();

                    int stackSlot = ItemsUtil.findStackSlot(beInv, stack -> stack.is(findItem.getItem()));

                    if (stackSlot > -1) {
                        ItemStack copy = beInv.extractItem(stackSlot, 64, false).copy();
                        blockEntity.setChanged();
                        return copy;
                    }

                }

            }

        }
        return ItemStack.EMPTY;
    }

    public IItemHandlerModifiable getOutputInv() {
        return this.getBagContainerInv(BagType.OUTPUT);
    }

    public IItemHandlerModifiable getOutputAdditionInv() {
        return this.getBagContainerInv(BagType.OUTPUT_ADDITION);
    }

    public IItemHandlerModifiable getInputInv() {
        return this.getBagContainerInv(BagType.INGREDIENT);
    }

    private IItemHandlerModifiable getBagContainerInv(BagType bagType) {
        return this.getCookInv().getAvailableInv(maid, bagType);
    }


    @Nullable
    public IItemHandlerModifiable getIngredientInv() {
        return this.getInputInv();
    }

    //不与烹饪中枢交互
    protected boolean enableHub() {
        return true;
    }
}
