package dev.creoii.greatbigworld.thealterworld.client;

import dev.creoii.greatbigworld.thealterworld.client.particle.TranslucentStillParticle;
import dev.creoii.greatbigworld.thealterworld.client.render.AncientPedestalBlockEntityRenderer;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlockEntityTypes;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldParticleTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

public class TheAlterworldClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TheAlterworldClientNetworking.register();

        BlockRenderLayerMap.putBlocks(ChunkSectionLayer.TRANSLUCENT, TheAlterworldBlocks.ALTERWORLD_PORTAL, TheAlterworldBlocks.REINFORCED_DEEPSLATE);
        BlockRenderLayerMap.putBlock(TheAlterworldBlocks.ANCIENT_PEDESTAL, ChunkSectionLayer.CUTOUT);

        BlockEntityRenderers.register(TheAlterworldBlockEntityTypes.ANCIENT_PEDESTAL, ctx -> new AncientPedestalBlockEntityRenderer(ctx.itemModelResolver()));

        ParticleFactoryRegistry.getInstance().register(TheAlterworldParticleTypes.TRANSLUCENT_STILL, TranslucentStillParticle.Factory.INSTANCE);
    }
}
