package dev.creoii.greatbigworld.thealterworld.mixin;

import com.google.common.collect.Maps;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldStatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect;
import net.minecraft.world.level.Level;

@Mixin(ClearAllStatusEffectsConsumeEffect.class)
public class ClearAllEffectsConsumeEffectMixin {
    @Inject(method = "apply", at = @At("HEAD"), cancellable = true)
    private void gbw$dontClearPlanarFractureEffectMilk(Level world, ItemStack stack, LivingEntity user, CallbackInfoReturnable<Boolean> cir) {
        if (world.isClientSide()) {
            cir.setReturnValue(false);
        } else if (user.getActiveEffectsMap().isEmpty()) {
            cir.setReturnValue(false);
        } else {
            Map<Holder<MobEffect>, MobEffectInstance> map = Maps.newHashMap(user.getActiveEffectsMap());
            map.remove(TheAlterworldStatusEffects.PLANAR_FRACTURE);
            map.forEach((entry, statusEffectInstance) -> {
                user.getActiveEffectsMap().remove(entry);
            });
            user.onEffectsRemoved(map.values());
            cir.setReturnValue(true);
        }
        cir.cancel();
    }
}
