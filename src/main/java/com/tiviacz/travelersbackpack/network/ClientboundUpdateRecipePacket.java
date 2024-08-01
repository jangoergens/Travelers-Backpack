package com.tiviacz.travelersbackpack.network;

import com.tiviacz.travelersbackpack.client.screens.TravelersBackpackScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;

public class ClientboundUpdateRecipePacket
{
    public static final ResourceLocation NULL = ResourceLocation.fromNamespaceAndPath("null", "null");

    private final ResourceLocation recipeId;
    private final ItemStack output;

    public ClientboundUpdateRecipePacket(RecipeHolder recipe, ItemStack output)
    {
        this.recipeId = recipe == null ? NULL : recipe.id();
        this.output = output;
    }

    public ClientboundUpdateRecipePacket(ResourceLocation recipeId, ItemStack output)
    {
        this.recipeId = recipeId;
        this.output = output;
    }

    public static ClientboundUpdateRecipePacket decode(final RegistryFriendlyByteBuf buffer)
    {
        ResourceLocation recipeId = ResourceLocation.parse(buffer.readUtf());

        return new ClientboundUpdateRecipePacket(recipeId, recipeId.equals(NULL) ? ItemStack.EMPTY : ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer));
    }

    public static void encode(final ClientboundUpdateRecipePacket message, final RegistryFriendlyByteBuf buffer)
    {
        buffer.writeUtf(message.recipeId.toString());
        if(!message.recipeId.equals(NULL))
        {
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, message.output);
           // buffer.writeItem(message.output);
        }
    }

    public static void handle(final ClientboundUpdateRecipePacket message, final CustomPayloadEvent.Context ctx)
    {
        ctx.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
        {
            RecipeHolder<?> recipe = Minecraft.getInstance().level.getRecipeManager().byKey(message.recipeId).orElse(null);

            if(Minecraft.getInstance().screen instanceof TravelersBackpackScreen screen)
            {
                screen.getMenu().resultSlots.setRecipeUsed(recipe);
                screen.getMenu().resultSlots.setItem(0, message.output);
            }
        }));

        ctx.setPacketHandled(true);
    }
}