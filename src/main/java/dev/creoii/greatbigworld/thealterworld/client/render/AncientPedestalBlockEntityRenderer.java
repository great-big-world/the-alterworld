package dev.creoii.greatbigworld.thealterworld.client.render;

import dev.creoii.greatbigworld.thealterworld.block.AncientPedestalBlock;
import dev.creoii.greatbigworld.thealterworld.block.entity.AncientPedestalBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Environment(EnvType.CLIENT)
public record AncientPedestalBlockEntityRenderer(ItemModelManager itemModelManager) implements BlockEntityRenderer<AncientPedestalBlockEntity, AncientPedestalBlockEntityRenderState> {
    @Override
    public void updateRenderState(AncientPedestalBlockEntity blockEntity, AncientPedestalBlockEntityRenderState state, float tickProgress, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderer.super.updateRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.itemRenderState = new ItemRenderState();
        if (blockEntity.hasWorld())
            state.worldTime = blockEntity.getWorld().getTime();
        state.tickProgress = tickProgress;
        if (blockEntity.getRelic() != null)
            state.update(blockEntity.getRelic().getDefaultStack(), itemModelManager);
    }

    @Override
    public void render(AncientPedestalBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        if (!state.itemRenderState.isEmpty()) {
            if (state.blockState.get(AncientPedestalBlock.LIT))
                return;

            matrices.push();

            matrices.translate(.5d, .8d + Math.sin((state.worldTime + state.tickProgress) / 8d) / 16d, .5d);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((state.worldTime + state.tickProgress) * 2f));
            queue.submitItem(matrices, ItemDisplayContext.GROUND, state.lightmapCoordinates, 0, 0, new int[]{}, List.of(), RenderLayer.LINES, ItemRenderState.Glint.SPECIAL);
            state.itemRenderState.render(matrices, queue, state.lightmapCoordinates, OverlayTexture.DEFAULT_UV, 0);

            matrices.pop();
        }
    }

    @Override
    public AncientPedestalBlockEntityRenderState createRenderState() {
        return new AncientPedestalBlockEntityRenderState();
    }
}
