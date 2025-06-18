package com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec;

public class ItemAmount {
    private final int amount;
    private int count;
    private int maxCount;
    private int recAmount = 1;
    private boolean tool;

    public ItemAmount(int amount, int count) {
        this.amount = amount;
        this.count = count;
    }

    public ItemAmount(int count) {
        this(1, count);
    }

    public void setMaxCount(int count) {
        this.maxCount = count;
    }

    public void addCount(int count) {
        this.count += count;
    }

    public void addCount() {
        if (this.maxCount > this.count) {
            return;
        }
        this.addCount(1);
    }

    public int needCount() {
        return count * amount;
    }

    public int getCount() {
        return count;
    }

    public int getAmount() {
        return amount;
    }

    public int getRecAmount() {
        return recAmount;
    }

    public void setRecAmount(int recAmount) {
        this.recAmount = recAmount;
    }

    public boolean isTool() {
        return tool;
    }

    public void setTool(boolean tool) {
        this.tool = tool;
    }
}
