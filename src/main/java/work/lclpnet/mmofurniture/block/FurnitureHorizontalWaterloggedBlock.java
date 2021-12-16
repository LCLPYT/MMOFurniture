package work.lclpnet.mmofurniture.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import org.jetbrains.annotations.Nullable;

public class FurnitureHorizontalWaterloggedBlock extends FurnitureWaterloggedBlock {

    public static final DirectionProperty DIRECTION = Properties.HORIZONTAL_FACING;

    public FurnitureHorizontalWaterloggedBlock(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState superState = super.getPlacementState(ctx);
        return superState == null ? null : superState.with(DIRECTION, ctx.getPlayerFacing());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(DIRECTION, rotation.rotate(state.get(DIRECTION)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(DIRECTION)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(DIRECTION);
    }
}
