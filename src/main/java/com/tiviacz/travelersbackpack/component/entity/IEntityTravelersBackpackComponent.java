package com.tiviacz.travelersbackpack.component.entity;

import net.minecraft.item.ItemStack;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public interface IEntityTravelersBackpackComponent extends Component, AutoSyncedComponent
{
    boolean hasWearable();

    ItemStack getWearable();

    void setWearable(ItemStack stack);

    void removeWearable();

    void sync();
}