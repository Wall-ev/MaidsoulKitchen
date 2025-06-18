package com.github.wallev.maidsoulkitchen.task.cook.common.inv;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
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
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.maid.IMaidCookInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.maid.MaidCookBagInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.maid.MaidInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.ItemAmount;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidRec;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.mkrec.MKRecipe;
import com.github.wallev.maidsoulkitchen.util.BubbleUtil;
import com.github.wallev.maidsoulkitchen.util.InvUtil;
import com.github.wallev.maidsoulkitchen.util.ItemStackUtil;
import com.github.wallev.maidsoulkitchen.util.TileUtil;
import com.github.wallev.maidsoulkitchen.util.debug.annotation.IAspectAnnotation;
import com.github.wallev.maidsoulkitchen.util.debug.annotation.timerecord.TimeRecord;
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

    protected final EntityMaid maid;
    protected final ServerLevel level;
    protected final ICookTask<?, R> task;
    protected final RecSerializerManager<R> recSerializerManager;
    protected final CookBeBase<?> cookBeBase;
    protected CookData cookData;

    protected List<MKRecipe<R>> rec = new ArrayList<>();
    protected List<MKRecipe<R>> currentRecs = new ArrayList<>();
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
        return this.maidRecs.poll();
    }

    public Map<ItemDefinition, LinkedList<ItemStack>> getInvIngredients() {
        return itemInventory.getStacksMap();
    }

    public ItemInventory getItemInventory() {
        return itemInventory;
    }

    public void updateInvIngredients() {
        itemInventory.update();
    }

    private boolean initInvData() {
        if (this.cookInv == null || this.bindingPoses == null || (!this.hasCulinaryHub && !this.findCulinaryHub().isEmpty())) {
            this.hasCulinaryHub = !this.findCulinaryHub().isEmpty();
            this.bindingPoses = ItemCulinaryHub.getBindPoses(this.findCulinaryHub());
            //@todo
            this.cookInv = this.initCookInv();
            this.itemInventory = cookInv.getItemInventory();
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
        if (!maidRecs.isEmpty()) {
            return true;
        }
        // 是否为上一次的背包以及手上的物品
        boolean lastInv = this.isLastCookInv();
        if (lastInv && tryTime++ < 10) {
            return true;
        }
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

    @TimeRecord(base = @IAspectAnnotation(name = "initChestDataName"))
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


    protected boolean mapChestIngredient() {
        if (!this.canHub()) {
            return false;
        }

        List<BlockEntity> validChests = this.initChestData();
        if (validChests.isEmpty()) {
            return false;
        }

        this.itemDown = hubItemDown;
        this.itemDown.clear();
        this.currentRecs = this.getRecs();
        this.initConditions();
        ItemInventory chestItemInventory = chestInputInventory.getItemInventory();
        Map<ItemDefinition, Long> available = chestItemInventory.getStacks();
        this.createIngres(available);
        // 创建食材配方失败，提前返回
        if (maidRecs.isEmpty()) {
            return true;
        }

        Map<ItemDefinition, Integer> useItemDef = this.itemDown.getUseItemDef();
        IItemHandlerModifiable inputInv = this.cookInv.getInputInv();
        this.extractedChestItem2Bag(useItemDef, chestItemInventory, inputInv);

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
        return true;
    }

    private void extractedChestItem2Bag(Map<ItemDefinition, Integer> useItemDef, ItemInventory itemInventory1, IItemHandlerModifiable inputInv) {
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
                    itemStack.shrink(copy.getCount() - leftStack.getCount());

                    break;
                } else {
                    ItemStack copy = itemStack.copy();
                    ItemStack leftStack = ItemHandlerHelper.insertItemStacked(inputInv, copy, false);
                    itemStack.shrink(copy.getCount() - leftStack.getCount());
                    amount -= stackCount;

                    if (amount <= 0) {
                        break;
                    }
                }
            }
        }
    }

    private boolean isExtraZone(BlockPos ingredientPo) {
        float maxDistance = maid.getRestrictRadius() * ItemCulinaryHub.WORK_RANGE;
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

    @TimeRecord
    private void createRecipesIngredients() {
        this.clear();

        // 将CookBag里无用的配方原料放回原料箱子
        this.itemUnIngre2Chest();
        // 获取原料箱子配方原料并置入CookBag
        boolean mapChestIngredient = this.mapChestIngredient();
        if (!mapChestIngredient) {
            chestInputInventory.update();
            // 更新CookBag的inventory
            this.syncInv();
            this.cookInv.refreshInv();
            this.makeResultsBubble();
            return;
        }

        this.itemDown = maidItemDown;
        this.itemDown.clear();
        this.cookInv.refreshInv();
        this.currentRecs = this.getRecs();
        this.createIngres();
        this.makeResultsBubble();
    }

    public void itemOutput2Chest() {
        this.itemCookBag2Chest(BagType.OUTPUT, false);
    }

    public void itemUnIngre2Chest() {
        this.itemCookBag2Chest(BagType.INGREDIENT, true);
    }

    protected void createIngres() {
        this.initConditions();
        Map<ItemDefinition, Long> available = new HashMap<>(itemInventory.getStacks());
        this.createIngres(available);
    }

    private void createIngres(Map<ItemDefinition, Long> available) {
        this.maidRecs = recSerializerManager.createMaidRecs(this.currentRecs, available, this::recAdd, this::recIsValid, this::doItemUse);
    }

    private boolean doItemUse(Map<ItemDefinition, ItemAmount> itemUse) {
        return this.itemDown.read(itemUse);
    }

    protected void makeResultsBubble() {
        if (maidRecs.isEmpty()) {
            BubbleUtil.makeResultsBubbleWithEmpty(maid);
        } else {
            BubbleUtil.makeResultsBubble(maid, maidRecs);
        }
    }

    public void initConditions() {
    }

    public void clear() {
        this.tryTime = 0;
        this.currentRecs = Collections.emptyList();
        this.itemDown.clear();
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

    // fixme: 不应该这么做，临时解决，等待版本重构
    // bug: 不知为啥，有时并没有初始化，但还是会跳过初始化调用 cookInv.syncInv();
    public void syncInv() {
        IMaidCookInventory cookInv = this.cookInv;
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
