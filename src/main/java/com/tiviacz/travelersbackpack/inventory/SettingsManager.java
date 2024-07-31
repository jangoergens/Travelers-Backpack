package com.tiviacz.travelersbackpack.inventory;

import com.tiviacz.travelersbackpack.components.Settings;
import com.tiviacz.travelersbackpack.config.TravelersBackpackConfig;
import com.tiviacz.travelersbackpack.init.ModDataComponents;
import com.tiviacz.travelersbackpack.util.Reference;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;

public class SettingsManager
{
    private final ITravelersBackpackContainer container;
    private byte[] craftingSettings = new byte[]{(byte)(TravelersBackpackConfig.SERVER.backpackSettings.craftingUpgrade.includeByDefault.get() ? 1 : 0), 0, 1};
    private byte[] toolSlotsSettings = new byte[] {0};

    //#TODO
    //private byte[] backpackSettings = new byte[] {1};

    public static final byte CRAFTING = 0;
    public static final byte TOOL_SLOTS = 1;
    //#TODO
    //public static final byte BACKPACK = 2;

    public static final int HAS_CRAFTING_GRID = 0;
    public static final int SHOW_CRAFTING_GRID = 1;
    public static final int SHIFT_CLICK_TO_BACKPACK = 2;

    public static final int SHOW_TOOL_SLOTS = 0;
    //#TODO
   // public static final int RENDER_BACKPACK = 0;

    public static final String CRAFTING_SETTINGS = "CraftingSettings";
    public static final String TOOL_SLOTS_SETTINGS = "ToolSlotsSettings";
    //#TODO
   // public static final String BACKPACK_SETTINGS = "BackpackSettings";


    public SettingsManager(ITravelersBackpackContainer container)
    {
        this.container = container;
    }

    public boolean hasCraftingGrid()
    {
        return getByte(CRAFTING, HAS_CRAFTING_GRID) == (byte)1;
    }

    public boolean shiftClickToBackpack()
    {
        return getByte(CRAFTING, SHIFT_CLICK_TO_BACKPACK) == (byte)1;
    }

    public boolean showCraftingGrid()
    {
        return getByte(CRAFTING, SHOW_CRAFTING_GRID) == (byte)1;
    }

    public boolean showToolSlots()
    {
        return getByte(TOOL_SLOTS, SHOW_TOOL_SLOTS) == (byte)1;
    }

    //#TODO
    //public boolean renderBackpack()
    //{
    //    return getByte(BACKPACK, RENDER_BACKPACK) == (byte)1;
    //}

    public byte getByte(byte dataArray, int place)
    {
        if(dataArray == CRAFTING)
        {
            return this.craftingSettings[place];
        }
        if(dataArray == TOOL_SLOTS)
        {
            return this.toolSlotsSettings[place];
        }
        //TODO
        //if(dataArray == BACKPACK)
        //{
        //    return this.backpackSettings[place];
        //}
        return 0;
    }

    public void set(byte selectedDataArray, int place, byte value)
    {
        byte[] dataArray = new byte[0];

        if(selectedDataArray == CRAFTING) dataArray = this.craftingSettings;
        if(selectedDataArray == TOOL_SLOTS) dataArray = this.toolSlotsSettings;

        //TODO
        //if(selectedDataArray == BACKPACK) dataArray = this.backpackSettings;

        dataArray[place] = value;
        setChanged();
    }

    public void saveSettings(CompoundTag compound)
    {
        compound.putByteArray(CRAFTING_SETTINGS, craftingSettings);
        compound.putByteArray(TOOL_SLOTS_SETTINGS, toolSlotsSettings);
        //#TODO
       // compound.putByteArray(BACKPACK_SETTINGS, backpackSettings);
    }

    public void saveSettings(ItemStack stack)
    {//#TODO
        stack.set(ModDataComponents.SETTINGS, Settings.createSettings(Arrays.asList(ArrayUtils.toObject(craftingSettings)), Arrays.asList(ArrayUtils.toObject(toolSlotsSettings)))); //, Arrays.asList(ArrayUtils.toObject(backpackSettings))));
    }

    public void loadSettings(CompoundTag compound)
    {//#TODO
        this.craftingSettings = compound.contains(CRAFTING_SETTINGS) ? (compound.getByteArray(CRAFTING_SETTINGS).length == 3 ? compound.getByteArray(CRAFTING_SETTINGS) : new byte[]{(byte)1, 0, 1}) : new byte[]{(byte)(TravelersBackpackConfig.SERVER.backpackSettings.craftingUpgrade.includeByDefault.get() ? 1 : 0), 0, 1};
        this.toolSlotsSettings = compound.contains(TOOL_SLOTS_SETTINGS) ? compound.getByteArray(TOOL_SLOTS_SETTINGS) : new byte[] {0};
       // this.backpackSettings = compound.contains(BACKPACK_SETTINGS) ? compound.getByteArray(BACKPACK_SETTINGS) : new byte[] {1};
    }

    public void loadSettings(ItemStack stack)
    {//#TODO
        List<List<Byte>> settings = stack.getOrDefault(ModDataComponents.SETTINGS, createDefaults());

        this.craftingSettings = ArrayUtils.toPrimitive(settings.get(0).stream().toArray(Byte[]::new));
        this.toolSlotsSettings = ArrayUtils.toPrimitive(settings.get(1).stream().toArray(Byte[]::new));
        //this.backpackSettings = settings.size() < 3 ? new byte[] {1} : ArrayUtils.toPrimitive(settings.get(2).stream().toArray(Byte[]::new));
    }

    public void setChanged()
    {
        if(container.getScreenID() != Reference.BLOCK_ENTITY_SCREEN_ID)
        {
            container.setDataChanged(ITravelersBackpackContainer.SETTINGS_DATA);
        }
        else
        {
            container.setDataChanged();
        }
    }

    public void loadDefaults()
    {
        this.craftingSettings = new byte[]{(byte)(TravelersBackpackConfig.SERVER.backpackSettings.craftingUpgrade.includeByDefault.get() ? 1 : 0), 0, 1};
        this.toolSlotsSettings = new byte[] {0};
        //this.backpackSettings = new byte[] {1};
        setChanged();
    }

    public List<List<Byte>> createDefaults()
    {
        this.craftingSettings = new byte[]{(byte)(TravelersBackpackConfig.SERVER.backpackSettings.craftingUpgrade.includeByDefault.get() ? 1 : 0), 0, 1};
        this.toolSlotsSettings = new byte[] {0};
        //this.backpackSettings = new byte[] {1};
        return Settings.createSettings(Arrays.asList(ArrayUtils.toObject(craftingSettings)), Arrays.asList(ArrayUtils.toObject(toolSlotsSettings))); //, Arrays.asList(ArrayUtils.toObject(backpackSettings)));
    }

    public boolean isDefault()
    {
        return Arrays.equals(this.craftingSettings, new byte[]{(byte) (TravelersBackpackConfig.SERVER.backpackSettings.craftingUpgrade.includeByDefault.get() ? 1 : 0), 0, 1}) && Arrays.equals(toolSlotsSettings, new byte[]{0});
    }

    public List<List<Byte>> getSettings()
    {
        return Settings.createSettings(Arrays.asList(ArrayUtils.toObject(craftingSettings)), Arrays.asList(ArrayUtils.toObject(toolSlotsSettings))); //, Arrays.asList(ArrayUtils.toObject(backpackSettings)));
    }

    public SettingsManager getManager(List<List<Byte>> data)
    {
        this.craftingSettings = ArrayUtils.toPrimitive(data.get(0).stream().toArray(Byte[]::new));
        this.toolSlotsSettings = ArrayUtils.toPrimitive(data.get(1).stream().toArray(Byte[]::new));
        //this.backpackSettings = ArrayUtils.toPrimitive(data.get(2).stream().toArray(Byte[]::new));
        return this;
    }
}