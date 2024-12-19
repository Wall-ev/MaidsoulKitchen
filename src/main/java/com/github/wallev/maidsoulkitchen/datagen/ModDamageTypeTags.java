package com.github.wallev.maidsoulkitchen.datagen;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModDamageTypes;

import java.util.concurrent.CompletableFuture;

public class ModDamageTypeTags extends TagsProvider<DamageType> {
    public static final TagKey<DamageType> DAMAGES_BURN = TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(MaidsoulKitchen.MOD_ID, "damages_burn"));

    public ModDamageTypeTags(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, Registries.DAMAGE_TYPE, pLookupProvider, MaidsoulKitchen.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(DAMAGES_BURN)
                .add(DamageTypes.IN_FIRE).add(DamageTypes.ON_FIRE).add(DamageTypes.HOT_FLOOR)
                .addOptional(ModDamageTypes.STOVE_BURN.location());
    }
}
