package com.tiviacz.travelersbackpack.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tiviacz.travelersbackpack.inventory.Tiers;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

public record FluidTanks(int capacity, FluidStack leftFluidStack, FluidStack rightFluidStack)
{
    public static final Codec<FluidTanks> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("capacity").forGetter(FluidTanks::capacity),
                    FluidStack.CODEC.fieldOf("leftFluidStack").forGetter(FluidTanks::leftFluidStack),
                    FluidStack.CODEC.fieldOf("rightFluidStack").forGetter(FluidTanks::rightFluidStack)
            ).apply(instance, FluidTanks::new)
    );

    public static final StreamCodec<ByteBuf, FluidTanks> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, FluidTanks::capacity,
            ByteBufCodecs.fromCodec(FluidStack.CODEC), FluidTanks::leftFluidStack,
            ByteBufCodecs.fromCodec(FluidStack.CODEC), FluidTanks::rightFluidStack,
            FluidTanks::new
    );

    public static FluidTanks createTanks(int capacity)
    {
        return new FluidTanks(capacity, FluidStack.EMPTY, FluidStack.EMPTY);
    }

    public static FluidTanks createTanksForCreativeTab()
    {
        return new FluidTanks(Tiers.LEATHER.getTankCapacity(), new FluidStack(Fluids.WATER, Tiers.LEATHER.getTankCapacity()), new FluidStack(Fluids.LAVA, Tiers.LEATHER.getTankCapacity()));
    }
}