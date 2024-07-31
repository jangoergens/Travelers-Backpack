package com.tiviacz.travelersbackpack.network;

import com.tiviacz.travelersbackpack.TravelersBackpack;
import com.tiviacz.travelersbackpack.capability.AttachmentUtils;
import com.tiviacz.travelersbackpack.inventory.menu.TravelersBackpackBlockEntityMenu;
import com.tiviacz.travelersbackpack.inventory.menu.TravelersBackpackItemMenu;
import com.tiviacz.travelersbackpack.inventory.sorter.SlotManager;
import com.tiviacz.travelersbackpack.util.Reference;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record ServerboundSlotPacket(byte screenID, boolean isActive, List<Integer> selectedSlots) implements CustomPacketPayload
{
    public static final Type<ServerboundSlotPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(TravelersBackpack.MODID, "slot"));

    public static final StreamCodec<FriendlyByteBuf, ServerboundSlotPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BYTE, ServerboundSlotPacket::screenID,
            ByteBufCodecs.BOOL, ServerboundSlotPacket::isActive,
            ByteBufCodecs.INT.apply(ByteBufCodecs.list()), ServerboundSlotPacket::selectedSlots,
            ServerboundSlotPacket::new
    );


    public static void handle(final ServerboundSlotPacket message, IPayloadContext ctx)
    {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();

            if(player instanceof ServerPlayer serverPlayer)
            {
                if(message.screenID == Reference.WEARABLE_SCREEN_ID)
                {
                    SlotManager manager = AttachmentUtils.getBackpackInv(serverPlayer).getSlotManager();
                    manager.setSelectorActive(SlotManager.UNSORTABLE, message.isActive);
                    manager.setUnsortableSlots(message.selectedSlots, true);
                    manager.setSelectorActive(SlotManager.UNSORTABLE, !message.isActive);
                }
                if(message.screenID == Reference.ITEM_SCREEN_ID)
                {
                    SlotManager manager = ((TravelersBackpackItemMenu)serverPlayer.containerMenu).container.getSlotManager();
                    manager.setSelectorActive(SlotManager.UNSORTABLE, message.isActive);
                    manager.setUnsortableSlots(message.selectedSlots, true);
                    manager.setSelectorActive(SlotManager.UNSORTABLE, !message.isActive);
                }
                if(message.screenID == Reference.BLOCK_ENTITY_SCREEN_ID)
                {
                    SlotManager manager = ((TravelersBackpackBlockEntityMenu)serverPlayer.containerMenu).container.getSlotManager();
                    manager.setSelectorActive(SlotManager.UNSORTABLE, message.isActive);
                    manager.setUnsortableSlots(message.selectedSlots, true);
                    manager.setSelectorActive(SlotManager.UNSORTABLE, !message.isActive);
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}