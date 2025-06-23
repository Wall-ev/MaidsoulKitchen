package com.github.wallev.maidsoulkitchen.mixinmanager.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Map;

public class TaskConfigConfig {
    public static final Codec<TaskConfigConfig> CODEC = RecordCodecBuilder.create(instance -> {
        RecordCodecBuilder<TaskConfigConfig, CommentConfig> commentConfigCodec = CommentConfig.CODEC.fieldOf("__comment").forGetter(instance0 -> {
            return instance0.getCommentConfig();
        });
        RecordCodecBuilder<TaskConfigConfig, Boolean> enableConfigCodec = Codec.BOOL.fieldOf("enable").forGetter(instance0 -> {
            return instance0.isEnableConfig();
        });
        RecordCodecBuilder<TaskConfigConfig, Map<String, Boolean>> mixinConfigCodec = MixinConfigConfig.MAP_CODEC.fieldOf("mixins").forGetter(instance0 -> {
            return instance0.getMixinConfigConfig();
        });

        return instance.group(commentConfigCodec, enableConfigCodec, mixinConfigCodec).apply(instance, ((commentConfig1, aBoolean, mixinConfigConfig1) -> {
            return new TaskConfigConfig(commentConfig1, aBoolean, mixinConfigConfig1);
        }));
    });

    private CommentConfig commentConfig;
    private boolean enableConfig;
    private Map<String, Boolean> mixinConfigConfig;

    public TaskConfigConfig(CommentConfig commentConfig, boolean enableConfig, Map<String, Boolean> mixinConfigConfig) {
        this.commentConfig = commentConfig;
        this.enableConfig = enableConfig;
        this.mixinConfigConfig = mixinConfigConfig;
    }

    public CommentConfig getCommentConfig() {
        return commentConfig;
    }

    public void setCommentConfig(CommentConfig commentConfig) {
        this.commentConfig = commentConfig;
    }

    public boolean isEnableConfig() {
        return enableConfig;
    }

    public void setEnableConfig(boolean enableConfig) {
        this.enableConfig = enableConfig;
    }

    public Map<String, Boolean> getMixinConfigConfig() {
        return mixinConfigConfig;
    }

    public void setMixinConfigConfig(Map<String, Boolean> mixinConfigConfig) {
        this.mixinConfigConfig = mixinConfigConfig;
    }
}
