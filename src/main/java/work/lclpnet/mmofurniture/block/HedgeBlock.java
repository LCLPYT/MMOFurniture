package work.lclpnet.mmofurniture.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.mmofurniture.module.HedgeModule;
import work.lclpnet.mmofurniture.util.VoxelShapeHelper;

import java.util.ArrayList;
import java.util.List;

public class HedgeBlock extends FurnitureWaterloggedBlock {

    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty WEST = BooleanProperty.of("west");

    public final ImmutableMap<BlockState, VoxelShape> SHAPES;
    public final ImmutableMap<BlockState, VoxelShape> COLLISION_SHAPES;

    public HedgeBlock(Block parent) {
        super(Settings.copy(parent));
        setDefaultState(getDefaultState()
                .with(NORTH, false)
                .with(EAST, false)
                .with(SOUTH, false)
                .with(WEST, false)
                .with(WATERLOGGED, false));
        SHAPES = this.generateShapes(this.getStateManager().getStates(), false);
        COLLISION_SHAPES = this.generateShapes(this.getStateManager().getStates(), true);
    }

    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states, boolean collision) {
        final VoxelShape POST = Block.createCuboidShape(4, 0, 4, 12, 16, 12);
        final VoxelShape[] SIDE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(4, 0, 0, 12, 16, 4), Direction.SOUTH));

        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for (BlockState state : states) {
            boolean north = state.get(NORTH);
            boolean east = state.get(EAST);
            boolean south = state.get(SOUTH);
            boolean west = state.get(WEST);

            List<VoxelShape> shapes = new ArrayList<>();
            shapes.add(this.applyCollision(POST, collision));
            if (north) {
                shapes.add(this.applyCollision(SIDE[Direction.NORTH.getHorizontal()], collision));
            }
            if (east) {
                shapes.add(this.applyCollision(SIDE[Direction.EAST.getHorizontal()], collision));
            }
            if (south) {
                shapes.add(this.applyCollision(SIDE[Direction.SOUTH.getHorizontal()], collision));
            }
            if (west) {
                shapes.add(this.applyCollision(SIDE[Direction.WEST.getHorizontal()], collision));
            }
            builder.put(state, VoxelShapeHelper.combineAll(shapes));
        }
        return builder.build();
    }

    private VoxelShape applyCollision(VoxelShape shape, boolean collision) {
        if (collision) {
            shape = VoxelShapeHelper.setMaxHeight(shape, 1.5);
            shape = VoxelShapeHelper.limitHorizontal(shape);
        }
        return shape;
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
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return COLLISION_SHAPES.get(state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        return this.getHedgeState(state, world, pos);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState placementState = super.getPlacementState(ctx);
        return placementState == null ? null : this.getHedgeState(placementState, ctx.getWorld(), ctx.getBlockPos());
    }

    private BlockState getHedgeState(BlockState state, WorldAccess world, BlockPos pos) {
        boolean north = canConnectToBlock(world, pos, Direction.NORTH);
        boolean east = canConnectToBlock(world, pos, Direction.EAST);
        boolean south = canConnectToBlock(world, pos, Direction.SOUTH);
        boolean west = canConnectToBlock(world, pos, Direction.WEST);
        return state.with(NORTH, north).with(EAST, east).with(SOUTH, south).with(WEST, west);
    }

    private boolean canConnectToBlock(WorldAccess world, BlockPos pos, Direction direction) {
        BlockPos offsetPos = pos.offset(direction);
        BlockState offsetState = world.getBlockState(offsetPos);

        return !cannotConnect(offsetState) && offsetState.isSideSolidFullSquare(world, offsetPos, direction.getOpposite()) || offsetState.isIn(HedgeModule.tag);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(NORTH, EAST, SOUTH, WEST);
    }

    @Override
    public int getOpacity(BlockState state, BlockView world, BlockPos pos) {
        return 1;
    }
}
