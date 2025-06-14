package com.github.wallev.maidsoulkitchen.task.cook.common.inv;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IChestType;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.chest.ChestManager;
import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.inventory.container.item.BagType;
import com.github.wallev.maidsoulkitchen.item.ItemCulinaryHub;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidRec;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.mkrec.MKRecipe;
import com.github.wallev.maidsoulkitchen.util.InvUtil;
import com.github.wallev.maidsoulkitchen.util.ItemStackUtil;
import com.github.wallev.maidsoulkitchen.util.TileUtil;
import com.github.wallev.verhelper.server.item.VItemStack;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.*;
import java.util.function.Predicate;

public class MaidRecipesManager2<R extends Recipe<? extends Container>> {
    protected final EntityMaid maid;
    protected final ServerLevel level;
    protected final ICookTask<?, R> task;
    protected final RecSerializerManager<R> recSerializerManager;
    protected final CookBeBase<?> cookBeBase;
    protected CookData cookData;

    protected List<MKRecipe<R>> rec = new ArrayList<>();
    protected List<MKRecipe<R>> currentRecs = new ArrayList<>();
    protected ICookInventory cookInv;
    protected boolean hasCulinaryHub;
    protected Map<BagType, List<BlockPos>> bindingPoses;
    protected int tryTime = 0;

    protected LinkedList<MaidRec> maidRecs = new LinkedList<>();
    protected Map<Item, LinkedList<ItemStack>> invIngredients = new HashMap<>();

    public MaidRecipesManager2(RecSerializerManager<R> recSerializerManager, EntityMaid maid, ICookTask<?, R> task, CookBeBase<?> cookBeBase) {
        this.recSerializerManager = recSerializerManager;
        this.maid = maid;
        this.level = (ServerLevel) maid.level;
        this.task = task;
        this.cookBeBase = cookBeBase;
    }

    public RecSerializerManager<R> getRecSerializerManager() {
        return recSerializerManager;
    }

    public boolean hasMaidRecs(CookBeBase<?> cookBeBase) {
        return !this.maidRecs.isEmpty();
    }

    public MaidRec pollMaidRec(CookBeBase<?> cookBeBase) {
        return this.maidRecs.poll();
    }

    public Map<Item, LinkedList<ItemStack>> getInvIngredients() {
        return invIngredients;
    }

    public void updateInvIngredients() {
        for (LinkedList<ItemStack> value : this.invIngredients.values()) {
            for (ItemStack itemStack : value) {
                if (itemStack != null) {
                    break;
                } else {
                    value.remove();
                }
            }
        }
    }

    private boolean initInvData() {
        if (this.cookInv == null || this.bindingPoses == null || (!this.hasCulinaryHub && !this.findCulinaryHub().isEmpty())) {
            this.hasCulinaryHub = !this.findCulinaryHub().isEmpty();
            this.bindingPoses = ItemCulinaryHub.getBindPoses(this.findCulinaryHub());
            //@todo
            this.cookInv = this.enableHub() ? this.initCookInv() : new MaidInventory(maid, false);

            return true;
        }
        return false;
    }

    private ICookInventory initCookInv() {
        ItemStack culinaryHub = this.findCulinaryHub();
        return culinaryHub.isEmpty() ? new MaidInventory(maid, false) : new CookBagInventory(maid, culinaryHub);
    }

    public ItemStack findCulinaryHub() {
        return ItemCulinaryHub.getItem(maid);
    }

    private void itemCookBag2Chest(BagType bagType, boolean requireHasItem) {
        if (!this.canHub()) {
            return;
        }

        List<BlockPos> bindPos = getBindingTypePoses(bagType);
        if (bindPos.isEmpty()) {
            return;
        }

        IItemHandlerModifiable hubHandler = this.getBagContainerInv(bagType);
        List<ItemStack> hubStacks = new ArrayList<>();
        for (int i = 0; i < hubHandler.getSlots(); i++) {
            ItemStack stack = hubHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                hubStacks.add(stack);
            }
        }
        if (hubStacks.isEmpty()) {
            return;
        }

        List<IItemHandler> chestHandlers = new ArrayList<>();
        List<BlockPos> chestPos = new ArrayList<>();
        collectChest(bindPos, chestHandlers, chestPos);

        for (ItemStack hubStack : hubStacks) {
            for (IItemHandler chestHandler : chestHandlers) {
                ItemStackUtil.item2Inv(hubStack, chestHandler);
                if (hubStack.isEmpty()) {
                    break;
                }
            }
        }

        for (BlockPos chestPo : chestPos) {
            TileUtil.makeChanged(chestPo, level);
        }
        this.syncInv();
    }

    protected void collectChest(List<BlockPos> bindPos, List<IItemHandler> chestHandlers, List<BlockPos> chestPos) {
        for (BlockPos pos : bindPos) {
            if (isExtraZone(pos)) {
                continue;
            }

            BlockEntity blockEntity = maid.level.getBlockEntity(pos);
            if (blockEntity == null) {
                continue;
            }

            // 原版
            for (IChestType type : ChestManager.getAllChestTypes()) {
                if (!type.isChest(blockEntity)) {
                    continue;
                }
                if (type.getOpenCount(maid.level, pos, blockEntity) > 0) {
                    continue;
                }

                LazyOptional<IItemHandler> capability = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null);
                if (!capability.isPresent()) {
                    continue;
                }
                Optional<IItemHandler> resolve = capability.resolve();
                if (resolve.isPresent()) {
                    chestHandlers.add(resolve.get());
                    chestPos.add(pos);
                    break;
                }
            }
        }
    }

    private List<BlockPos> getBindingTypePoses(BagType bagType) {
        return this.bindingPoses.getOrDefault(bagType, Collections.emptyList());
    }

    public EntityMaid getMaid() {
        return maid;
    }

    protected List<MKRecipe<R>> getRecs() {
        List<MKRecipe<R>> list = this.getFilterRecipes(this.rec);
        return shuffle(list);
    }

    protected List<MKRecipe<R>> getFilterRecipes(List<MKRecipe<R>> rec) {
        return new ArrayList<>(rec);
    }

    public boolean checkAndCreateRecipesIngredients() {
        //预防隙间转移走烹饪中枢
        if (this.hasCulinaryHub && this.findCulinaryHub().isEmpty()) {
            this.maidRecs = new LinkedList<>();
            this.maid.refreshBrain(level);
            return false;
        }
        this.init();
        // 缓存的配方原料没了
        if (!maidRecs.isEmpty()) return true;
        // 是否为上一次的背包以及手上的物品
        boolean lastInv = this.isLastCookInv();
        if (lastInv && tryTime++ < 10) return true;
        tryTime = 0;
        this.createRecipesIngredients();
        return true;
    }

    private boolean initTaskData() {
        if (cookData == null) {
            this.cookData = task.getTaskData(maid);
            this.rec = this.getValidRecipesFor();
            return true;
        }
        return false;
    }

    private List<MKRecipe<R>> getValidRecipesFor() {
        return task.getRecipes(level).stream()
                .filter(r -> cookData.canCook(r.idStr()))
                .toList();
    }

    private boolean isLastCookInv() {
        List<ItemStack> lastInvStack = this.cookInv.getLastInvStack();

        if (canHub()) {
            IItemHandlerModifiable availableInv1 = this.getCookInv().getAvailableInv(BagType.INGREDIENT);
            if (availableInv1.getSlots() != lastInvStack.size()) return false;

            for (int i = 0; i < availableInv1.getSlots(); i++) {
                ItemStack stackInSlot = availableInv1.getStackInSlot(i);
                ItemStack cacheStack = lastInvStack.get(i);
                if (!(stackInSlot.is(cacheStack.getItem()) && stackInSlot.getCount() == cacheStack.getCount())) {
                    return false;
                }
            }
        } else {
            CombinedInvWrapper availableInv = this.maid.getAvailableInv(true);
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

    protected void mapChestIngredient() {
        if (!this.canHub()) return;

        List<BlockPos> ingredientPos = getBindingTypePoses(BagType.INGREDIENT);
        if (ingredientPos.isEmpty()) return;

        IItemHandlerModifiable inventory = this.getCookInv().getAvailableInv(BagType.INGREDIENT);

        Map<Item, Integer> available = new HashMap<>();
        Map<Item, List<ItemStack>> ingredientAmount = new HashMap<>();

        Map<ItemStack, Pair<IItemHandler, Integer>> stackContentHandler = new HashMap<>();

        // 汇集所有箱子的原料
        for (BlockPos ingredientPo : ingredientPos) {
            if (isExtraZone(ingredientPo)) {
                continue;
            }

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
        }

//        List<Pair<List<Integer>, List<Item>>> _make = this.createIngres(available);
        List<Pair<List<Integer>, List<Item>>> _make = new ArrayList<>();
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
                        ItemStack copy = VItemStack.copyWithCount(itemStack, i1);

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
            if (isExtraZone(ingredientPo)) {
                continue;
            }

            BlockEntity blockEntity = level.getBlockEntity(ingredientPo);
            if (blockEntity != null) {
                TileUtil.makeChanged(blockEntity);
            }
        }
        // 更新CookBag的inventory
        this.syncInv();
    }

    private boolean isExtraZone(BlockPos ingredientPo) {
        float maxDistance = maid.getRestrictRadius() * 2;
        if (maid.distanceToSqr(ingredientPo.getX(), ingredientPo.getY(), ingredientPo.getZ()) > (maxDistance * maxDistance)) {
            return true;
        }
        return false;
    }

    private void init() {
        boolean initTaskData = this.initTaskData();
        boolean initInvData = this.initInvData();
        if (initTaskData || initInvData) {
            this.maidRecs = new LinkedList<>();
        }
    }

    private void createRecipesIngredients() {
        this.init();


        // 将CookBag里无用的配方原料放回原料箱子
        this.itemUnIngre2Chest();
        // 获取原料箱子配方原料并置入CookBag
        this.mapChestIngredient();
        this.cookInv.refreshInv();

        this.currentRecs = this.getRecs();
        this.createIngres();
        this.currentRecs = Collections.emptyList();
    }

    public void itemOutput2Chest() {
        this.itemCookBag2Chest(BagType.OUTPUT, false);
    }

    public void itemUnIngre2Chest() {
        this.itemCookBag2Chest(BagType.INGREDIENT, true);
    }

    protected void createIngres() {
        this.invIngredients = this.getCookInv().getInventoryStackQueue();

        Map<ItemDefinition, Long> available = new HashMap<>(this.cookInv.itemInventory.getStacks());
        this.maidRecs = recSerializerManager.createMaidRecs(this.currentRecs, available, this::recAdd, this::recIsValid);
    }

    public void clear() {
    }

    protected void recAdd(MKRecipe<R> r, int index) {
    }

    protected boolean recIsValid(MKRecipe<R> r) {
        return true;
    }

    protected Map<Item, Integer> getMaidAvailableInv() {
        return new HashMap<>(getCookInv().getInventoryItem());
    }

    protected ICookInventory getCookInv() {
        return this.cookInv;
    }

    protected List<MKRecipe<R>> shuffle(List<MKRecipe<R>> recipes) {
        Collections.shuffle(recipes);
        return recipes;
    }


    public ItemStack getItemFromOutputAddition(Predicate<ItemStack> predicate) {
        return this.getItem(BagType.OUTPUT_ADDITION, predicate);
    }

    public boolean hasItemFromOutputAddition(Predicate<ItemStack> predicate) {
        return this.hasItem(BagType.OUTPUT_ADDITION, predicate);
    }

    public ItemStack getItemFromOutputAddition(ItemStack itemStack) {
        return this.getItem(BagType.OUTPUT_ADDITION, itemStack);
    }

    public boolean hasItemFromOutputAddition(ItemStack itemStack) {
        return this.hasItem(BagType.OUTPUT_ADDITION, itemStack);
    }

    public ItemStack getItem(BagType bagType, Predicate<ItemStack> predicate) {
        if (this.canHub()) {
            IItemHandlerModifiable inv = this.getCookInv().getAvailableInv(bagType);
            ItemStack itemStack = InvUtil.getStack(inv, predicate);
            if (!itemStack.isEmpty()) {
                return itemStack;
            } else {
                return this.getStackFromChest(bagType, predicate);
            }
        } else {
            return InvUtil.getStack(maid.getAvailableInv(true), predicate);
        }
    }

    public ItemStack getItem(BagType bagType, ItemStack stack) {
        return this.getItem(bagType, stack.getItem());
    }

    public ItemStack getItem(BagType bagType, Item item) {
        return this.getItem(bagType, itemStack -> itemStack.is(item));
    }

    public boolean hasItem(BagType bagType, Predicate<ItemStack> predicate) {
        return !this.getItem(bagType, predicate).isEmpty();
    }

    public boolean hasItem(BagType bagType, ItemStack stack) {
        return !this.getItem(bagType, stack).isEmpty();
    }

    public boolean hasItem(BagType bagType, Item item) {
        return !this.getItem(bagType, item).isEmpty();
    }

    protected ItemStack getStackFromChest(BagType bagType, Predicate<ItemStack> predicate) {
        List<BlockPos> bindModePoses = getBindingTypePoses(bagType);
        for (BlockPos bindModePose : bindModePoses) {
            if (isExtraZone(bindModePose)) {
                continue;
            }

            BlockEntity blockEntity = level.getBlockEntity(bindModePose);
            if (blockEntity == null) {
                continue;
            }

            LazyOptional<IItemHandler> capability = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null);
            if (!capability.isPresent()) {
                continue;
            }

            Optional<IItemHandler> resolve = capability.resolve();
            if (resolve.isEmpty()) {
                continue;
            }

            ItemStack itemStack1 = InvUtil.getStack(resolve.get(), predicate);
            if (!itemStack1.isEmpty()) {
                return itemStack1;
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
        return this.getCookInv().getAvailableInv(bagType);
    }

    public IItemHandlerModifiable getIngredientInv() {
        return this.getInputInv();
    }

    // fixme: 不应该这么做，临时解决，等待版本重构
    // bug: 不知为啥，有时并没有初始化，但还是会跳过初始化调用 cookInv.syncInv();
    public void syncInv() {
        ICookInventory cookInv = this.getCookInv();
        if (cookInv != null) {
            cookInv.syncInv();
        } else {
            MaidsoulKitchen.LOGGER.error("CookInv is null!");
        }
    }

    // fixme: 不应该这么做，临时解决，等待版本重构
    private boolean canHub() {
        return enableHub() && hasCulinaryHub;
    }

    //不与烹饪中枢交互
    protected boolean enableHub() {
        return true;
    }
}
