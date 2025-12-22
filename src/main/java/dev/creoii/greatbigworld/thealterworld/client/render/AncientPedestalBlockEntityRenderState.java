package dev.creoii.greatbigworld.thealterworld.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class AncientPedestalBlockEntityRenderState extends BlockEntityRenderState {
    protected ItemStackRenderState itemRenderState = new ItemStackRenderState();
    protected long worldTime;
    protected float tickProgress;

    public void update(ItemStack stack, ItemModelResolver itemModelManager) {
        itemModelManager.appendItemLayers(itemRenderState, stack, ItemDisplayContext.GROUND, null, null, 0);
    }
}
