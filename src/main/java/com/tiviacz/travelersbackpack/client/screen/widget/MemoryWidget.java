package com.tiviacz.travelersbackpack.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import com.tiviacz.travelersbackpack.client.screen.TravelersBackpackHandledScreen;
import com.tiviacz.travelersbackpack.init.ModNetwork;
import com.tiviacz.travelersbackpack.inventory.sorter.InventorySorter;
import com.tiviacz.travelersbackpack.inventory.sorter.SlotManager;
import com.tiviacz.travelersbackpack.util.BackpackUtils;
import com.tiviacz.travelersbackpack.util.TextUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;

public class MemoryWidget extends WidgetBase
{
    public MemoryWidget(TravelersBackpackHandledScreen screen, int x, int y, int width, int height)
    {
        super(screen, x, y, width, height);
        this.isVisible = false;
        this.showTooltip = true;
    }

    @Override
    protected void drawBackground(MatrixStack matrixStack, MinecraftClient minecraft, int mouseX, int mouseY)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TravelersBackpackHandledScreen.SETTINGS_TRAVELERS_BACKPACK);

        if(isVisible())
        {
            drawTexture(matrixStack, x, y, 16, isWidgetActive ? 19 : 0, width, height);
        }
    }

    @Override
    public void drawMouseoverTooltip(MatrixStack matrixStack, int mouseX, int mouseY)
    {
        if(isMouseOver(mouseX, mouseY) && showTooltip)
        {
            //String[] s =  I18n.translate("screen.travelersbackpack.memory").split("\n");
            //screen.renderTooltip(matrixStack, List.of(new LiteralText(s[0]), new LiteralText(s[1])), mouseX, mouseY);
            screen.renderTooltip(matrixStack, TextUtils.getTranslatedSplittedText("screen.travelersbackpack.memory", null), mouseX, mouseY);
        }
    }

    @Override
    public void setWidgetStatus(boolean status)
    {
        super.setWidgetStatus(status);
        screen.sortWidget.setTooltipVisible(!status);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton)
    {
        if(!this.screen.settingsWidget.isWidgetActive()) return false;

        if(screen.inventory.getSlotManager().isSelectorActive(SlotManager.UNSORTABLE))
        {
            return false;
        }

        if(isMouseOver(pMouseX, pMouseY))
        {
            setWidgetStatus(!this.isWidgetActive);

            //Turns slot checking on server
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeByte(screen.inventory.getScreenID()).writeByte(InventorySorter.MEMORY).writeBoolean(BackpackUtils.isShiftPressed());

            ClientPlayNetworking.send(ModNetwork.SORTER_ID, buf);

            //Turns slot checking on client
            PacketByteBuf buf2 = PacketByteBufs.copy(PacketByteBufs.create().writeByte(screen.inventory.getScreenID()).writeBoolean(screen.inventory.getSlotManager().isSelectorActive(SlotManager.MEMORY))).writeIntArray(screen.inventory.getSlotManager().getMemorySlots().stream().mapToInt(Pair::getFirst).toArray());

            for(ItemStack stack : screen.inventory.getSlotManager().getMemorySlots().stream().map(Pair::getSecond).toArray(ItemStack[]::new))
            {
                buf2.writeItemStack(stack);
            }

            ClientPlayNetworking.send(ModNetwork.MEMORY_ID, buf2);
            screen.inventory.getSlotManager().setSelectorActive(SlotManager.MEMORY, !screen.inventory.getSlotManager().isSelectorActive(SlotManager.MEMORY));

            screen.playUIClickSound();
            return true;
        }
        return false;
    }
}