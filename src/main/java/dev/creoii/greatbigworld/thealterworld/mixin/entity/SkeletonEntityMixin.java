package dev.creoii.greatbigworld.thealterworld.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Skeleton.class)
public abstract class SkeletonEntityMixin extends AbstractSkeleton {
    protected SkeletonEntityMixin(EntityType<? extends AbstractSkeleton> entityType, Level world) {
        super(entityType, world);
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/skeleton/Skeleton;isAlive()Z"))
    private boolean gbw$disableSkeletonCovertInOverworld(boolean original) {
        return original && level().dimension() != Level.OVERWORLD;
    }
}
