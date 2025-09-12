package dev.creoii.greatbigworld.thealterworld.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;

public class ReinforcedDeepslateBlock extends Block {
    public static final BooleanProperty CAN_FRACTURE = BooleanProperty.of("can_fracture");
    public static final IntProperty FRACTURE = IntProperty.of("fracture", 0, 8);

    public ReinforcedDeepslateBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(CAN_FRACTURE, false).with(FRACTURE, 0));
    }

    public static boolean isFractured(BlockState state) {
        return state.get(CAN_FRACTURE, false) && state.get(FRACTURE, 0) > 0;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(CAN_FRACTURE, FRACTURE);
    }
}
