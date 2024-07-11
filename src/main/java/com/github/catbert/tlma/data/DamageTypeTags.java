package com.github.catbert.tlma.data;

import com.github.catbert.tlma.foundation.utility.Mods;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModDamageTypes;

import java.util.concurrent.CompletableFuture;

import static com.github.catbert.tlma.foundation.utility.Mods.FD;

public class DamageTypeTags extends DamageTypeTagsProvider {
    public DamageTypeTags(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        registerIsFire();
    }

    protected void registerIsFire(){
        addIsFireTag(FD, ModDamageTypes.STOVE_BURN);
    }

    protected void addIsFireTag(Mods mod, ResourceKey<DamageType> damageType){
        if (mod.isLoaded) {
            tag(net.minecraft.tags.DamageTypeTags.IS_FIRE).add(damageType);
        }
    }
}
