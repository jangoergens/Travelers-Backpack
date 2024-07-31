package com.tiviacz.travelersbackpack.network;

import com.tiviacz.travelersbackpack.TravelersBackpack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientboundSyncItemStackPacket(int entityId, ItemStack stack) implements CustomPacketPayload
{
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(TravelersBackpack.MODID, "sync_itemstack");
    public static final Type<ClientboundSyncItemStackPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSyncItemStackPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ClientboundSyncItemStackPacket::entityId,
            ItemStack.OPTIONAL_STREAM_CODEC, ClientboundSyncItemStackPacket::stack,
            ClientboundSyncItemStackPacket::new
    );

    public static void handle(final ClientboundSyncItemStackPacket message, IPayloadContext ctx)
    {
        if(ctx.flow().isClientbound())
        {
            ctx.enqueueWork(() -> {

                Player player = (Player)Minecraft.getInstance().player.level().getEntity(message.entityId());

                if(player != null && player.getMainHandItem().getItem() == message.stack().getItem())
                {
                    player.getMainHandItem().applyComponents(message.stack().getComponentsPatch());
                }
            });
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}