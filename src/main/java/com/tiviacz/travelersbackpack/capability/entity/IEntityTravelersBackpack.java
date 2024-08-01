package com.tiviacz.travelersbackpack.capability.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IEntityTravelersBackpack
{
    boolean hasWearable();

    ItemStack getWearable();

    void setWearable(ItemStack stack);

    void removeWearable();

    void synchronise();

    CompoundTag saveTag();

    void loadTag(CompoundTag compoundTag);
}