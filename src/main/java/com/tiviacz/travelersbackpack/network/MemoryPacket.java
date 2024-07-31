package com.tiviacz.travelersbackpack.network;

import com.tiviacz.travelersbackpack.TravelersBackpack;
import com.tiviacz.travelersbackpack.component.ComponentUtils;
import com.tiviacz.travelersbackpack.inventory.screen.TravelersBackpackBlockEntityScreenHandler;
import com.tiviacz.travelersbackpack.inventory.screen.TravelersBackpackItemScreenHandler;
import com.tiviacz.travelersbackpack.inventory.sorter.SlotManager;
import com.tiviacz.travelersbackpack.util.Reference;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.List;

public record MemoryPacket(byte screenID, boolean isActive, List<Integer> selectedSlots, List<ItemStack> stacks) implements CustomPayload
{
    public static final CustomPayload.Id<MemoryPacket> PACKET_ID = new CustomPayload.Id<>(Identifier.of(TravelersBackpack.MODID, "memory"));
    public static final PacketCodec<RegistryByteBuf, MemoryPacket> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.BYTE, MemoryPacket::screenID, PacketCodecs.BOOL, MemoryPacket::isActive,
            PacketCodecs.INTEGER.collect(PacketCodecs.toList()), MemoryPacket::selectedSlots, ItemStack.OPTIONAL_PACKET_CODEC.collect(PacketCodecs.toList()), MemoryPacket::stacks, MemoryPacket::new);

    public static void apply(MemoryPacket message, ServerPlayNetworking.Context context)
    {
        context.player().getServer().execute(() ->
        {
            if(context.player() != null)
            {
                if(message.screenID() == Reference.WEARABLE_SCREEN_ID)
                {
                    SlotManager manager = ComponentUtils.getBackpackInv(context.player()).getSlotManager();
                    manager.setSelectorActive(SlotManager.MEMORY, message.isActive());
                    manager.setMemorySlots(message.selectedSlots(), message.stacks(), true);
                    manager.setSelectorActive(SlotManager.MEMORY, !message.isActive());
                }
                if(message.screenID() == Reference.ITEM_SCREEN_ID)
                {
                    SlotManager manager = ((TravelersBackpackItemScreenHandler)context.player().currentScreenHandler).inventory.getSlotManager();
                    manager.setSelectorActive(SlotManager.MEMORY, message.isActive());
                    manager.setMemorySlots(message.selectedSlots(), message.stacks(), true);
                    manager.setSelectorActive(SlotManager.MEMORY, !message.isActive());
                }
                if(message.screenID() == Reference.BLOCK_ENTITY_SCREEN_ID)
                {
                    SlotManager manager = ((TravelersBackpackBlockEntityScreenHandler)context.player().currentScreenHandler).inventory.getSlotManager();
                    manager.setSelectorActive(SlotManager.MEMORY, message.isActive());
                    manager.setMemorySlots(message.selectedSlots(), message.stacks(), true);
                    manager.setSelectorActive(SlotManager.MEMORY, !message.isActive());
                }
            }
        });
    }

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId()
    {
        return PACKET_ID;
    }
}