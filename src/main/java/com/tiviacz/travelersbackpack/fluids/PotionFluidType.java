package com.tiviacz.travelersbackpack.fluids;

import com.tiviacz.travelersbackpack.TravelersBackpack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.function.Consumer;

public class PotionFluidType extends FluidType
{
    public static final ResourceLocation POTION_STILL_RL = ResourceLocation.fromNamespaceAndPath(TravelersBackpack.MODID, "block/potion_still");
    public static final ResourceLocation POTION_FLOW_RL = ResourceLocation.fromNamespaceAndPath(TravelersBackpack.MODID, "block/potion_flow");

    public PotionFluidType(Properties properties)
    {
        super(properties);
    }

    @Override
    public Component getDescription(FluidStack stack)
    {
        return Component.translatable(this.getDescriptionId(stack));
    }

    @Override
    public String getDescriptionId(FluidStack stack)
    {
        return Potion.getName(stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion(), this.getDescriptionId() + ".effect.");
    }

    @Override
    public String getDescriptionId()
    {
        return "item.minecraft.potion.effect.empty";
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer)
    {
        consumer.accept(new IClientFluidTypeExtensions()
        {
            private static final int EMPTY_COLOR = 0xf800f8;

            @Override
            public int getTintColor()
            {
                return EMPTY_COLOR | 0xFF000000;
            }

            @Override
            public int getTintColor(FluidStack stack)
            {
                return stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).getColor();
            }

            @Override
            public ResourceLocation getStillTexture() {
                return POTION_STILL_RL;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return POTION_FLOW_RL;
            }
        });
    }
}