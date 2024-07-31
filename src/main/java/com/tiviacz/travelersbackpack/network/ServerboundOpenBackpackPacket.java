package com.tiviacz.travelersbackpack.network;

import com.tiviacz.travelersbackpack.TravelersBackpack;
import com.tiviacz.travelersbackpack.inventory.TravelersBackpackContainer;
import com.tiviacz.travelersbackpack.items.TravelersBackpackItem;
import com.tiviacz.travelersbackpack.util.Reference;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerboundOpenBackpackPacket(int index) implements CustomPacketPayload
{
    public static final Type<ServerboundOpenBackpackPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(TravelersBackpack.MODID, "open_backpack"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundOpenBackpackPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ServerboundOpenBackpackPacket::index,
            ServerboundOpenBackpackPacket::new
    );

    public static void handle(final ServerboundOpenBackpackPacket message, IPayloadContext ctx)
    {
        ctx.enqueueWork(() -> {

            Player player = ctx.player();

            if(player instanceof ServerPlayer serverPlayer)
            {
                Slot slot = serverPlayer.containerMenu.getSlot(message.index);

                if(slot != null && slot.hasItem() && slot.getItem().getItem() instanceof TravelersBackpackItem)
                {
                    serverPlayer.closeContainer();
                    TravelersBackpackContainer.openGUI(serverPlayer, slot.getItem(), Reference.ITEM_SCREEN_ID);
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
