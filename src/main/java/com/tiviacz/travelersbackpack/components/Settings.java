package com.tiviacz.travelersbackpack.components;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record Settings(List<List<Byte>> settings)
{
    public static final Codec<List<List<Byte>>> CODEC = Codec.list(Codec.list(Codec.BYTE));
    public static final PacketCodec<ByteBuf, List<List<Byte>>> PACKET_CODEC = PacketCodecs.BYTE.collect(PacketCodecs.toList()).collect(PacketCodecs.toList());

    public static List<List<Byte>> createSettings(List<Byte>... lists)
    {
        List<List<Byte>> list = new ArrayList<>(Arrays.asList(lists));
        return list;
    }

    public static List<Byte> createDefaultToolSettings()
    {
        return Arrays.asList((byte)0);
    }
}
