package dev.creoii.greatbigworld.thealterworld.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.creoii.greatbigworld.thealterworld.block.AncientPedestalBlock;
import dev.creoii.greatbigworld.thealterworld.block.entity.AncientPedestalBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

@Environment(EnvType.CLIENT)
public record AncientPedestalBlockEntityRenderer(ItemModelResolver itemModelManager) implements BlockEntityRenderer<AncientPedestalBlockEntity, AncientPedestalBlockEntityRenderState> {
    @Override
    public void submit(AncientPedestalBlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if (!state.itemRenderState.isEmpty()) {
            if (state.blockState.getValue(AncientPedestalBlock.LIT))
                return;

            poseStack.pushPose();

            poseStack.translate(.5d, .8d + Math.sin((state.worldTime + state.tickProgress) / 8d) / 16d, .5d);
            poseStack.mulPose(Axis.YP.rotationDegrees((state.worldTime + state.tickProgress) * 2f));
            submitNodeCollector.submitItem(poseStack, ItemDisplayContext.GROUND, state.lightCoords, 0, 0, new int[]{}, List.of(), RenderTypes.LINES, ItemStackRenderState.FoilType.SPECIAL);
            state.itemRenderState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

            poseStack.popPose();
        }
    }

    @Override
    public void extractRenderState(AncientPedestalBlockEntity blockEntity, AncientPedestalBlockEntityRenderState state, float f, Vec3 vec3, ModelFeatureRenderer.@org.jspecify.annotations.Nullable CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, f, vec3, crumblingOverlay);
        state.itemRenderState = new ItemStackRenderState();
        if (blockEntity.hasLevel())
            state.worldTime = blockEntity.getLevel().getGameTime();
        state.tickProgress = f;
        if (blockEntity.getStack() != ItemStack.EMPTY)
            state.update(blockEntity.getStack(), itemModelManager);
    }

    @Override
    public AncientPedestalBlockEntityRenderState createRenderState() {
        return new AncientPedestalBlockEntityRenderState();
    }
}
