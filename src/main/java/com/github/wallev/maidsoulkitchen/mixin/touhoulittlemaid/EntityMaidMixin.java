package com.github.wallev.maidsoulkitchen.mixin.touhoulittlemaid;

import com.github.wallev.maidsoulkitchen.entity.passive.IAddonMaid;
import com.github.wallev.maidsoulkitchen.util.FakePlayerUtil;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

import java.lang.ref.WeakReference;

@Mixin(value = EntityMaid.class, remap = false)
public abstract class EntityMaidMixin extends TamableAnimal implements CrossbowAttackMob, IMaid, IAddonMaid {

    @SuppressWarnings("all")
    private WeakReference<FakePlayer> fakePlayer;

    protected EntityMaidMixin(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public @NotNull WeakReference<FakePlayer> tlmk$getFakePlayer() {
        this.tlmk$initFakePlayer();
        return fakePlayer;
    }

    @Override
    public void tlmk$initFakePlayer() {
        if (fakePlayer == null) {
            this.fakePlayer = FakePlayerUtil.setupBeforeTrigger((ServerLevel) level(), this.getName().getString(), this);
        }
    }
}
