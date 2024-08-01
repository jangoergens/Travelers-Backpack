package com.tiviacz.travelersbackpack.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class ClientboundSyncItemStackPacket
{
    private final int entityID;
    private final ItemStack stack;

    public ClientboundSyncItemStackPacket(int entityID, ItemStack stack)
    {
        this.entityID = entityID;
        this.stack = stack;
    }

    public static ClientboundSyncItemStackPacket decode(final RegistryFriendlyByteBuf buffer)
    {
        final int entityID = buffer.readInt();
        final ItemStack stack = ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer);

        return new ClientboundSyncItemStackPacket(entityID, stack);
    }

    public static void encode(final ClientboundSyncItemStackPacket message, final RegistryFriendlyByteBuf buffer)
    {
        buffer.writeInt(message.entityID);
        ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, message.stack);
    }

    public static void handle(final ClientboundSyncItemStackPacket message, CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() ->
        {
            Player player = (Player)Minecraft.getInstance().player.level().getEntity(message.entityID);

            if(player != null && player.getMainHandItem().getItem() == message.stack.getItem())
            {
                player.getMainHandItem().applyComponents(message.stack.getComponentsPatch());
            }
        });
        ctx.setPacketHandled(true);
    }
}
