package com.tiviacz.travelersbackpack.util;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.SoundActions;

public class FluidUtils
{
    public static SoundEvent getFluidEmptySound(Fluid fluid)
    {
        SoundEvent soundevent = fluid.getFluidType().getSound(SoundActions.BUCKET_EMPTY);

        if(soundevent == null)
        {
            soundevent = fluid.is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
        }

        return soundevent;
    }

    public static SoundEvent getFluidFillSound(Fluid fluid)
    {
        SoundEvent soundevent = fluid.getFluidType().getSound(SoundActions.BUCKET_FILL);

        if(soundevent == null)
        {
            soundevent = fluid.is(FluidTags.LAVA) ? SoundEvents.BUCKET_FILL_LAVA : SoundEvents.BUCKET_FILL;
        }

        return soundevent;
    }

    //#TODO no possible conversion
   /* public static void setFluidStackNBT(ItemStack stack, FluidStack fluidStack)
    {
        if(stack.getTag() != null)
        {
            fluidStack.setTag(stack.getTag());
        }
    }

    public static Potion getPotionTypeFromFluidStack(FluidStack fluidStack)
    {
        return PotionUtils.getPotion(fluidStack.getTag());
    }

    public static ItemStack getItemStackFromFluidStack(FluidStack fluidStack)
    {
        return PotionUtils.setPotion(new ItemStack(Items.POTION), getPotionTypeFromFluidStack(fluidStack));
    }

    public static ItemStack getItemStackFromPotionType(Potion potion)
    {
        return PotionContents.createItemStack(Items.POTION, potion);
        //return PotionUtils.setPotion(new ItemStack(Items.POTION), potion);
    } */
}