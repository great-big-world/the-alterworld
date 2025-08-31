package dev.creoii.greatbigworld.thealterworld.client;

import dev.creoii.greatbigworld.thealterworld.client.render.AncientPedestalBlockEntityRenderer;
import dev.creoii.greatbigworld.thealterworld.client.render.AncientTotemBlockEntityRenderer;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlockEntityTypes;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class TheAlterworldClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.putBlock(TheAlterworldBlocks.ALTERWORLD_PORTAL, BlockRenderLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(TheAlterworldBlocks.ANCIENT_MOSAIC, BlockRenderLayer.CUTOUT_MIPPED);
        BlockRenderLayerMap.putBlock(TheAlterworldBlocks.ANCIENT_PEDESTAL, BlockRenderLayer.CUTOUT);

        BlockEntityRendererFactories.register(TheAlterworldBlockEntityTypes.ANCIENT_PEDESTAL, ctx -> new AncientPedestalBlockEntityRenderer());
        BlockEntityRendererFactories.register(TheAlterworldBlockEntityTypes.ANCIENT_TOTEM, AncientTotemBlockEntityRenderer::new);
    }
}
