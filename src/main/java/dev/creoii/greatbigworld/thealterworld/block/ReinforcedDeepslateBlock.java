package dev.creoii.greatbigworld.thealterworld.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ReinforcedDeepslateBlock extends Block {
    public static final BooleanProperty CAN_FRACTURE = BooleanProperty.create("can_fracture");
    public static final IntegerProperty FRACTURE = IntegerProperty.create("fracture", 0, 8);

    public ReinforcedDeepslateBlock(Properties settings) {
        super(settings);
        registerDefaultState(getStateDefinition().any().setValue(CAN_FRACTURE, false).setValue(FRACTURE, 0));
    }

    public static boolean isFractured(BlockState state) {
        return state.getValueOrElse(CAN_FRACTURE, false) && state.getValueOrElse(FRACTURE, 0) > 0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CAN_FRACTURE, FRACTURE);
    }
}
