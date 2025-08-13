package dev.creoii.greatbigworld.thealterworld.client.render;

import dev.creoii.greatbigworld.thealterworld.block.AncientTotemBlock;
import dev.creoii.greatbigworld.thealterworld.block.entity.AncientTotemBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
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
public class AncientTotemBlockEntityRenderer implements BlockEntityRenderer<AncientTotemBlockEntity> {
    public AncientTotemBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(AncientTotemBlockEntity entity, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPos) {
        if (!entity.getStack().isEmpty()) {
            BlockState state = entity.getCachedState();
            Direction facing = state.get(AncientTotemBlock.FACING);

            matrices.push();
            matrices.translate(.5f, .5f, .5f);

            double outward = .5f;
            matrices.translate(facing.getOffsetX() * outward, facing.getOffsetY() * outward, facing.getOffsetZ() * outward);
            float yaw = switch (facing) {
                case NORTH -> 180f;
                case WEST -> 90f;
                case EAST -> -90f;
                case SOUTH, UP, DOWN -> 0f;
            };
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw));
            MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(), ItemDisplayContext.GROUND, WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().offset(facing)), overlay, matrices, vertexConsumers, entity.getWorld(), 0);

            matrices.pop();
        }
    }
}
