package com.github.wallev.maidsoulkitchen.mixinmanager.legacy.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class IssueCommentConfig extends CommentConfig {
    public static final Codec<IssueCommentConfig> CODEC = RecordCodecBuilder.create(instance -> {
        RecordCodecBuilder<IssueCommentConfig, String> enUsCodec = Codec.STRING.fieldOf("en_us").forGetter((instance0) -> {
            return instance0.getEnUs();
        });
        RecordCodecBuilder<IssueCommentConfig, String> zhCnCodec = Codec.STRING.fieldOf("zh_cn").forGetter((instance0) -> {
            return instance0.getZhCn();
        });
        RecordCodecBuilder<IssueCommentConfig, String> issueUrlCodec = Codec.STRING.fieldOf("issue_url").forGetter((instance0) -> {
            return instance0.getIssueUrl();
        });
        return instance.group(enUsCodec, zhCnCodec, issueUrlCodec).apply(instance, (us, zh, issueUrl) -> {
            return new IssueCommentConfig(us, zh, issueUrl);
        });
    });
    public static final Codec<IssueCommentConfig> NAME_CODEC = CODEC.fieldOf("__comment").codec();

    private String issueUrl = "";

    public IssueCommentConfig(String enUs, String zhCn, String issueUrl) {
        super(enUs, zhCn);
        this.issueUrl = issueUrl;

    }

    public String getIssueUrl() {
        return issueUrl;
    }

    public void setIssueUrl(String issueUrl) {
        this.issueUrl = issueUrl;
    }
}
