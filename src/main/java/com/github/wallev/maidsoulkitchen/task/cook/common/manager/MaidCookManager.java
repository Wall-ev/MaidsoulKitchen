package com.github.wallev.maidsoulkitchen.task.cook.common.manager;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.cook.v1.CookDataV1;
import com.github.wallev.maidsoulkitchen.inventory.container.item.BagType;
import com.github.wallev.maidsoulkitchen.item.ItemCulinaryHub;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.chest.ChestInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.chest.ChestInvsData;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemDefinition;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.itemdown.HubItemDown;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.itemdown.IItemDown;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.itemdown.MaidItemDown;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.itemdown.RecDataUse;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.maid.IMaidCookInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.maid.MaidCookBagInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.maid.MaidInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidRec;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.mkrec.MKRecipe;
import com.github.wallev.maidsoulkitchen.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.*;
import java.util.function.Predicate;

public class MaidCookManager<R extends Recipe<? extends Container>> {
    protected final MaidItemDown maidItemDown = new MaidItemDown();
    protected final HubItemDown hubItemDown = new HubItemDown();
    protected final CheckRange checkRange = new CheckRange();

    protected final EntityMaid maid;
    protected final ServerLevel level;
    protected final ICookTask<?, R> task;
    protected final RecSerializerManager<R> recSerializerManager;
    protected final CookBeBase<?> cookBeBase;
    protected CookDataV1 cookData;

    protected boolean init = false;
    // 0: 无
    // 1: 读取箱子物资
    // 2: 生成配方
    // 3: 转移物资（收尾）
    protected int runState = 0;
    protected List<BlockEntity> validChests = Collections.emptyList();
    protected RecsGenerate<R> recsGenerate = new RecsGenerate<>();

    protected IMaidCookInventory cookInv;
    protected ItemInventory itemInventory;
    protected ChestInventory chestInputInventory;
    protected ChestInventory chestOutputInventory;
    protected IItemDown itemDown = maidItemDown;
    protected boolean hasCulinaryHub;
    protected Map<BagType, List<BlockPos>> bindingPoses;
    protected int tryTime = 0;

    protected LinkedList<MaidRec> allMaidRecsFromChest = new LinkedList<>();
    protected LinkedList<MaidRec> maidRecs = new LinkedList<>();

    public MaidCookManager(RecSerializerManager<R> recSerializerManager, EntityMaid maid, ICookTask<?, R> task, CookBeBase<?> cookBeBase) {
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
        MaidRec maidRec = this.maidRecs.poll();
        assert maidRec != null;
        BubbleUtil.makeResultsBubble(maid, maidRec);
        return maidRec;
    }

    public Map<ItemDefinition, LinkedList<ItemStack>> getInvIngredients() {
        return getItemInventory().getStacksMap();
    }

    public ItemInventory getItemInventory() {
        return getCookInv().getItemInventory();
    }

    public void updateInvIngredients() {
        getItemInventory().update();
    }

    private boolean initInvData() {
        if (this.cookInv == null || this.bindingPoses == null || (!this.hasCulinaryHub && !this.findCulinaryHub().isEmpty())) {
            this.hasCulinaryHub = !this.findCulinaryHub().isEmpty();
            this.bindingPoses = ItemCulinaryHub.getBindPoses(this.findCulinaryHub());
            //@todo
            this.cookInv = this.initCookInv();
            this.chestInputInventory = new ChestInventory();
            this.chestOutputInventory = new ChestInventory();

            return true;
        }
        return false;
    }

    private IMaidCookInventory initCookInv() {
        if (this.canHub()) {
            ItemStack culinaryHub = this.findCulinaryHub();
            if (!culinaryHub.isEmpty()) {
                return new MaidCookBagInventory(maid, culinaryHub);
            }
        }
        return new MaidInventory(maid);
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

            IItemHandler beInv = ItemCulinaryHub.getBeInv(blockEntity);
            if (beInv == null) {
                continue;
            }

            chestHandlers.add(beInv);
            chestPos.add(pos);
        }
    }

    private List<BlockPos> getBindingTypePoses(BagType bagType) {
        return this.bindingPoses.getOrDefault(bagType, Collections.emptyList());
    }

    public EntityMaid getMaid() {
        return maid;
    }

    protected List<MKRecipe<R>> getRecs() {
        List<MKRecipe<R>> list = this.getFilterRecipes(recsGenerate.getRecs());
        return shuffle(list);
    }

    protected List<MKRecipe<R>> getFilterRecipes(List<MKRecipe<R>> rec) {
        return new ArrayList<>(rec);
    }

    public boolean checkAndInit() {
        //预防隙间转移走烹饪中枢
        if (this.hasCulinaryHub && this.findCulinaryHub().isEmpty()) {
            this.maidRecs = new LinkedList<>();
            MemoryUtil.rememberMaidRecs(maid, this.maidRecs);
            this.maid.refreshBrain(level);
            return false;
        }
        return this.init();
    }

    public void checkAndCreateRecipes() {
        // 缓存的配方原料没了
        if (runState > 0 || !maidRecs.isEmpty()) {
            return;
        }
        // 是否为上一次的背包以及手上的物品
        boolean lastInv = this.isLastCookInv();
        if (lastInv && tryTime++ < 10) {
            return;
        }
        this.createRecipesIngredients();
    }

    private boolean initTaskData() {
        if (cookData == null) {
            this.cookData = task.getTaskData(maid);
            this.recsGenerate.setRecs(this.getValidRecipesFor());
            return true;
        }
        return false;
    }

    private List<MKRecipe<R>> getValidRecipesFor() {
        return task.getRecipes(level).stream()
                .filter(r -> cookData.canCook(r))
                .toList();
    }

    private boolean isLastCookInv() {
        List<ItemStack> lastInvStack = cookInv.getLastInvStack();

        IItemHandlerModifiable availableInv1 = cookInv.getInputInv();
        if (availableInv1.getSlots() != lastInvStack.size()) return false;

        for (int i = 0; i < availableInv1.getSlots(); i++) {
            ItemStack stackInSlot = availableInv1.getStackInSlot(i);
            ItemStack cacheStack = lastInvStack.get(i);
            if (!(stackInSlot.is(cacheStack.getItem()) && stackInSlot.getCount() == cacheStack.getCount())) {
                return false;
            }
        }

        return true;
    }

    protected List<BlockEntity> initChestData() {
        List<BlockPos> ingredientPos = getBindingTypePoses(BagType.INGREDIENT);
        if (ingredientPos.isEmpty()) {
            return Collections.emptyList();
        }

        List<BlockPos> validPoses = new ArrayList<>();
        List<BlockEntity> validBlockEntities = new ArrayList<>();
        List<IItemHandler> validItemHandlers = new ArrayList<>();
        int beSlots = 0;

        for (BlockPos ingredientPo : ingredientPos) {

            if (isExtraZone(ingredientPo)) {
                continue;
            }

            BlockEntity blockEntity = level.getBlockEntity(ingredientPo);
            if (blockEntity == null) {
                continue;
            }

            IItemHandler beInv = ItemCulinaryHub.getBeInv(blockEntity);
            if (beInv == null) {
                continue;
            }

            validPoses.add(ingredientPo);
            validBlockEntities.add(blockEntity);
            validItemHandlers.add(beInv);
            beSlots += beInv.getSlots();
        }

        if (beSlots == 0) {
            return Collections.emptyList();
        }
        ChestInvsData chestInvsData = new ChestInvsData(validPoses, validBlockEntities, validItemHandlers, beSlots);
        chestInputInventory.init(chestInvsData);
        return validBlockEntities;
    }

    public void startCollectChestIngredient() {
        if (this.chestInputInventory.needReUpdate()) {
            validChests = this.initChestData();
            if (validChests.isEmpty()) {
                return;
            }
        }
        runState = 1;
        MemoryUtil.makeCollectChestItemHandler(maid);
    }

    public void startGenerateRecs() {
        runState = 2;

        this.itemDown = hubItemDown;
        this.itemDown.clear();
        this.recsGenerate.setCurrentRecs(this.getRecs());
        this.initConditions();
        ItemInventory chestItemInventory = chestInputInventory.getItemInventory();
        Map<ItemDefinition, Long> available = new HashMap<>(chestItemInventory.getStacks());
        recsGenerate.setAvailable(available);

        MemoryUtil.makeGenerateRecs(maid);
    }

    public boolean recsGenDoneAndUpdate() {
        this.runState = 3;
        if (itemDown instanceof HubItemDown) {
            // 创建食材配方失败，提前返回
            if (maidRecs.isEmpty()) {
                this.resetState();
                this.makeResultsBubble();
                return false;
            }

            Map<ItemDefinition, Integer> useItemDef = this.itemDown.getUseItemDef();
            IItemHandlerModifiable inputInv = this.cookInv.getInputInv();
            this.extractedChestItem2Bag(useItemDef, chestInputInventory.getItemInventory(), inputInv, false);

            int recLimitIndex = this.itemDown.getRecLimitIndex();
            if (recLimitIndex > maidRecs.size()) {
                this.allMaidRecsFromChest = new LinkedList<>(maidRecs);
                this.maidRecs = new LinkedList<>(maidRecs.subList(0, recLimitIndex));
            }

            this.syncInv();
            // 更新所有箱子的状态
            for (BlockEntity be : validChests) {
                TileUtil.makeChanged(be);
            }

            this.chestIngredientDone();
        }
        MemoryUtil.rememberMaidRecs(maid, this.maidRecs);
        this.resetState();
        this.cookInv.calcAvailableSlots();
        this.makeResultsBubble();
        this.setNextCheckTickCount(0);
        return true;
    }

    private void extractedChestItem2Bag(Map<ItemDefinition, Integer> useItemDef, ItemInventory itemInventory1, IItemHandlerModifiable inputInv, boolean simulate) {
        for (Map.Entry<ItemDefinition, Integer> entry : useItemDef.entrySet()) {
            ItemDefinition itemDefinition = entry.getKey();
            Integer amount = entry.getValue();
            LinkedList<ItemStack> itemStacks = itemInventory1.getItemStacks(itemDefinition);

            for (ItemStack itemStack : itemStacks) {
                if (itemStack == null || itemStack.isEmpty()) {
                    continue;
                }

                int stackCount = itemStack.getCount();
                if (stackCount >= amount) {
                    ItemStack copy = itemStack.copyWithCount(amount);
                    ItemStack leftStack = ItemHandlerHelper.insertItemStacked(inputInv, copy, false);
                    if (!simulate) {
                        itemStack.shrink(copy.getCount() - leftStack.getCount());
                    }

                    break;
                } else {
                    ItemStack copy = itemStack.copy();
                    ItemStack leftStack = ItemHandlerHelper.insertItemStacked(inputInv, copy, false);
                    if (!simulate) {
                        itemStack.shrink(copy.getCount() - leftStack.getCount());
                    }
                    amount -= stackCount;

                    if (amount <= 0) {
                        break;
                    }
                }
            }
        }
    }

    private boolean isExtraZone(BlockPos ingredientPo) {
        return ItemCulinaryHub.isExtraZone(maid, ingredientPo);
    }

    private boolean init() {
        boolean initTaskData = this.initTaskData();
        boolean initInvData = this.initInvData();
        if (initTaskData || initInvData) {
            this.init = true;
            this.maidRecs = new LinkedList<>();
            MemoryUtil.rememberMaidRecs(maid, this.maidRecs);
            return false;
        }
        return true;
    }

    public boolean inited() {
        return init;
    }

    private void createRecipesIngredients() {
        this.clear();
        // 将CookBag里无用的配方原料放回原料箱子
        this.itemUnIngre2Chest();

        // 获取原料箱子配方原料并置入CookBag
        if (this.canHub()) {
            this.startCollectChestIngredient();
        } else {
            this.itemDown = maidItemDown;
            this.itemDown.clear();
            this.cookInv.refreshInv();
            this.recsGenerate.setCurrentRecs(this.getRecs());
            this.createIngres();
//            this.makeResultsBubble();
        }
    }

    public void chestIngredientDone() {
        this.runState = 0;

        this.chestInputInventory.update();
        // 更新CookBag的inventory
        this.syncInv();
        this.cookInv.refreshInv();
//        this.makeResultsBubble();
        this.cookInv.calcAvailableSlots();
//        LogUtil.debug("availableSlots: {}", cookInv.getInputAvailableSlots());
        return;
    }

    public void itemOutput2Chest() {
        this.itemCookBag2Chest(BagType.OUTPUT, false);
    }

    public void itemUnIngre2Chest() {
        this.itemCookBag2Chest(BagType.INGREDIENT, true);
    }

    protected void createIngres() {
        this.initConditions();
        Map<ItemDefinition, Long> available = new HashMap<>(getItemInventory().getStacks());
        recsGenerate.setAvailable(available);

        MemoryUtil.makeGenerateRecs(maid);
        runState = 2;
    }

    public void tickGenerateRecs() {
        List<MKRecipe<R>> mkRecipes = recsGenerate.tickRun();
        Map<ItemDefinition, Long> available = recsGenerate.getAvailable();

        LinkedList<MaidRec> maidRecs1 = recSerializerManager.createMaidRecs(mkRecipes, available, this::recAdd, this::recIsValid, this::doItemUse, this::doneConsumer);
        this.maidRecs.addAll(maidRecs1);
//        MemoryUtil.rememberMaidRecs(maid, this.maidRecs);
    }

    private void doneConsumer(boolean done) {
        if (done) {
            this.runState = 2;
            this.recsGenerate.markDone();
        }
    }

    private boolean doItemUse(RecDataUse recDataUse) {
        return this.itemDown.read(recDataUse);
    }

    public void makeResultsBubble() {
        if (maidRecs.isEmpty()) {
            BubbleUtil.makeResultsBubbleWithEmpty(maid);
        } else {
            BubbleUtil.makeResultsBubble(maid, maidRecs);
        }
    }

    public void initConditions() {
    }

    public void clear() {
        this.resetState();
        this.recsGenerate.clear();
        this.maidRecs.clear();
        MemoryUtil.rememberMaidRecs(maid, this.maidRecs);
        this.itemDown.clear();
        this.chestInputInventory.clear();
    }

    public void resetState() {
        this.tryTime = 0;
        this.runState = 0;
    }

    protected void recAdd(MKRecipe<R> r, IndexRange indexRange) {
    }

    protected boolean recIsValid(MKRecipe<R> r) {
        return true;
    }

    protected List<MKRecipe<R>> shuffle(List<MKRecipe<R>> recipes) {
        Collections.shuffle(recipes);
        return recipes;
    }

    public ItemStack getItem(Predicate<ItemStack> predicate) {
        if (this.canHub()) {
            IItemHandlerModifiable inputInv = this.getInputInv();
            ItemStack itemStack = InvUtil.getStack(inputInv, predicate);
            if (!itemStack.isEmpty()) {
                return itemStack;
            } else {
                return this.getStackFromChest(predicate);
            }
        } else {
            return InvUtil.getStack(maid.getAvailableInv(true), predicate);
        }
    }

    public ItemStack getItem(ItemStack stack) {
        return this.getItem(stack.getItem());
    }

    public ItemStack getItem(Item item) {
        return this.getItem(itemStack -> itemStack.is(item));
    }

    public boolean hasItem(Predicate<ItemStack> predicate) {
        return !this.getItem(predicate).isEmpty();
    }

    public boolean hasItem(ItemStack stack) {
        return !this.getItem(stack).isEmpty();
    }

    public boolean hasItem(Item item) {
        return !this.getItem(item).isEmpty();
    }

    protected ItemStack getStackFromChest(Predicate<ItemStack> predicate) {
        List<BlockPos> bindModePoses = getBindingTypePoses(BagType.INGREDIENT);
        for (BlockPos bindModePose : bindModePoses) {
            if (isExtraZone(bindModePose)) {
                continue;
            }

            BlockEntity blockEntity = level.getBlockEntity(bindModePose);
            if (blockEntity == null) {
                continue;
            }

            IItemHandler beInv = ItemCulinaryHub.getBeInv(blockEntity);
            if (beInv == null) {
                continue;
            }

            ItemStack itemStack1 = InvUtil.getStack(beInv, predicate);
            if (!itemStack1.isEmpty()) {
                return itemStack1;
            }
        }
        return ItemStack.EMPTY;
    }

    public IItemHandlerModifiable getOutputInv() {
        return this.cookInv.getOutputInv();
    }

    public IItemHandlerModifiable getInputInv() {
        return this.cookInv.getInputInv();
    }

    private IItemHandlerModifiable getBagContainerInv(BagType bagType) {
        return this.cookInv.getAvailableInv(bagType);
    }

    public void syncInv() {
        this.cookInv.syncInv();
    }

    public IMaidCookInventory getCookInv() {
        return cookInv;
    }

    // fixme: 不应该这么做，临时解决，等待版本重构
    private boolean canHub() {
        return enableHub() && hasCulinaryHub;
    }

    //不与烹饪中枢交互
    protected boolean enableHub() {
        return true;
    }

    public ChestInventory getChestOutputInventory() {
        return chestOutputInventory;
    }

    public ChestInventory getChestInputInventory() {
        return chestInputInventory;
    }

    public int getRunState() {
        return runState;
    }

    public boolean recsGenerateDone() {
        return recsGenerate.done();
    }

    public boolean checkExtraStartConditions() {
        return checkRange.checkExtraStartConditions(maid);
    }

    public void setMaxCheckRate(int maxCheckRate) {
        this.checkRange.setMaxCheckRate(maxCheckRate);
    }

    public void setNextCheckTickCount(int nextCheckTickCount) {
        this.checkRange.setNextCheckTickCount(nextCheckTickCount);
    }
}
