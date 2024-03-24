package com.tiviacz.travelersbackpack.init;

import com.tiviacz.travelersbackpack.TravelersBackpack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags
{
    public static final TagKey<Item> BLACKLISTED_ITEMS = ItemTags.create(new ResourceLocation(TravelersBackpack.MODID, "blacklisted_items"));
    public static final TagKey<Item> ACCEPTABLE_TOOLS = ItemTags.create(new ResourceLocation(TravelersBackpack.MODID, "acceptable_tools"));
    public static final TagKey<Item> CUSTOM_TRAVELERS_BACKPACK = ItemTags.create(new ResourceLocation(TravelersBackpack.MODID, "custom_travelers_backpack"));
}