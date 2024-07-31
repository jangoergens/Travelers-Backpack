package com.tiviacz.travelersbackpack.client.renderer;

import com.tiviacz.travelersbackpack.components.FluidTanks;
import com.tiviacz.travelersbackpack.init.ModDataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class RenderData
{
    private final ItemStack stack;
    private final FluidTank leftTank = new FluidTank(3000); //Will be changed anyway later
    private final FluidTank rightTank = new FluidTank(3000);

    public RenderData(ItemStack stack, boolean loadData)
    {
        this.stack = stack;

        if(loadData)
        {
            this.loadDataFromStack();
        }
    }

    public FluidTank getLeftTank()
    {
        return this.leftTank;
    }

    public FluidTank getRightTank()
    {
        return this.rightTank;
    }

    public ItemStack getItemStack()
    {
        return this.stack;
    }

    public int getSleepingBagColor()
    {
        return stack.getOrDefault(ModDataComponents.SLEEPING_BAG_COLOR, DyeColor.RED.getId());
    }

    public void loadDataFromStack()
    {
        if(this.stack.has(ModDataComponents.FLUID_TANKS))
        {
            loadTanks();
        }
    }

    public void loadTanks()
    {
        FluidTanks tanks = stack.get(ModDataComponents.FLUID_TANKS);

        this.leftTank.setCapacity(tanks.capacity());
        this.leftTank.setFluid(tanks.leftFluidStack());

        this.rightTank.setCapacity(tanks.capacity());
        this.rightTank.setFluid(tanks.rightFluidStack());
    }
}