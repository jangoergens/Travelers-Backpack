package com.tiviacz.travelersbackpack.network;

import com.tiviacz.travelersbackpack.TravelersBackpack;
import com.tiviacz.travelersbackpack.capability.AttachmentUtils;
import com.tiviacz.travelersbackpack.inventory.menu.TravelersBackpackBlockEntityMenu;
import com.tiviacz.travelersbackpack.inventory.menu.TravelersBackpackItemMenu;
import com.tiviacz.travelersbackpack.inventory.sorter.SlotManager;
import com.tiviacz.travelersbackpack.util.Reference;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record ServerboundMemoryPacket(byte screenID, boolean isActive, List<Integer> selectedSlots, List<ItemStack> stacks) implements CustomPacketPayload
{
    public static final Type<ServerboundMemoryPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(TravelersBackpack.MODID, "memory"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundMemoryPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BYTE, ServerboundMemoryPacket::screenID,
            ByteBufCodecs.BOOL, ServerboundMemoryPacket::isActive,
            ByteBufCodecs.INT.apply(ByteBufCodecs.list()), ServerboundMemoryPacket::selectedSlots,
            ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list()), ServerboundMemoryPacket::stacks,
            ServerboundMemoryPacket::new
    );

    public static void handle(final ServerboundMemoryPacket message, IPayloadContext ctx)
    {
        ctx.enqueueWork(() ->
        {
            Player player = ctx.player();

            if(player instanceof ServerPlayer serverPlayer)
            {
                if(message.screenID == Reference.WEARABLE_SCREEN_ID)
                {
                    SlotManager manager = AttachmentUtils.getBackpackInv(serverPlayer).getSlotManager();
                    manager.setSelectorActive(SlotManager.MEMORY, message.isActive);
                    manager.setMemorySlots(message.selectedSlots(), message.stacks, true);
                    manager.setSelectorActive(SlotManager.MEMORY, !message.isActive);
                }
                if(message.screenID == Reference.ITEM_SCREEN_ID)
                {
                    SlotManager manager = ((TravelersBackpackItemMenu)serverPlayer.containerMenu).container.getSlotManager();
                    manager.setSelectorActive(SlotManager.MEMORY, message.isActive);
                    manager.setMemorySlots(message.selectedSlots(), message.stacks, true);
                    manager.setSelectorActive(SlotManager.MEMORY, !message.isActive);
                }
                if(message.screenID == Reference.BLOCK_ENTITY_SCREEN_ID)
                {
                    SlotManager manager = ((TravelersBackpackBlockEntityMenu)serverPlayer.containerMenu).container.getSlotManager();
                    manager.setSelectorActive(SlotManager.MEMORY, message.isActive);
                    manager.setMemorySlots(message.selectedSlots, message.stacks, true);
                    manager.setSelectorActive(SlotManager.MEMORY, !message.isActive);
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}