package com.tiviacz.travelersbackpack.inventory.sorter;

import com.mojang.datafixers.util.Pair;
import com.tiviacz.travelersbackpack.components.Slots;
import com.tiviacz.travelersbackpack.init.ModDataComponents;
import com.tiviacz.travelersbackpack.inventory.ITravelersBackpackContainer;
import com.tiviacz.travelersbackpack.util.Reference;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SlotManager
{
    protected final ITravelersBackpackContainer container;
    protected List<Integer> unsortableSlots = new ArrayList<>();
    protected List<Pair<Integer, ItemStack>> memorySlots = new ArrayList<>();
    protected boolean isUnsortableActive = false;
    protected boolean isMemoryActive = false;

    public static final String UNSORTABLE_SLOTS = "UnsortableSlots";
    public static final String MEMORY_SLOTS = "MemorySlots";

    public static final byte UNSORTABLE = 0;
    public static final byte MEMORY = 1;

    public SlotManager(ITravelersBackpackContainer container)
    {
        this.container = container;
    }

    public List<Integer> getUnsortableSlots()
    {
        return this.unsortableSlots;
    }

    public List<Pair<Integer, ItemStack>> getMemorySlots()
    {
        return this.memorySlots;
    }

    public boolean isSlot(byte type, int slot)
    {
        if(type == UNSORTABLE)
        {
            return unsortableSlots.contains(slot);
        }

        if(type == MEMORY)
        {
            for(Pair<Integer, ItemStack> pair : memorySlots)
            {
                if(pair.getFirst() == slot) return true;
            }
        }
        return false;
    }

    public void setUnsortableSlots(int[] slots, boolean isFinal)
    {
        if(isSelectorActive(UNSORTABLE))
        {
            unsortableSlots = Arrays.stream(slots).boxed().collect(Collectors.toList());

            if(isFinal)
            {
                setChanged();
            }
        }
    }

    public void setMemorySlots(int[] slots, ItemStack[] stacks, boolean isFinal)
    {
        if(isSelectorActive(MEMORY))
        {
            List<Pair<Integer, ItemStack>> pairs = new ArrayList<>();

            for(int i = 0; i < stacks.length; i++)
            {
                pairs.add(Pair.of(slots[i], stacks[i]));
            }

            //Sort
            pairs.sort(Comparator.comparing(Pair::getFirst));

            this.memorySlots = pairs;

            if(isFinal)
            {
                setChanged();
            }
        }
    }

    public void setMemorySlot(int slot, ItemStack stack)
    {
        if(isSelectorActive(MEMORY))
        {
            if(slot <= container.getHandler().getSlots() - 1)
            {
                if(isSlot(MEMORY, slot))
                {
                    memorySlots.removeIf(p -> p.getFirst() == slot);
                }
                else
                {
                    memorySlots.add(Pair.of(slot, stack));
                }
            }
        }
    }

    public void setUnsortableSlot(int slot)
    {
        if(isSelectorActive(UNSORTABLE))
        {
            if(slot <= container.getHandler().getSlots() - 1)
            {
                if(isSlot(UNSORTABLE, slot))
                {
                    unsortableSlots.remove((Object)slot);
                }
                else
                {
                    unsortableSlots.add(slot);
                }
            }
        }
    }

    public void clearUnsortables()
    {
        if(isSelectorActive(UNSORTABLE))
        {
            unsortableSlots = new ArrayList<>();
        }
    }

    public void setChanged()
    {
        if(container.getScreenID() != Reference.BLOCK_ENTITY_SCREEN_ID)
        {
            container.setDataChanged(ITravelersBackpackContainer.SLOT_DATA);
        }
        else
        {
            container.setDataChanged();
        }
    }

    public boolean isSelectorActive(byte type)
    {
        return switch (type) {
            case UNSORTABLE -> this.isUnsortableActive;
            case MEMORY -> this.isMemoryActive;
            default -> false;
        };
    }

    public void setSelectorActive(byte type, boolean bool)
    {
        switch(type)
        {
            case UNSORTABLE -> this.isUnsortableActive = bool;
            case MEMORY -> this.isMemoryActive = bool;
        }
    }

    public void saveUnsortableSlots(CompoundTag compound)
    {
        compound.putIntArray(UNSORTABLE_SLOTS, getUnsortableSlots().stream().mapToInt(i -> i).toArray());
    }

    public void saveUnsortableSlots(ItemStack stack)
    {
        Slots newSlots = new Slots(getUnsortableSlots(), getMemorySlots());
        stack.set(ModDataComponents.SLOTS.get(), newSlots);
    }

    public void loadUnsortableSlots(CompoundTag compound)
    {
        this.unsortableSlots = Arrays.stream(compound.getIntArray(UNSORTABLE_SLOTS)).boxed().collect(Collectors.toList());
    }

    public void loadUnsortableSlots(ItemStack stack)
    {
        this.unsortableSlots = new ArrayList<>(stack.getOrDefault(ModDataComponents.SLOTS.get(), Slots.createDefault()).unsortables());
    }

    public void saveMemorySlots(HolderLookup.Provider provider, CompoundTag compound)
    {
        ListTag memorySlotsList = new ListTag();

        for(Pair<Integer, ItemStack> pair : memorySlots)
        {
            if(!pair.getSecond().isEmpty())
            {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt("Slot", pair.getFirst());
                memorySlotsList.add(pair.getSecond().save(provider, itemTag));
            }
        }
        compound.put(MEMORY_SLOTS, memorySlotsList);
    }

    public void saveMemorySlots(ItemStack stack)
    {
        Slots newSlots = new Slots(getUnsortableSlots(), getMemorySlots());
        stack.set(ModDataComponents.SLOTS.get(), newSlots);
    }

    public void loadMemorySlots(HolderLookup.Provider provider, CompoundTag compound)
    {
        ListTag tagList = compound.getList(MEMORY_SLOTS, Tag.TAG_COMPOUND);
        List<Pair<Integer, ItemStack>> pairs = new ArrayList<>();

        for(int i = 0; i < tagList.size(); i++)
        {
            CompoundTag itemTag = tagList.getCompound(i);
            int slot = itemTag.getInt("Slot");

            if(slot <= container.getHandler().getSlots() - 1)
            {
                Pair<Integer, ItemStack> pair = Pair.of(slot, ItemStack.parseOptional(provider, itemTag));
                pairs.add(pair);
            }
        }

        this.memorySlots = pairs;
    }

    public void loadMemorySlots(ItemStack stack)
    {
        this.memorySlots = new ArrayList<>(stack.getOrDefault(ModDataComponents.SLOTS.get(), Slots.createDefault()).memory());
    }

    public Slots getSlots()
    {
        return new Slots(getUnsortableSlots(), getMemorySlots());
    }

    public SlotManager getManager(Slots slots)
    {
        this.unsortableSlots = new ArrayList<>(slots.unsortables());
        this.memorySlots = new ArrayList<>(slots.memory());
        return this;
    }
}