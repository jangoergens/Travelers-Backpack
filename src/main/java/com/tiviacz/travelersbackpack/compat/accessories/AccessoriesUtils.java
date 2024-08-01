package com.tiviacz.travelersbackpack.compat.accessories;

import com.tiviacz.travelersbackpack.TravelersBackpack;
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.EquipmentChecking;
import io.wispforest.accessories.api.slot.SlotEntryReference;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class AccessoriesUtils
{
    public static void rightClickUnequip(PlayerEntity player, ItemStack stack)
    {
        if(TravelersBackpack.enableAccessories())
        {
            AccessoriesCapability accessories = AccessoriesCapability.get(player);
            if(accessories == null) return;

            Predicate<ItemStack> predicate = stackPredicate -> ItemStack.areItemsAndComponentsEqual(stackPredicate, stack);

            if(accessories.isEquipped(predicate, EquipmentChecking.ACCESSORIES_ONLY))
            {
                SlotEntryReference reference = accessories.getFirstEquipped(predicate);

                if(reference != null)
                {
                    reference.reference().setStack(ItemStack.EMPTY);
                }
            }
        }
    }

    @Nullable
    public static boolean rightClickEquip(PlayerEntity player, ItemStack stack)
    {
        AccessoriesCapability accessories = AccessoriesCapability.get(player);
        if(accessories == null) return false;
        return accessories.equipAccessory(stack) != null;
    }
}