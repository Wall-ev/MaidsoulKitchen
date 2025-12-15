package com.github.wallev.maidsoulkitchen.datagen;

import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.nbtcustom.MsmNbtTagUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;


public class ModItemTags extends ItemTagsProvider {

    public ModItemTags(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, modId, existingFileHelper);
    }

    @SuppressWarnings("all")
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        MsmNbtTagUtil.autonGenMatchNbtItem(this);
    }

    @Override
    public IntrinsicTagAppender<Item> tag(TagKey<Item> pTag) {
        return super.tag(pTag);
    }
}
