package com.tiviacz.travelersbackpack.compat.craftingtweaks;

import com.tiviacz.travelersbackpack.TravelersBackpack;
import com.tiviacz.travelersbackpack.client.screens.TravelersBackpackScreen;
import com.tiviacz.travelersbackpack.client.screens.widgets.CraftingWidget;
import net.blay09.mods.craftingtweaks.CraftingTweaksProviderManager;
import net.blay09.mods.craftingtweaks.api.CraftingTweaksClientAPI;
import net.blay09.mods.craftingtweaks.api.TweakType;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.inventory.Slot;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TravelersBackpackCraftingGridAddition implements ICraftingTweaks
{
    @OnlyIn(Dist.CLIENT)
    private TravelersBackpackScreen screen;

    private static final Method ADD_RENDERABLE_WIDGET = ObfuscationReflectionHelper.findMethod(Screen.class, "addRenderableWidget", GuiEventListener.class);
    private final List<AbstractWidget> widgets = new ArrayList<>();

    public static void registerCraftingTweaksAddition()
    {
        CraftingWidget.setCraftingTweaksAddition(new TravelersBackpackCraftingGridAddition());
    }

    @OnlyIn(Dist.CLIENT)
    private void addButton(AbstractWidget widget)
    {
        widgets.add(widget);
        try {
            ADD_RENDERABLE_WIDGET.invoke(screen, widget);
        }
        catch(IllegalAccessException | InvocationTargetException e) {
            TravelersBackpack.LOGGER.error("Error calling addButton in Screen class", e);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onCraftingSlotsHidden()
    {
        if(widgets.isEmpty())
        {
            return;
        }

        List<GuiEventListener> screenChildren = ObfuscationReflectionHelper.getPrivateValue(Screen.class, screen, "children");
        List<AbstractWidget> screenRenderables = ObfuscationReflectionHelper.getPrivateValue(Screen.class, screen, "renderables");
        if(screenChildren == null || screenRenderables == null)
        {
            return;
        }

        widgets.forEach(screenChildren::remove);
        widgets.forEach(screenRenderables::remove);
        widgets.clear();
    }

    @Override
    public void onCraftingSlotsDisplayed()
    {
        Slot thirdSlot = screen.getMenu().getSlot(screen.container.getCombinedHandler().getSlots() - 6);
        CraftingTweaksProviderManager.getDefaultCraftingGrid(screen.getMenu()).ifPresent(craftingGrid -> {
            addButton(CraftingTweaksClientAPI.createTweakButtonRelative(craftingGrid, screen, getButtonX(thirdSlot), getButtonY(thirdSlot, 0), TweakType.Rotate));
            addButton(CraftingTweaksClientAPI.createTweakButtonRelative(craftingGrid, screen, getButtonX(thirdSlot), getButtonY(thirdSlot, 1), TweakType.Balance));
            addButton(CraftingTweaksClientAPI.createTweakButtonRelative(craftingGrid, screen, getButtonX(thirdSlot), getButtonY(thirdSlot, 2), TweakType.Clear));
        });
    }

    @Override
    public void setScreen(TravelersBackpackScreen screen)
    {
        this.screen = screen;
    }

    @OnlyIn(Dist.CLIENT)
    private int getButtonX(Slot thirdSlot)
    {
        return thirdSlot.x + 19;
    }

    @OnlyIn(Dist.CLIENT)
    private int getButtonY(Slot thirdSlot, int index)
    {
        return thirdSlot.y + 18 * index;
    }
}