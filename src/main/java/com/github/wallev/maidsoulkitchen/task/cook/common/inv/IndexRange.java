package com.github.wallev.maidsoulkitchen.task.cook.common.inv;

public class IndexRange {
    private int start;
    private int end;

    public IndexRange() {
    }

    public void set(int start, int size) {
        this.start = start;
        this.end = start + size;
    }

    public int start() {
        return start;
    }

    public int end() {
        return end;
    }

    public void reset() {
        this.start = 0;
        this.end = 0;
    }
}
