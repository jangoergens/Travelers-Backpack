package com.tiviacz.travelersbackpack.inventory;

public interface ITanks
{
    FluidTank getLeftTank();

    FluidTank getRightTank();

    boolean updateTankSlots();
}