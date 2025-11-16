package dev.creoii.greatbigworld.thealterworld.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public class AncientPedestalBlockEntityRenderState extends BlockEntityRenderState {
    protected ItemRenderState itemRenderState = new ItemRenderState();
    protected long worldTime;
    protected float tickProgress;

    public void update(ItemStack stack, ItemModelManager itemModelManager) {
        itemModelManager.update(this.itemRenderState, stack, ItemDisplayContext.NONE, null, null, 0);
    }
}
