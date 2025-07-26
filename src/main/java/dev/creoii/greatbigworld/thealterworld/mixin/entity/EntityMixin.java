package dev.creoii.greatbigworld.thealterworld.mixin.entity;

import dev.creoii.greatbigworld.thealterworld.block.AlterworldPortalBlock;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldStatusEffects;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract Random getRandom();
    @Shadow public abstract BlockState getBlockStateAtPos();

    @Inject(method = "tickPortalTeleportation", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;teleportTo(Lnet/minecraft/world/TeleportTarget;)Lnet/minecraft/entity/Entity;", shift = At.Shift.AFTER), cancellable = true)
    private void gbw$applyFracturedRealmEffect(CallbackInfo ci) {
        BlockState state = getBlockStateAtPos();
        if (state.isOf(TheAlterworldBlocks.ALTERWORLD_PORTAL)) {
            if (((Entity) (Object) this) instanceof LivingEntity living) {
                if (state.get(AlterworldPortalBlock.FRACTURED)) {
                    int ticks = getRandom().nextBetween(620, 3620);
                    living.addStatusEffect(new StatusEffectInstance(TheAlterworldStatusEffects.PLANAR_FRACTURE, ticks /* 30 seconds to 3 minutes */, 0, false, false));
                } else {
                    living.removeStatusEffect(TheAlterworldStatusEffects.PLANAR_FRACTURE);
                }
            }
        } else if (state.isOf(Blocks.NETHER_PORTAL) || state.isOf(Blocks.END_PORTAL)) {
            if (((Entity) (Object) this) instanceof LivingEntity living && living.hasStatusEffect(TheAlterworldStatusEffects.PLANAR_FRACTURE)) {
                ci.cancel();
            }
        }
    }
}
