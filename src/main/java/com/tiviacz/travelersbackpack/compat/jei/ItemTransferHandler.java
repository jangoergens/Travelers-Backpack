package com.tiviacz.travelersbackpack.compat.jei;

import com.tiviacz.travelersbackpack.inventory.SettingsManager;
import com.tiviacz.travelersbackpack.inventory.screen.TravelersBackpackItemScreenHandler;
import com.tiviacz.travelersbackpack.network.SettingsPacket;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IStackHelper;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import mezz.jei.common.network.IConnectionToServer;
import mezz.jei.library.transfer.BasicRecipeTransferHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import org.jetbrains.annotations.Nullable;

public class ItemTransferHandler extends BasicRecipeTransferHandler<TravelersBackpackItemScreenHandler, RecipeEntry<CraftingRecipe>>
{
    public ItemTransferHandler(IConnectionToServer serverConnection, IStackHelper stackHelper, IRecipeTransferHandlerHelper handlerHelper, IRecipeTransferInfo<TravelersBackpackItemScreenHandler, RecipeEntry<CraftingRecipe>> transferInfo)
    {
        super(serverConnection, stackHelper, handlerHelper, transferInfo);
    }

    @Nullable
    @Override
    public IRecipeTransferError transferRecipe(TravelersBackpackItemScreenHandler container, RecipeEntry<CraftingRecipe> recipe, IRecipeSlotsView recipeSlotsView, PlayerEntity player, boolean maxTransfer, boolean doTransfer)
    {
        if(doTransfer)
        {
            container.inventory.getSettingsManager().set(SettingsManager.CRAFTING, SettingsManager.SHOW_CRAFTING_GRID, (byte)1);

            //PacketByteBuf buf = PacketByteBufs.create();
            //buf.writeByte(container.inventory.getScreenID()).writeByte(SettingsManager.CRAFTING).writeInt(SettingsManager.SHOW_CRAFTING_GRID).writeByte((byte)1);

            //ClientPlayNetworking.send(ModNetwork.SETTINGS_ID, buf);

            ClientPlayNetworking.send(new SettingsPacket(container.inventory.getScreenID(), SettingsManager.CRAFTING, SettingsManager.SHOW_CRAFTING_GRID, (byte)1));
        }
        return super.transferRecipe(container, recipe, recipeSlotsView, player, maxTransfer, doTransfer);
    }
}