package com.github.catbert.tlma.entity.data.inner;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CookData {
    public static final Codec<List<String>> LIST_CODEC = Codec.STRING.listOf().xmap(Lists::newArrayList, Function.identity());
    public static final Codec<CookData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("Mode").forGetter(CookData::mode),
            LIST_CODEC.fieldOf("Recs").forGetter(CookData::recs)
    ).apply(instance, CookData::new));
    private String mode;
    private List<String> recs;

    public CookData() {
        this(Mode.RANDOM.name, new ArrayList<>());
    }

    public CookData(String mode, List<String> recs) {
        this.mode = mode;
        this.recs = recs;
    }

    public void addRec(String rec) {
        this.recs.add(rec);
    }

    public void removeRec(String rec) {
        this.recs.remove(rec);
    }

    public void addOrRemovalRec(String rec) {
        if (this.recs.contains(rec)) {
            this.recs.remove(rec);
        } else {
            this.recs.add(rec);
        }
    }

    public void setRecs(List<String> recs) {
        this.recs = recs;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<String> recs() {
        return recs;
    }

    public String mode() {
        return mode;
    }

    public enum Mode {
        RANDOM("random"),
        SELECT("select");

        public final String name;

        Mode(String select) {
            this.name = select;
        }
    }
}
