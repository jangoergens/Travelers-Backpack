package com.tiviacz.travelersbackpack.component.entity;

import com.tiviacz.travelersbackpack.component.ComponentUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public class EntityTravelersBackpackComponent implements IEntityTravelersBackpackComponent
{
    private String WEARABLE = "Wearable";
    private ItemStack wearable = null;
    private final LivingEntity livingEntity;

    public EntityTravelersBackpackComponent(LivingEntity livingEntity)
    {
        this.livingEntity = livingEntity;
    }

    @Override
    public boolean hasWearable()
    {
        return this.wearable != null;
    }

    @Override
    public ItemStack getWearable()
    {
        return this.wearable;
    }

    @Override
    public void setWearable(ItemStack stack)
    {
        this.wearable = stack;
    }

    @Override
    public void removeWearable()
    {
        this.wearable = null;
    }

    @Override
    public void sync()
    {
        ComponentUtils.ENTITY_WEARABLE.sync(livingEntity);
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup)
    {
        ItemStack wearable = ItemStack.fromNbtOrEmpty(registryLookup, tag.getCompound(WEARABLE));

        if(wearable.isEmpty())
        {
            setWearable(null);
        }
        else
        {
            setWearable(wearable);
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup)
    {
        NbtCompound compound = new NbtCompound();

        if(hasWearable())
        {
            ItemStack wearable = getWearable();
            compound = (NbtCompound)wearable.encodeAllowEmpty(registryLookup);
        }

        tag.put(WEARABLE, compound);

       /* if(hasWearable())
        {
            ItemStack wearable = getWearable();
            wearable.encode(registryLookup, tag);
        } */
        //if(!hasWearable())
       //{
       //     ItemStack wearable = ItemStack.EMPTY;
       //     wearable.writeNbt(tag);
       // }
    }
}