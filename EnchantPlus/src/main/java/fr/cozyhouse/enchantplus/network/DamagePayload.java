package fr.cozyhouse.enchantplus.network;

import fr.cozyhouse.enchantplus.Enchantplus;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public record DamagePayload(float damage) implements CustomPacketPayload {

    public static final Type<@NotNull DamagePayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(Enchantplus.MOD_ID, "damage"));

    public static final StreamCodec<ByteBuf, DamagePayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.FLOAT, DamagePayload::damage,
                    DamagePayload::new
            );

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return TYPE;
    }
}
