package com.tiviacz.travelersbackpack.network;

import com.tiviacz.travelersbackpack.TravelersBackpack;
import com.tiviacz.travelersbackpack.capability.AttachmentUtils;
import com.tiviacz.travelersbackpack.inventory.SettingsManager;
import com.tiviacz.travelersbackpack.inventory.menu.TravelersBackpackBlockEntityMenu;
import com.tiviacz.travelersbackpack.inventory.menu.TravelersBackpackItemMenu;
import com.tiviacz.travelersbackpack.util.Reference;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerboundSettingsPacket(byte screenID, byte dataArray, int place, byte value) implements CustomPacketPayload
{
    public static final Type<ServerboundSettingsPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(TravelersBackpack.MODID, "settings"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundSettingsPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BYTE, ServerboundSettingsPacket::screenID,
            ByteBufCodecs.BYTE, ServerboundSettingsPacket::dataArray,
            ByteBufCodecs.INT, ServerboundSettingsPacket::place,
            ByteBufCodecs.BYTE, ServerboundSettingsPacket::value,
            ServerboundSettingsPacket::new
    );

    public static void handle(final ServerboundSettingsPacket message, IPayloadContext ctx)
    {
        ctx.enqueueWork(() ->
        {
            Player player = ctx.player();

            if(player instanceof ServerPlayer serverPlayer)
            {
                if(message.screenID == Reference.WEARABLE_SCREEN_ID)
                {
                    SettingsManager manager = AttachmentUtils.getBackpackInv(serverPlayer).getSettingsManager();
                    manager.set(message.dataArray, message.place, message.value);
                }
                if(message.screenID == Reference.ITEM_SCREEN_ID)
                {
                    SettingsManager manager = ((TravelersBackpackItemMenu)serverPlayer.containerMenu).container.getSettingsManager();
                    manager.set(message.dataArray, message.place, message.value);
                }
                if(message.screenID == Reference.BLOCK_ENTITY_SCREEN_ID)
                {
                    SettingsManager manager = ((TravelersBackpackBlockEntityMenu)serverPlayer.containerMenu).container.getSettingsManager();
                    manager.set(message.dataArray, message.place, message.value);
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
