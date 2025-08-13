package dev.creoii.greatbigworld.thealterworld.client.render;

import dev.creoii.greatbigworld.thealterworld.block.AncientPedestalBlock;
import dev.creoii.greatbigworld.thealterworld.block.entity.AncientPedestalBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class AncientPedestalBlockEntityRenderer implements BlockEntityRenderer<AncientPedestalBlockEntity> {
    public AncientPedestalBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(AncientPedestalBlockEntity entity, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPos) {
        if (!entity.getStack().isEmpty()) {
            if (entity.getCachedState().get(AncientPedestalBlock.LIT))
                return;

            matrices.push();

            matrices.translate(.5d, .8d + Math.sin((entity.getWorld().getTime() + tickProgress) / 8d) / 16d, .5d);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((entity.getWorld().getTime() + tickProgress) * 2f));
            MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(), ItemDisplayContext.GROUND, WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().offset(Direction.UP)), overlay, matrices, vertexConsumers, entity.getWorld(), 0);

            matrices.pop();
        }
    }
}
