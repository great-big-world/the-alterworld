package dev.creoii.greatbigworld.thealterworld.client;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.thealterworld.block.ReinforcedDeepslateBlock;
import dev.creoii.greatbigworld.thealterworld.client.particle.TranslucentStillParticle;
import dev.creoii.greatbigworld.thealterworld.client.render.AncientPedestalBlockEntityRenderer;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlockEntityTypes;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldParticleTypes;
import dev.creoii.greatbigworld.thealterworld.util.TranslucentStillParticleOptions;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

public class TheAlterworldClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.putBlocks(ChunkSectionLayer.TRANSLUCENT, TheAlterworldBlocks.ALTERWORLD_PORTAL, TheAlterworldBlocks.REINFORCED_DEEPSLATE);
        BlockRenderLayerMap.putBlock(TheAlterworldBlocks.ANCIENT_PEDESTAL, ChunkSectionLayer.CUTOUT);

        BlockEntityRenderers.register(TheAlterworldBlockEntityTypes.ANCIENT_PEDESTAL, ctx -> new AncientPedestalBlockEntityRenderer(ctx.itemModelResolver()));

        ParticleFactoryRegistry.getInstance().register(TheAlterworldParticleTypes.TRANSLUCENT_STILL, TranslucentStillParticle.Factory.INSTANCE);

        ClientPlayNetworking.registerGlobalReceiver(ReinforcedDeepslateBlock.FractureS2C.PACKET_ID, (fractureS2C, context) -> {
            int lifetime = fractureS2C.lifetime();
            Vec3 vec3 = fractureS2C.vec3();
            String rune = fractureS2C.rune();
            context.client().execute(() -> {
                Identifier particleId = Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "rune/" + rune);
                context.client().level.addParticle(new TranslucentStillParticleOptions(particleId, lifetime, new Vec3(0d, 180d, 0d)), vec3.x + .5d, vec3.y, vec3.z - .001d, 0f, 0f, 0f);
                context.client().level.addParticle(new TranslucentStillParticleOptions(particleId, lifetime), vec3.x + .5d, vec3.y, vec3.z + 1.001d, 0f, 0f, 0f);
                context.client().level.addParticle(new TranslucentStillParticleOptions(particleId, lifetime, new Vec3(0d, 270, 0d)), vec3.x - .001d, vec3.y, vec3.z + .5d, 0f, 0f, 0f);
                context.client().level.addParticle(new TranslucentStillParticleOptions(particleId, lifetime, new Vec3(0d, 90, 0d)), vec3.x + 1.001d, vec3.y, vec3.z + .5d, 0f, 0f, 0f);
            });
        });
    }
}
