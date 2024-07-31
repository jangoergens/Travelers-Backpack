package com.tiviacz.travelersbackpack.components;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

import java.util.List;

public record Slots(List<Integer> unsortables, List<Pair<Integer, ItemStack>> memory)
{
    public static final Codec<Slots> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                            Codec.INT.listOf().fieldOf("unsortables").forGetter(Slots::unsortables),
                            Codec.mapPair(Codec.INT.fieldOf("index"), ItemStack.OPTIONAL_CODEC.fieldOf("item")).codec().listOf().fieldOf("memory").forGetter(Slots::memory))
                    .apply(instance, Slots::new)
    );

    public static final PacketCodec<RegistryByteBuf, Slots> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER.collect(PacketCodecs.toList()), Slots::unsortables,
            PacketCodecs.registryCodec(Codec.mapPair(Codec.INT.fieldOf("index"), ItemStack.OPTIONAL_CODEC.fieldOf("item")).codec()).collect(PacketCodecs.toList()), Slots::memory,
            Slots::new
    );

    public static Slots createDefault()
    {
        return new Slots(List.of(), List.of());
    }
}