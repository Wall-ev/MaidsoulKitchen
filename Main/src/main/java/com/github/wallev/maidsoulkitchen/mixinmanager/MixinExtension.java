package com.github.wallev.maidsoulkitchen.mixinmanager;

import com.llamalad7.mixinextras.utils.MixinInternals;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.extensibility.IMixinConfig;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;

import java.util.List;

public class MixinExtension implements IExtension {
    public MixinExtension() {
        Mixins.getConfigs().forEach(mixinConfig -> {
            IMixinConfig config = mixinConfig.getConfig();
            List<String> mixinClasses = MixinConfigHelper.getMixinClasses(config);
            int a = 1;
        });
    }

    @Override
    public boolean checkActive(MixinEnvironment environment) {
        return true;
    }

    @Override
    public void preApply(ITargetClassContext context) {
        MixinInternals.getMixinsFor(context).forEach(mixinInfo -> {
            if (!"com.github.tartaricacid.touhoulittlemaid.mixin.ThrownTridentMixin".equals(mixinInfo.getLeft().getClassName()))
                return;
            mixinInfo.getRight().methods.removeIf(methodNode -> "onHitEntity".equals(methodNode.name));
        });
        int a = 1;
    }

    @Override
    public void postApply(ITargetClassContext context) {

    }

    @Override
    public void export(MixinEnvironment env, String name, boolean force, ClassNode classNode) {

    }
}
