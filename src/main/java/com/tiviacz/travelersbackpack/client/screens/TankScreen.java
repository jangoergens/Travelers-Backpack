package com.tiviacz.travelersbackpack.client.screens;

import com.tiviacz.travelersbackpack.util.RenderUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.ArrayList;
import java.util.List;

public class TankScreen
{
    private final int height;
    private final int width;
    private final int startX;
    private final int startY;
    private final FluidTank tank;

    public TankScreen(FluidTank tank, int x, int y, int height, int width)
    {
        this.startX = x;
        this.startY = y;
        this.height = height;
        this.width = width;
        this.tank = tank;
    }

    public List<Component> getTankTooltip(Level level)
    {
        FluidStack fluidStack = tank.getFluid();
        List<Component> tankTips = new ArrayList<>();
        String fluidName = !fluidStack.isEmpty() ? fluidStack.getDisplayName().getString() : I18n.get("screen.travelersbackpack.none");
        String fluidAmount = !fluidStack.isEmpty() ? fluidStack.getAmount() + "/" + tank.getCapacity() : I18n.get("screen.travelersbackpack.empty");

        /*if(!fluidStack.isEmpty())
        {
            if(fluidStack.getTag() != null)
            {
                if(fluidStack.getTag().contains("Potion"))
                {
                    fluidName = null;
                    //setPotionDescription(PotionUtils.getMobEffects(FluidUtils.getItemStackFromFluidStack(fluidStack)), tankTips, level);
                }
            }
        } */

        if(fluidName != null) tankTips.add(Component.literal(fluidName));
        tankTips.add(Component.literal(fluidAmount));

        return tankTips;
    }

    public void drawScreenFluidBar(TravelersBackpackScreen screen, GuiGraphics guiGraphics)
    {
        RenderUtils.renderScreenTank(guiGraphics, tank, screen.getGuiLeft() + this.startX, screen.getGuiTop() + this.startY, 0, this.height, this.width);
    }

    public boolean inTank(TravelersBackpackScreen screen, int mouseX, int mouseY)
    {
        return screen.getGuiLeft() + startX <= mouseX && mouseX <= startX + width + screen.getGuiLeft() && startY + screen.getGuiTop() <= mouseY && mouseY <= startY + height + screen.getGuiTop();
    }
}