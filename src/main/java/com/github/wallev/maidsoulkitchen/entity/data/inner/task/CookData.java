package com.github.wallev.maidsoulkitchen.entity.data.inner.task;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public class CookData {
    public static final Codec<List<String>> LIST_CODEC = Codec.STRING.listOf().xmap(Lists::newArrayList, Function.identity());
    public static final Codec<CookData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("Mode").forGetter(CookData::mode),
            LIST_CODEC.fieldOf("WhitelistRecs").forGetter(CookData::whitelistRecs),
            LIST_CODEC.fieldOf("BlacklistRecs").forGetter(CookData::blacklistRecs)
    ).apply(instance, CookData::new));
    private String mode;
    private List<String> whitelistRecs;
    private List<String> blacklistRecs;

    public CookData() {
        this(Lists.newArrayList());
    }

    public CookData(List<String> blacklistRecs) {
        this(Lists.newArrayList(), blacklistRecs);
    }

    public CookData(List<String> whitelistRecs, List<String> blacklistRecs) {
        this(Mode.BLACKLIST.name, whitelistRecs, blacklistRecs);
    }

    public CookData(String mode, List<String> whitelistRecs, List<String> blacklistRecs) {
        this.mode = mode;
        this.whitelistRecs = whitelistRecs;
        this.blacklistRecs = blacklistRecs;
    }

    public void addOrRemoveRec(String rec, String mode) {
        switch (mode) {
            case "whitelist" -> {
                if (this.whitelistRecs.contains(rec)) {
                    this.whitelistRecs.remove(rec);
                } else {
                    this.whitelistRecs.add(rec);
                }
            }
            case "blacklist" -> {
                if (this.blacklistRecs.contains(rec)) {
                    this.blacklistRecs.remove(rec);
                } else {
                    this.blacklistRecs.add(rec);
                }
            }
        }
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<String> whitelistRecs() {
        return whitelistRecs;
    }

    public List<String> blacklistRecs() {
        return blacklistRecs;
    }

    public String mode() {
        return mode;
    }

    public List<String> getRecs() {
        if (this.isWhitelistMode()) {
            return this.whitelistRecs;
        } else {
            return this.blacklistRecs;
        }
    }

    public boolean isWhitelistMode() {
        return Mode.byName(this.mode).isWhitelistMode();
    }

    public boolean canCook(String recId) {
        switch (this.mode) {
            case "whitelist" -> {
                return this.whitelistRecs.contains(recId);
            }
            case "blacklist" -> {
                return !this.blacklistRecs.contains(recId);
            }
        }
        return false;
    }

    public boolean canCook(Recipe<?> r) {
        return this.canCook(r.getId().toString());
    }

    public enum Mode {
        WHITELIST("whitelist"),
        BLACKLIST("blacklist");

        public final String name;

        Mode(String name) {
            this.name = name;
        }

        public boolean isWhitelistMode() {
            return this == WHITELIST;
        }

        public static boolean isWhitelistMode(String name) {
            return byName(name).isWhitelistMode();
        }

        public static Mode byName(String name) {
            return Mode.valueOf(name.toUpperCase(Locale.ENGLISH));
        }
    }
}
