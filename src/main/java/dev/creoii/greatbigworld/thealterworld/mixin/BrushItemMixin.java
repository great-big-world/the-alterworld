package dev.creoii.greatbigworld.thealterworld.mixin;

import com.google.common.collect.Sets;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import dev.creoii.greatbigworld.knowledge.KnowledgeManager;
import dev.creoii.greatbigworld.thealterworld.block.ReinforcedDeepslateBlock;
import dev.creoii.greatbigworld.block.entity.KnowledgeBlockEntity;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldSoundEvents;
import dev.creoii.greatbigworld.thealterworld.world.FracturedAlterworldPortal;
import dev.creoii.greatbigworld.util.network.LearnKnowledgeS2C;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(BrushItem.class)
public class BrushItemMixin {
    @Inject(method = "onUseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;)V", shift = At.Shift.AFTER))
    private void gbw$brushCavePaintings(Level level, LivingEntity livingEntity, ItemStack itemStack, int i, CallbackInfo ci, @Local(ordinal = 1) int j, @Local Player player, @Local BlockState blockState, @Local BlockPos blockPos) {
        if (!level.isClientSide() && j > 40 && blockState.is(TheAlterworldBlocks.REINFORCED_DEEPSLATE) && (level.dimension() == Level.OVERWORLD || level.dimension() == GreatBigWorld.ALTERWORLD_KEY)) {
            if (blockState.hasProperty(ReinforcedDeepslateBlock.CAN_FRACTURE) && !blockState.getValue(ReinforcedDeepslateBlock.CAN_FRACTURE))
                return;

            Block block = blockState.getBlock();
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (block instanceof ReinforcedDeepslateBlock reinforcedDeepslateBlock && blockEntity instanceof KnowledgeBlockEntity knowledgeBlockEntity) {
                if (knowledgeBlockEntity.hasPlayerLearned(player))
                    return;

                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    BlockPos pos1 = blockPos.relative(direction);

                    BlockState state = level.getBlockState(pos1);
                    if (!state.canBeReplaced() && !state.isAir())
                        continue;

                    Optional<FracturedAlterworldPortal> optional2 = FracturedAlterworldPortal.getNewPortal(level, pos1, direction);

                    if (optional2.isPresent()) {
                        KnowledgeManager knowledgeManager = KnowledgeManager.getServerState(level.getServer());
                        Optional<Knowledge> knowledge = knowledgeBlockEntity.getKnowledge() != null ? Optional.of(knowledgeBlockEntity.getKnowledge()) : reinforcedDeepslateBlock.getKnowledgePool(blockState).getRandom(level.random);
                        if (knowledge.isPresent()) {
                            if (knowledgeManager.learn(player, knowledge.get())) {
                                ServerPlayNetworking.send((ServerPlayer) player, new LearnKnowledgeS2C(knowledge.get().type(), Sets.newHashSet(knowledge.get())));

                                EquipmentSlot equipmentSlot = itemStack.equals(player.getItemBySlot(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                                itemStack.hurtAndBreak(1, player, equipmentSlot);

                                knowledgeBlockEntity.setKnowledge(knowledge.get());
                            }
                        }

                        optional2.get().createPortal(level);
                        level.playSound(null, optional2.get().lowerCorner(), TheAlterworldSoundEvents.STRUCTURE_ANCIENT_CITY_PORTAL_OPEN, SoundSource.AMBIENT, .5f, .75f);
                        knowledgeBlockEntity.setPlayerLearned(player);
                        break;
                    }
                }
            }
        }
    }
}