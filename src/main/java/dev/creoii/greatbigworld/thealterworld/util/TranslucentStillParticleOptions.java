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

public record TranslucentStillParticleOptions(ParticleType<TranslucentStillParticleOptions> type, Identifier sprite, int lifetime, Vec3 rotation, boolean fade) implements ParticleOptions {
    public TranslucentStillParticleOptions(Identifier sprite, int lifetime, Vec3 rotation, boolean fade) {
        this(TheAlterworldParticleTypes.TRANSLUCENT_STILL, sprite, lifetime, rotation, fade);
    }

    public TranslucentStillParticleOptions(Identifier sprite, int lifetime, Vec3 rotation) {
        this(TheAlterworldParticleTypes.TRANSLUCENT_STILL, sprite, lifetime, rotation, true);
    }

    public TranslucentStillParticleOptions(Identifier sprite, int lifetime, boolean fade) {
        this(TheAlterworldParticleTypes.TRANSLUCENT_STILL, sprite, lifetime, Vec3.ZERO, fade);
    }

    public TranslucentStillParticleOptions(Identifier sprite, int lifetime) {
        this(TheAlterworldParticleTypes.TRANSLUCENT_STILL, sprite, lifetime, Vec3.ZERO, true);
    }

    public static MapCodec<TranslucentStillParticleOptions> codec(ParticleType<TranslucentStillParticleOptions> particleType) {
        return RecordCodecBuilder.mapCodec(instance -> {
            return instance.group(
                    Identifier.CODEC.fieldOf("sprite").forGetter(particleOptions -> particleOptions.sprite),
                    Codec.INT.fieldOf("lifetime").forGetter(particleOptions -> particleOptions.lifetime),
                    Vec3.CODEC.fieldOf("rotation").orElse(Vec3.ZERO).forGetter(particleOptions -> particleOptions.rotation),
                    Codec.BOOL.fieldOf("fade").orElse(true).forGetter(particleOptions -> particleOptions.fade)
            ).apply(instance, (sprite, lifetime, rotation, fade) -> new TranslucentStillParticleOptions(particleType, sprite, lifetime, rotation, fade));
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
