package com.tiviacz.travelersbackpack.inventory.screen.slot;

import com.tiviacz.travelersbackpack.component.ComponentUtils;
import com.tiviacz.travelersbackpack.init.ModTags;
import com.tiviacz.travelersbackpack.inventory.ITravelersBackpackInventory;
import com.tiviacz.travelersbackpack.util.Reference;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.screen.slot.Slot;

public class ToolSlot extends Slot
{
    private final PlayerEntity player;
    private final ITravelersBackpackInventory inventory;

    public ToolSlot(PlayerEntity player, ITravelersBackpackInventory inventoryIn, int index, int x, int y)
    {
        super(inventoryIn.getInventory(), index, x, y);

        this.player = player;
        this.inventory = inventoryIn;
    }

    @Override
    public boolean canInsert(ItemStack stack)
    {
        return isValid(stack);
    }

    public static boolean isValid(ItemStack stack)
    {
        //Datapacks :D
        if(stack.isIn(ModTags.ACCEPTABLE_TOOLS)) return true;

        if(stack.getMaxCount() == 1)
        {
            //Vanilla tools
            return stack.getItem() instanceof ToolItem || stack.getItem() instanceof HoeItem || stack.getItem() instanceof FishingRodItem || stack.getItem() instanceof ShearsItem || stack.getItem() instanceof FlintAndSteelItem;
        }
        return false;
    }

    @Override
    public void markDirty()
    {
        super.markDirty();

        if(inventory.getScreenID() == Reference.TRAVELERS_BACKPACK_WEARABLE_SCREEN_ID && !player.world.isClient)
        {
            ComponentUtils.sync(this.player);
            ComponentUtils.syncToTracking(this.player);
        }
    }
}