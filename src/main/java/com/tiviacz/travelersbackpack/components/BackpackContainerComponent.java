package com.tiviacz.travelersbackpack.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.OptionalInt;

public final class BackpackContainerComponent
{
    public static final BackpackContainerComponent DEFAULT = new BackpackContainerComponent(DefaultedList.of());
    public static final Codec<BackpackContainerComponent> CODEC;
    public static final PacketCodec<RegistryByteBuf, BackpackContainerComponent> PACKET_CODEC;
    private final DefaultedList<ItemStack> stacks;
    private final int hashCode;

    private BackpackContainerComponent(DefaultedList<ItemStack> stacks) {
        if (stacks.size() > 256) {
            throw new IllegalArgumentException("Got " + stacks.size() + " items, but maximum is 256");
        } else {
            this.stacks = stacks;
            this.hashCode = ItemStack.listHashCode(stacks);
        }
    }

    private BackpackContainerComponent(int size) {
        this(DefaultedList.ofSize(size, ItemStack.EMPTY));
    }

    private BackpackContainerComponent(List<ItemStack> stacks) {
        this(stacks.size());

        for(int i = 0; i < stacks.size(); ++i) {
            this.stacks.set(i, stacks.get(i));
        }

    }

    public DefaultedList<ItemStack> getStacks()
    {
        return this.stacks;
    }

    public static BackpackContainerComponent upgradeContents(int newSize, DefaultedList<ItemStack> oldContents)
    {
        List<ItemStack> items = new ArrayList<>(oldContents);
        items.addAll(DefaultedList.ofSize(newSize - oldContents.size(), ItemStack.EMPTY));
        return new BackpackContainerComponent(items);
    }

    private static BackpackContainerComponent fromSlots(List<BackpackContainerComponent.Slot> slots) {
        OptionalInt optionalInt = slots.stream().mapToInt(BackpackContainerComponent.Slot::index).max();
        if (optionalInt.isEmpty()) {
            return DEFAULT;
        } else {
            BackpackContainerComponent containerComponent = new BackpackContainerComponent(optionalInt.getAsInt() + 1);
            Iterator var3 = slots.iterator();

            while(var3.hasNext()) {
                BackpackContainerComponent.Slot slot = (BackpackContainerComponent.Slot)var3.next();
                containerComponent.stacks.set(slot.index(), slot.item());
            }

            return containerComponent;
        }
    }

    public static BackpackContainerComponent fromStacks(int size, List<ItemStack> stacks)
    {
        BackpackContainerComponent containerComponent = new BackpackContainerComponent(size);

        for(int j = 0; j < size; ++j) {
            containerComponent.stacks.set(j, stacks.get(j).copy());
        }
        return containerComponent;
    }

    private List<BackpackContainerComponent.Slot> collectSlots() {
        List<BackpackContainerComponent.Slot> list = new ArrayList();

        for(int i = 0; i < this.stacks.size(); ++i) {
            ItemStack itemStack = this.stacks.get(i);
            list.add(new BackpackContainerComponent.Slot(i, itemStack));
        }

        return list;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            boolean var10000;
            if (o instanceof BackpackContainerComponent) {
                BackpackContainerComponent containerComponent = (BackpackContainerComponent)o;
                if (ItemStack.stacksEqual(this.stacks, containerComponent.stacks)) {
                    var10000 = true;
                    return var10000;
                }
            }

            var10000 = false;
            return var10000;
        }
    }

    public int hashCode() {
        return this.hashCode;
    }

    static {
        CODEC = BackpackContainerComponent.Slot.CODEC.sizeLimitedListOf(256).xmap(BackpackContainerComponent::fromSlots, BackpackContainerComponent::collectSlots);
        PACKET_CODEC = ItemStack.OPTIONAL_PACKET_CODEC.collect(PacketCodecs.toList(256)).xmap(BackpackContainerComponent::new, (component) -> {
            return component.stacks;
        });
    }

    static record Slot(int index, ItemStack item) {
        public static final Codec<BackpackContainerComponent.Slot> CODEC = RecordCodecBuilder.create((instance) -> {
            return instance.group(Codec.intRange(0, 255).fieldOf("slot").forGetter(BackpackContainerComponent.Slot::index), ItemStack.OPTIONAL_CODEC.fieldOf("item").forGetter(BackpackContainerComponent.Slot::item)).apply(instance, BackpackContainerComponent.Slot::new);
        });

        Slot(int index, ItemStack item) {
            this.index = index;
            this.item = item;
        }

        public int index() {
            return this.index;
        }

        public ItemStack item() {
            return this.item;
        }
    }
}

