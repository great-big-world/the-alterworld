package dev.creoii.greatbigworld.thealterworld.client;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.thealterworld.block.ReinforcedDeepslateBlock;
import dev.creoii.greatbigworld.thealterworld.util.TranslucentStillParticleOptions;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

public final class TheAlterworldClientNetworking {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(ReinforcedDeepslateBlock.FractureS2C.PACKET_ID, (fractureS2C, context) -> {
            int lifetime = fractureS2C.lifetime();
            Vec3 vec3 = fractureS2C.vec3();
            String rune = fractureS2C.rune();
            context.client().execute(() -> {
                Identifier particleId = Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "rune/" + rune);
                context.client().level.addAlwaysVisibleParticle(new TranslucentStillParticleOptions(particleId, lifetime, new Vec3(0d, 180d, 0d)), vec3.x + .5d, vec3.y, vec3.z - .001d, 0f, 0f, 0f);
                context.client().level.addAlwaysVisibleParticle(new TranslucentStillParticleOptions(particleId, lifetime), vec3.x + .5d, vec3.y, vec3.z + 1.001d, 0f, 0f, 0f);
                context.client().level.addAlwaysVisibleParticle(new TranslucentStillParticleOptions(particleId, lifetime, new Vec3(0d, 270, 0d)), vec3.x - .001d, vec3.y, vec3.z + .5d, 0f, 0f, 0f);
                context.client().level.addAlwaysVisibleParticle(new TranslucentStillParticleOptions(particleId, lifetime, new Vec3(0d, 90, 0d)), vec3.x + 1.001d, vec3.y, vec3.z + .5d, 0f, 0f, 0f);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(ReinforcedDeepslateBlock.AncientGlowS2C.PACKET_ID, (fractureS2C, context) -> {
            Vec3 vec3 = fractureS2C.vec3();
            String rune = fractureS2C.rune();
            context.client().execute(() -> {
                Identifier particleId = Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "rune/" + rune);
                context.client().level.addAlwaysVisibleParticle(new TranslucentStillParticleOptions(particleId, 30, new Vec3(0d, 180d, 0d), false), vec3.x + .5d, vec3.y, vec3.z - .001d, 0f, 0f, 0f);
                context.client().level.addAlwaysVisibleParticle(new TranslucentStillParticleOptions(particleId, 30, false), vec3.x + .5d, vec3.y, vec3.z + 1.001d, 0f, 0f, 0f);
                context.client().level.addAlwaysVisibleParticle(new TranslucentStillParticleOptions(particleId, 30, new Vec3(0d, 270, 0d), false), vec3.x - .001d, vec3.y, vec3.z + .5d, 0f, 0f, 0f);
                context.client().level.addAlwaysVisibleParticle(new TranslucentStillParticleOptions(particleId, 30, new Vec3(0d, 90, 0d), false), vec3.x + 1.001d, vec3.y, vec3.z + .5d, 0f, 0f, 0f);
            });
        });
    }
}
