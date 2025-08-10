package dev.creoii.greatbigworld.thealterworld.client;

import dev.creoii.greatbigworld.thealterworld.client.render.AncientPedestalBlockEntityRenderer;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlockEntityTypes;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class TheAlterworldClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(TheAlterworldBlocks.ALTERWORLD_PORTAL, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(TheAlterworldBlocks.ANCIENT_MOSAIC, RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(TheAlterworldBlocks.ANCIENT_PEDESTAL, RenderLayer.getCutout());

        BlockEntityRendererFactories.register(TheAlterworldBlockEntityTypes.ANCIENT_PEDESTAL, AncientPedestalBlockEntityRenderer::new);
    }
}
