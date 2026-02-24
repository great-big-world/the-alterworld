package dev.creoii.greatbigworld.thealterworld.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.class)
public class GuiMixin {
    @ModifyExpressionValue(method = "renderPortalOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;defaultBlockState()Lnet/minecraft/world/level/block/state/BlockState;"))
    private BlockState gbw$renderAlterworldPortalOverlay(BlockState original) {
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        ClientLevel clientLevel = Minecraft.getInstance().level;

        if (localPlayer.portalProcess != null && clientLevel.getBlockState(localPlayer.portalProcess.getEntryPosition()).is(TheAlterworldBlocks.ALTERWORLD_PORTAL)) {
            return TheAlterworldBlocks.ALTERWORLD_PORTAL.defaultBlockState();
        } else {
            BlockPos pos = new BlockPos(localPlayer.getBlockX(), localPlayer.getBlockY(), localPlayer.getBlockZ());
            return clientLevel.getBlockState(pos).is(TheAlterworldBlocks.ALTERWORLD_PORTAL) ? TheAlterworldBlocks.ALTERWORLD_PORTAL.defaultBlockState() : original;
        }
    }
}
