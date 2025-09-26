package com.github.wallev.maidsoulkitchen.mixinmanager.legacy.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class CommentConfig {
    public static final Codec<CommentConfig> CODEC = RecordCodecBuilder.create(instance -> {
        RecordCodecBuilder<CommentConfig, String> enUsCodec = Codec.STRING.fieldOf("en_us").forGetter((instance0) -> {
            return instance0.getEnUs();
        });
        RecordCodecBuilder<CommentConfig, String> zhCnCodec = Codec.STRING.fieldOf("zh_cn").forGetter((instance0) -> {
            return instance0.getZhCn();
        });
        return instance.group(enUsCodec, zhCnCodec).apply(instance, (us, zh) -> {
            return new CommentConfig(us, zh);
        });
    });
    public static final Codec<CommentConfig> NAME_CODEC = CODEC.fieldOf("__comment").codec();

    private String enUs = "";
    private String zhCn = "";

    public CommentConfig(String enUs, String zhCn) {
        this.enUs = enUs;
        this.zhCn = zhCn;
    }

    public String getEnUs() {
        return enUs;
    }

    public void setEnUs(String enUs) {
        this.enUs = enUs;
    }

    public String getZhCn() {
        return zhCn;
    }

    public void setZhCn(String zhCn) {
        this.zhCn = zhCn;
    }
}
