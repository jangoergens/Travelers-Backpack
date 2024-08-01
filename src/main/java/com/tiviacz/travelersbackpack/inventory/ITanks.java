package com.tiviacz.travelersbackpack.inventory;

import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface ITanks
{
    FluidTank getLeftTank();

    FluidTank getRightTank();

    boolean updateTankSlots();
}