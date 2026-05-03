package com.github.wallev.maidsoulkitchen.entity.data.inner.task.cook.v1;

import com.github.wallev.maidsoulkitchen.entity.data.inner.task.cook.v0.CookData;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

@SuppressWarnings("deprecation")
public class CookDataV1 extends CookData {
    public static final Codec<CookDataV1> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("Mode").forGetter(CookDataV1::mode),
            LIST_CODEC.fieldOf("WhitelistRecs").forGetter(CookDataV1::whitelistRecs),
            LIST_CODEC.fieldOf("BlacklistRecs").forGetter(CookDataV1::blacklistRecs)
    ).apply(instance, CookDataV1::new));

    public CookDataV1() {
        super();
    }

    public CookDataV1(List<String> blacklistRecs) {
        super(blacklistRecs);
    }

    public CookDataV1(List<String> whitelistRecs, List<String> blacklistRecs) {
        super(whitelistRecs, blacklistRecs);
    }

    public CookDataV1(String mode, List<String> whitelistRecs, List<String> blacklistRecs) {
        super(mode, whitelistRecs, blacklistRecs);
    }
}
