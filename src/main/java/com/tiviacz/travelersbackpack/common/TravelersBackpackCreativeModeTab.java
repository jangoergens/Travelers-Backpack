package com.tiviacz.travelersbackpack.common;

import com.tiviacz.travelersbackpack.TravelersBackpack;
import com.tiviacz.travelersbackpack.init.ModBlocks;
import com.tiviacz.travelersbackpack.init.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.RegistryObject;

public class TravelersBackpackCreativeModeTab extends CreativeModeTab
{
    public static final CreativeModeTab TAB_TRAVELERS_BACKPACK = new TravelersBackpackCreativeModeTab(CreativeModeTab.getGroupCountSafe(), TravelersBackpack.MODID);

    private TravelersBackpackCreativeModeTab(int index, String label)
    {
        super(index, label);
    }

    @Override
    public ItemStack makeIcon()
    {
        return new ItemStack(ModItems.STANDARD_TRAVELERS_BACKPACK.get());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void fillItemList(NonNullList<ItemStack> items)
    {
        addItem(items, ModItems.SLEEPING_BAG.get());
        addItem(items, ModItems.BACKPACK_TANK.get());
        addItem(items, ModItems.HOSE_NOZZLE.get());
        addItem(items, ModItems.HOSE.get());

        //Standard
        addBlock(items, ModBlocks.STANDARD_TRAVELERS_BACKPACK);

        //Blocks
        addBlock(items, ModBlocks.NETHERITE_TRAVELERS_BACKPACK);
        addBlock(items, ModBlocks.DIAMOND_TRAVELERS_BACKPACK);
        addBlock(items, ModBlocks.GOLD_TRAVELERS_BACKPACK);
        addBlock(items, ModBlocks.EMERALD_TRAVELERS_BACKPACK);
        addBlock(items, ModBlocks.IRON_TRAVELERS_BACKPACK);
        addBlock(items, ModBlocks.LAPIS_TRAVELERS_BACKPACK);
        addBlock(items, ModBlocks.REDSTONE_TRAVELERS_BACKPACK);
        addBlock(items, ModBlocks.COAL_TRAVELERS_BACKPACK);

        addBlock(items, ModBlocks.QUARTZ_TRAVELERS_BACKPACK);
        addBlock(items, ModBlocks.BOOKSHELF_TRAVELERS_BACKPACK);
        //addBlock(items, ModBlocks.CRYING_OBSIDIAN_TRAVELERS_BACKPACK);

        addBlock(items, ModBlocks.HAY_TRAVELERS_BACKPACK);
        addBlock(items, ModBlocks.MELON_TRAVELERS_BACKPACK);
        addBlock(items, ModBlocks.PUMPKIN_TRAVELERS_BACKPACK);

        addBlock(items, ModBlocks.BLAZE_TRAVELERS_BACKPACK);

        //Friendly Mobs
        addBlock(items, ModBlocks.BAT_TRAVELERS_BACKPACK);
        addBlock(items, ModBlocks.WOLF_TRAVELERS_BACKPACK);
        addBlock(items, ModBlocks.FOX_TRAVELERS_BACKPACK);
        addBlock(items, ModBlocks.OCELOT_TRAVELERS_BACKPACK);
        addBlock(items, ModBlocks.COW_TRAVELERS_BACKPACK);
        addBlock(items, ModBlocks.PIG_TRAVELERS_BACKPACK);
        addBlock(items, ModBlocks.CHICKEN_TRAVELERS_BACKPACK);
        addBlock(items, ModBlocks.VILLAGER_TRAVELERS_BACKPACK);
    }

    public void addItem(NonNullList<ItemStack> items, Item item)
    {
        items.add(new ItemStack(item));
    }

    public void addBlock(NonNullList<ItemStack> items, RegistryObject<Block> block)
    {
        items.add(new ItemStack(block.get()));
    }
}