package com.tiviacz.travelersbackpack.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tiviacz.travelersbackpack.inventory.Tiers;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.fluid.Fluids;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record FluidTanks(long capacity, FluidTanks.Tank leftTank, FluidTanks.Tank rightTank)
{
    public static final Codec<FluidTanks> CODEC;
    public static final PacketCodec<RegistryByteBuf, FluidTanks> PACKET_CODEC;

   /* public static final Codec<FluidTanks> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("capacity").forGetter(FluidTanks::capacity),
                    FluidVariant.CODEC.fieldOf("leftFluidTank").forGetter(FluidTanks::leftFluidTank),
                    FluidVariant.CODEC.fieldOf("rightFluidTank").forGetter(FluidTanks::rightFluidTank)
            ).apply(instance, FluidTanks::new)
    );

    public static final PacketCodec<RegistryByteBuf, FluidTanks> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, FluidTanks::capacity,
            FluidVariant.PACKET_CODEC, FluidTanks::leftFluidVariant,
            FluidVariant.PACKET_CODEC, FluidTanks::rightFluidVariant,
            FluidTanks::new
    ); */

    static {
        CODEC = RecordCodecBuilder.create((instance) -> {
            return instance.group(Codec.LONG.fieldOf("capacity").forGetter(FluidTanks::capacity),
                    Tank.CODEC.fieldOf("leftTank").forGetter(FluidTanks::leftTank),
                    Tank.CODEC.fieldOf("rightTank").forGetter(FluidTanks::rightTank)).apply(instance, FluidTanks::new);
        });
        PACKET_CODEC = PacketCodec.tuple(PacketCodecs.VAR_LONG, FluidTanks::capacity, PacketCodecs.codec(Tank.CODEC), FluidTanks::leftTank, PacketCodecs.codec(Tank.CODEC), FluidTanks::rightTank, FluidTanks::new);
    }

    public static FluidTanks createTanks(long capacity)
    {
        return new FluidTanks(capacity, new Tank(FluidVariant.blank(), 0), new Tank(FluidVariant.blank(), 0));
    }

    public static FluidTanks createTanksForCreativeTab()
    {
        return new FluidTanks(Tiers.LEATHER.getTankCapacity(), new Tank(FluidVariant.of(Fluids.WATER), Tiers.LEATHER.getTankCapacity()), new Tank(FluidVariant.of(Fluids.LAVA), Tiers.LEATHER.getTankCapacity()));
    }

    public static record Tank(FluidVariant fluidVariant, long amount) {
        public static final Codec<Tank> CODEC = RecordCodecBuilder.create((instance) -> {
            return instance.group(FluidVariant.CODEC.fieldOf("fluidVariant").forGetter(Tank::fluidVariant), Codec.LONG.fieldOf("amount").forGetter(Tank::amount)).apply(instance, Tank::new);
        });

        public Tank(FluidVariant fluidVariant, long amount) {
            this.fluidVariant = fluidVariant;
            this.amount = amount;
        }

        public FluidVariant fluidVariant() {
            return this.fluidVariant;
        }

        public long amount() {
            return this.amount;
        }
    }
}