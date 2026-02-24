package dev.creoii.greatbigworld.thealterworld.block;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.block.KnowledgeBlock;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import dev.creoii.greatbigworld.thealterworld.world.FracturedAlterworldPortal;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.equipment.trim.TrimPatterns;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class ReinforcedDeepslateBlock extends KnowledgeBlock {
    public static final EnumProperty<Rune> RUNE = EnumProperty.create("rune", Rune.class);
    public static final BooleanProperty CAN_FRACTURE = BooleanProperty.create("can_fracture");
    public static final BooleanProperty FRACTURED = BooleanProperty.create("fractured");

    public ReinforcedDeepslateBlock(Properties settings) {
        super(settings);
        registerDefaultState(getStateDefinition().any().setValue(CAN_FRACTURE, false).setValue(FRACTURED, false).setValue(RUNE, Rune.NONE));
    }

    public static boolean isFractured(BlockState state) {
        return state.getValueOrElse(CAN_FRACTURE, false) && state.getValue(FRACTURED);
    }

    @Override
    public WeightedList<Knowledge> getKnowledgePool(BlockState state) {
        return state.getValue(RUNE).getKnowledgePool();
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult blockHitResult) {
        InteractionResult result = super.useWithoutItem(state, world, pos, player, blockHitResult);
        if (!world.isClientSide() && (world.dimension() == Level.OVERWORLD || world.dimension() == GreatBigWorld.ALTERWORLD_KEY)) {
            Optional<FracturedAlterworldPortal> optional2 = FracturedAlterworldPortal.getNewPortal(world, pos, Direction.Axis.X);
            if (optional2.isPresent()) { // never present??
                optional2.get().createPortal(world);
                return InteractionResult.SUCCESS_SERVER;
            }
        }
        return result;
    }

    public static void fractureAt(ServerLevel level, BlockPos pos, int lifetime) {
        BlockState state = level.getBlockState(pos);
        if (state.is(TheAlterworldBlocks.REINFORCED_DEEPSLATE)) {
            level.setBlock(pos, state.setValue(FRACTURED, true), 18);

            String rune = state.getValue(RUNE).getSerializedName();
            Vec3 vec3 = Vec3.atLowerCornerOf(pos).add(0d, .5d, 0d);
            PlayerLookup.tracking(level, pos).forEach(serverPlayer -> {
                ServerPlayNetworking.send(serverPlayer, new FractureS2C(vec3, lifetime, rune));
            });
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CAN_FRACTURE, FRACTURED, RUNE);
    }

    public enum Rune implements StringRepresentable {
        NONE(WeightedList.<Knowledge>builder().build()),
        JUNGLE(WeightedList.<Knowledge>builder().add(new Knowledge(Knowledge.Type.ARMOR_TRIM, TrimPatterns.WILD)).build()),
        DESERT(WeightedList.<Knowledge>builder().add(new Knowledge(Knowledge.Type.ARMOR_TRIM, TrimPatterns.DUNE)).build()),
        SWAMP(WeightedList.<Knowledge>builder().add(new Knowledge(Knowledge.Type.ENCHANTMENT, Enchantments.DEPTH_STRIDER)).build()),
        ICE(WeightedList.<Knowledge>builder().add(new Knowledge(Knowledge.Type.ENCHANTMENT, Enchantments.FROST_WALKER)).build());

        private final WeightedList<Knowledge> knowledgePool;

        Rune(WeightedList<Knowledge> knowledgePool) {
            this.knowledgePool = knowledgePool;
        }

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }

        public WeightedList<Knowledge> getKnowledgePool() {
            return knowledgePool;
        }
    }

    public record FractureS2C(Vec3 vec3, int lifetime, String rune) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<FractureS2C> PACKET_ID = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "fracture"));
        public static final StreamCodec<RegistryFriendlyByteBuf, FractureS2C> PACKET_CODEC = StreamCodec.ofMember(FractureS2C::write, FractureS2C::new);

        public FractureS2C(RegistryFriendlyByteBuf buf) {
            this(buf.readVec3(), buf.readInt(), buf.readUtf());
        }

        public void write(RegistryFriendlyByteBuf buf) {
            buf.writeVec3(vec3);
            buf.writeInt(lifetime);
            buf.writeUtf(rune);
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }
}
