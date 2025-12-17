package dev.creoii.greatbigworld.thealterworld.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

public record TranslucentStillParticleOptions(ParticleType<TranslucentStillParticleOptions> type, Identifier sprite, int lifetime, Vec3 rotation) implements ParticleOptions {
    public TranslucentStillParticleOptions(Identifier sprite, int lifetime, Vec3 rotation) {
        this(TheAlterworldParticleTypes.TRANSLUCENT_STILL, sprite, lifetime, rotation);
    }

    public TranslucentStillParticleOptions(Identifier sprite, int lifetime) {
        this(TheAlterworldParticleTypes.TRANSLUCENT_STILL, sprite, lifetime, Vec3.ZERO);
    }

    public static MapCodec<TranslucentStillParticleOptions> codec(ParticleType<TranslucentStillParticleOptions> particleType) {
        return RecordCodecBuilder.mapCodec(instance -> {
            return instance.group(
                    Identifier.CODEC.fieldOf("sprite").forGetter(particleOptions -> particleOptions.sprite),
                    Codec.INT.fieldOf("lifetime").forGetter(particleOptions -> particleOptions.lifetime),
                    Vec3.CODEC.fieldOf("rotation").orElse(Vec3.ZERO).forGetter(particleOptions -> particleOptions.rotation)
            ).apply(instance, (sprite, lifetime, rotation) -> new TranslucentStillParticleOptions(particleType, sprite, lifetime, rotation));
        });
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, TranslucentStillParticleOptions> streamCodec(ParticleType<TranslucentStillParticleOptions> particleType) {
        return ByteBufCodecs.fromCodec(codec(particleType).codec());
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }
}
