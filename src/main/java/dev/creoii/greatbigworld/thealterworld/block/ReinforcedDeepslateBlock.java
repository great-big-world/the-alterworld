package dev.creoii.greatbigworld.thealterworld.block;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class ReinforcedDeepslateBlock extends Block {
    public static final BooleanProperty CAN_FRACTURE = BooleanProperty.create("can_fracture");
    public static final BooleanProperty FRACTURED = BooleanProperty.create("fractured");

    public ReinforcedDeepslateBlock(Properties settings) {
        super(settings);
        registerDefaultState(getStateDefinition().any().setValue(CAN_FRACTURE, false).setValue(FRACTURED, false));
    }

    public static boolean isFractured(BlockState state) {
        return state.getValueOrElse(CAN_FRACTURE, false) && state.getValue(FRACTURED);
    }

    public static void fractureAt(ServerLevel level, BlockPos pos, int lifetime) {
        level.setBlock(pos, TheAlterworldBlocks.REINFORCED_DEEPSLATE.defaultBlockState().setValue(ReinforcedDeepslateBlock.FRACTURED, true), 18);

        Vec3 vec3 = Vec3.atLowerCornerOf(pos).add(0d, .5d, 0d);
        PlayerLookup.tracking(level, pos).forEach(serverPlayer -> {
            ServerPlayNetworking.send(serverPlayer, new FractureS2C(vec3, lifetime));
        });
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if (!level.isClientSide())
            fractureAt((ServerLevel) level, blockPos, 250);
        return super.useWithoutItem(blockState, level, blockPos, player, blockHitResult);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CAN_FRACTURE, FRACTURED);
    }

    public record FractureS2C(Vec3 vec3, int lifetime) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<FractureS2C> PACKET_ID = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "fracture"));
        public static final StreamCodec<RegistryFriendlyByteBuf, FractureS2C> PACKET_CODEC = StreamCodec.ofMember(FractureS2C::write, FractureS2C::new);

        public FractureS2C(RegistryFriendlyByteBuf buf) {
            this(buf.readVec3(), buf.readInt());
        }

        public void write(RegistryFriendlyByteBuf buf) {
            buf.writeVec3(vec3);
            buf.writeInt(lifetime);
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }
}
