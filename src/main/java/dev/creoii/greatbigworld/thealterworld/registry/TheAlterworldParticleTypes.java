package dev.creoii.greatbigworld.thealterworld.registry;

import com.mojang.serialization.MapCodec;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.thealterworld.util.TranslucentStillParticleOptions;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public final class TheAlterworldParticleTypes {
    public static ParticleType<TranslucentStillParticleOptions> TRANSLUCENT_STILL;

    public static void register() {
        TRANSLUCENT_STILL = Registry.register(BuiltInRegistries.PARTICLE_TYPE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "translucent_still"), new ParticleType<TranslucentStillParticleOptions>(false) {
            @Override
            public MapCodec<TranslucentStillParticleOptions> codec() {
                return TranslucentStillParticleOptions.codec(this);
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, TranslucentStillParticleOptions> streamCodec() {
                return TranslucentStillParticleOptions.streamCodec(this);
            }
        });
    }
}
