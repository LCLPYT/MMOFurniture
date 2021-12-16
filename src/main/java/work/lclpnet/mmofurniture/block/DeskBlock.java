package work.lclpnet.mmofurniture.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import work.lclpnet.mmofurniture.util.VoxelShapeHelper;

import java.util.ArrayList;
import java.util.List;

public class DeskBlock extends FurnitureHorizontalWaterloggedBlock {

    public static final EnumProperty<Type> TYPE = EnumProperty.of("type", Type.class);

    private final short variant;
    public final ImmutableMap<BlockState, VoxelShape> SHAPES;

    public DeskBlock(Block parent, short variant) {
        super(Settings.copy(parent));
        this.variant = variant;
        setDefaultState(getDefaultState().with(DIRECTION, Direction.NORTH).with(TYPE, Type.SINGLE));
        SHAPES = this.generateShapes(this.getStateManager().getStates());
    }

    protected ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states) {
        final VoxelShape[] DESK_TOP = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 14, 0, 16, 16, 16), Direction.SOUTH));
        final VoxelShape[] DESK_BACK = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 2, 1, 16, 14, 3), Direction.SOUTH));
        final VoxelShape[] DESK_LEFT = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 0, 0, 2, 14, 15), Direction.SOUTH));
        final VoxelShape[] DESK_RIGHT = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(14, 0, 0, 16, 14, 15), Direction.SOUTH));

        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for (BlockState state : states) {
            Direction direction = state.get(DIRECTION);
            Type type = state.get(TYPE);
            List<VoxelShape> shapes = new ArrayList<>();
            shapes.add(DESK_TOP[direction.getHorizontal()]);
            shapes.add(DESK_BACK[direction.getHorizontal()]);
            switch (type) {
                case SINGLE:
                    shapes.add(DESK_LEFT[direction.getHorizontal()]);
                    shapes.add(DESK_RIGHT[direction.getHorizontal()]);
                    break;
                case LEFT:
                    shapes.add(DESK_LEFT[direction.getHorizontal()]);
                    break;
                case RIGHT:
                    shapes.add(DESK_RIGHT[direction.getHorizontal()]);
                    break;
            }
            builder.put(state, VoxelShapeHelper.combineAll(shapes));
        }
        return builder.build();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES.get(state);
    }

    @Override
    public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return SHAPES.get(state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        Direction dir = state.get(DIRECTION);
        boolean left = isDesk(world, pos, dir.rotateYCounterclockwise(), dir);
        boolean right = isDesk(world, pos, dir.rotateYClockwise(), dir);
        if (left && right) {
            return state.with(TYPE, Type.MIDDLE);
        }
        if (left) {
            return state.with(TYPE, Type.RIGHT);
        }
        if (right) {
            return state.with(TYPE, Type.LEFT);
        }
        return state.with(TYPE, Type.SINGLE);
    }

    private boolean isDesk(WorldAccess world, BlockPos source, Direction checkDirection, Direction tableDirection) {
        BlockState state = world.getBlockState(source.offset(checkDirection));
        return state.getBlock() instanceof DeskBlock && ((DeskBlock) state.getBlock()).variant == variant && state.get(DIRECTION) == tableDirection;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(TYPE);
    }

    public enum Type implements StringIdentifiable {
        SINGLE("single"),
        LEFT("left"),
        RIGHT("right"),
        MIDDLE("middle");

        private final String id;

        Type(String id) {
            this.id = id;
        }

        @Override
        public String asString() {
            return id;
        }

        @Override
        public String toString() {
            return id;
        }
    }
}
