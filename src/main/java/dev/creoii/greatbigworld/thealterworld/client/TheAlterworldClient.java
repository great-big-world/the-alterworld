package dev.creoii.greatbigworld.thealterworld.client;

import dev.creoii.greatbigworld.thealterworld.client.render.AncientPedestalBlockEntityRenderer;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlockEntityTypes;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

public class TheAlterworldClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.putBlock(TheAlterworldBlocks.ALTERWORLD_PORTAL, ChunkSectionLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(TheAlterworldBlocks.ANCIENT_PEDESTAL, ChunkSectionLayer.CUTOUT);

        BlockEntityRenderers.register(TheAlterworldBlockEntityTypes.ANCIENT_PEDESTAL, ctx -> new AncientPedestalBlockEntityRenderer(ctx.itemModelResolver()));
    }
}
