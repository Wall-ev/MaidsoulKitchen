package com.github.wallev.maidsoulkitchen.entity.data.inner.task;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class CookData implements ITaskData {
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

    public void addRec(String rec, String mode) {
        if (mode.equals(Mode.WHITELIST.name)) {
            this.whitelistRecs.add(rec);
        } else if (mode.equals(Mode.BLACKLIST.name)) {
            this.blacklistRecs.add(rec);
        }
    }

    public void removeRec(String rec, String mode) {
        if (mode.equals(Mode.WHITELIST.name)) {
            this.whitelistRecs.remove(rec);
        } else if (mode.equals(Mode.BLACKLIST.name)) {
            this.blacklistRecs.remove(rec);
        }
    }

    public void addOrRemoveRec(String rec, String mode) {
        if (mode.equals(Mode.WHITELIST.name)) {
            if (this.whitelistRecs.contains(rec)) {
                this.whitelistRecs.remove(rec);
            } else {
                this.whitelistRecs.add(rec);
            }
        } else if (mode.equals(Mode.BLACKLIST.name)) {
            if (this.blacklistRecs.contains(rec)) {
                this.blacklistRecs.remove(rec);
            } else {
                this.blacklistRecs.add(rec);
            }
        }
    }

    public void setWhitelistRecs(List<String> whitelistRecs) {
        this.whitelistRecs = whitelistRecs;
    }

    public void setBlacklistRecs(List<String> blacklistRecs) {
        this.blacklistRecs = blacklistRecs;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<String> recs(String mode) {
        if (mode.equals(Mode.WHITELIST.name)) {
            return this.whitelistRecs;
        } else if (mode.equals(Mode.BLACKLIST.name)) {
            return this.blacklistRecs;
        }
        return Collections.emptyList();
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
        if (this.mode.equals(Mode.WHITELIST.name)) {
            return this.whitelistRecs;
        } else if (this.mode.equals(Mode.BLACKLIST.name)) {
            return this.blacklistRecs;
        } else {
            return Collections.emptyList();
        }
    }

    public enum Mode {
        WHITELIST("whitelist"),
        BLACKLIST("blacklist");

        public final String name;

        Mode(String name) {
            this.name = name;
        }
    }
}
