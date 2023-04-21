package com.tiviacz.travelersbackpack.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.tiviacz.travelersbackpack.client.screen.TravelersBackpackHandledScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;

public abstract class WidgetBase extends DrawableHelper implements Drawable, Element
{
    public final TravelersBackpackHandledScreen screen;
    protected int x;
    protected int y;
    protected int zOffset = 0;
    protected int width;
    protected int height;
    protected boolean isHovered;
    protected boolean isWidgetActive = false;
    protected boolean isVisible;
    protected boolean showTooltip;

    public WidgetBase(TravelersBackpackHandledScreen screen, int x, int y, int width, int height)
    {
        this.screen = screen;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        isHovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;

        if(zOffset != 0)
        {
            matrixStack.push();
            matrixStack.translate(0, 0, zOffset);
        }

        RenderSystem.enableDepthTest();
        drawBackground(matrixStack, MinecraftClient.getInstance(), mouseX, mouseY);
        drawMouseoverTooltip(matrixStack, mouseX, mouseY);

        if(zOffset != 0)
        {
            matrixStack.pop();
        }
    }

    abstract void drawBackground(MatrixStack matrixStack, MinecraftClient minecraft, int mouseX, int mouseY);

    abstract void drawMouseoverTooltip(MatrixStack matrixStack, int mouseX, int mouseY);

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton)
    {
        if(isHovered)
        {
            setWidgetStatus(!this.isWidgetActive);
            this.screen.playUIClickSound();
            return true;
        }
        return false;
    }

    public void setWidgetStatus(boolean status)
    {
        this.isWidgetActive = status;
    }

    public boolean isWidgetActive()
    {
        return this.isWidgetActive;
    }

    public boolean isVisible()
    {
        return this.isVisible;
    }

    public void setVisible(boolean visibility)
    {
        this.isVisible = visibility;
    }

    public void setTooltipVisible(boolean visible)
    {
        this.showTooltip = visible;
    }

    public boolean isSettingsChild()
    {
        return true;
    }

    public int[] getWidgetSizeAndPos()
    {
        int[] size = new int[4];
        size[0] = x;
        size[1] = y;
        size[2] = width;
        size[3] = height;
        return size;
    }
}