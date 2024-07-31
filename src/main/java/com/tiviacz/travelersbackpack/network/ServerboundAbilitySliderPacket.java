package com.tiviacz.travelersbackpack.network;

import com.tiviacz.travelersbackpack.TravelersBackpack;
import com.tiviacz.travelersbackpack.capability.AttachmentUtils;
import com.tiviacz.travelersbackpack.common.ServerActions;
import com.tiviacz.travelersbackpack.inventory.menu.TravelersBackpackBlockEntityMenu;
import com.tiviacz.travelersbackpack.util.Reference;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerboundAbilitySliderPacket(byte screenID, boolean sliderValue) implements CustomPacketPayload
{
    public static final Type<ServerboundAbilitySliderPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(TravelersBackpack.MODID, "ability_slider"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundAbilitySliderPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BYTE, ServerboundAbilitySliderPacket::screenID,
            ByteBufCodecs.BOOL, ServerboundAbilitySliderPacket::sliderValue,
            ServerboundAbilitySliderPacket::new
    );

    public static void handle(final ServerboundAbilitySliderPacket message, IPayloadContext ctx)
    {
        ctx.enqueueWork(() -> {
            final Player player = ctx.player();

            if(player instanceof ServerPlayer serverPlayer)
            {
                if(message.screenID == Reference.WEARABLE_SCREEN_ID && AttachmentUtils.isWearingBackpack(serverPlayer))
                {
                    ServerActions.switchAbilitySlider(serverPlayer, message.sliderValue);
                }
                else if(message.screenID == Reference.BLOCK_ENTITY_SCREEN_ID && serverPlayer.containerMenu instanceof TravelersBackpackBlockEntityMenu menu)
                {
                    ServerActions.switchAbilitySliderBlockEntity(serverPlayer, menu.container.getPosition(), message.sliderValue);
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