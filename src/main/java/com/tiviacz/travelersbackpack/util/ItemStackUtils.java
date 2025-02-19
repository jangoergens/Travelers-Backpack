package com.tiviacz.travelersbackpack.util;

import com.tiviacz.travelersbackpack.items.HoseItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class ItemStackUtils
{
    public static boolean isSameItemSameTags(ItemStack stack1, ItemStack stack2)
    {
        //Hose patch
        if(stack1.getItem() instanceof HoseItem && stack1.is(stack2.getItem())) return true;
        
        return ItemStack.isSameItem(stack1, stack2) && tagMatches(stack1, stack2);
    }

    public static boolean tagMatches(ItemStack stack1, ItemStack stack2)
    {
        if (stack1.isEmpty() && stack2.isEmpty()) {
            return true;
        } else if (!stack1.isEmpty() && !stack2.isEmpty()) {
            if (stack1.getTag() == null && stack2.getTag() != null) {
                return false;
            } else {

                CompoundTag copy1 = stack1.getTag() == null ? null : stack1.getTag().copy();
                CompoundTag copy2 = stack2.getTag() == null ? null : stack2.getTag().copy();

                if(copy1 != null)
                {
                    if(copy1.contains("Damage"))
                    {
                        copy1.remove("Damage");
                    }
                }

                if(copy2 != null)
                {
                    if(copy2.contains("Damage"))
                    {
                        copy2.remove("Damage");
                    }
                }

                return (stack1.getTag() == null || copy1.equals(copy2)) && stack1.areCapsCompatible(stack2);
            }
        } else {
            return false;
        }
    }
}