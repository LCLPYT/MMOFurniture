package work.lclpnet.mmofurniture.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.mmofurniture.module.UpgradedFenceModule;
import work.lclpnet.mmofurniture.util.VoxelShapeHelper;

import java.util.ArrayList;
import java.util.List;

public class UpgradedFenceBlock extends FurnitureWaterloggedBlock implements IPathNodeOverride {

    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty WEST = BooleanProperty.of("west");

    public final ImmutableMap<BlockState, VoxelShape> SHAPES;
    public final ImmutableMap<BlockState, VoxelShape> COLLISION_SHAPES;

    public UpgradedFenceBlock(Block parent) {
        super(Settings.copy(parent));
        setDefaultState(getDefaultState().with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false));
        SHAPES = this.generateShapes(this.getStateManager().getStates(), false);
        COLLISION_SHAPES = this.generateShapes(this.getStateManager().getStates(), true);
    }

    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states, boolean collision) {
        final VoxelShape FENCE_POST = Block.createCuboidShape(6, 0, 6, 10, 17, 10);
        final VoxelShape[] POST_SIDE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0.75, 0, 6.5, 4.75, 16, 9.5), Direction.WEST));
        final VoxelShape[] POST_MIDDLE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(6.01, 0, 6.5, 8, 16, 9.5), Direction.WEST));
        final VoxelShape[] FRAME_BOTTOM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 2.5, 7, 6, 5.5, 9), Direction.WEST));
        final VoxelShape[] FRAME_TOP = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 10.5, 7, 6.01, 13.5, 9), Direction.WEST));

        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for (BlockState state : states) {
            boolean north = state.get(NORTH);
            boolean east = state.get(EAST);
            boolean south = state.get(SOUTH);
            boolean west = state.get(WEST);
            boolean post = !(north && !east && south && !west || !north && east && !south && west);

            List<VoxelShape> shapes = new ArrayList<>();
            if (post) {
                shapes.add(this.applyCollision(FENCE_POST, collision));
            }
            if (north) {
                shapes.add(this.applyCollision(POST_SIDE[Direction.NORTH.getHorizontal()], collision));
                shapes.add(this.applyCollision(POST_MIDDLE[Direction.NORTH.getHorizontal()], collision));
                shapes.add(FRAME_BOTTOM[Direction.NORTH.getHorizontal()]);
                shapes.add(FRAME_TOP[Direction.NORTH.getHorizontal()]);
            }
            if (east) {
                shapes.add(this.applyCollision(POST_SIDE[Direction.EAST.getHorizontal()], collision));
                shapes.add(this.applyCollision(POST_MIDDLE[Direction.EAST.getHorizontal()], collision));
                shapes.add(FRAME_BOTTOM[Direction.EAST.getHorizontal()]);
                shapes.add(FRAME_TOP[Direction.EAST.getHorizontal()]);
            }
            if (south) {
                shapes.add(this.applyCollision(POST_SIDE[Direction.SOUTH.getHorizontal()], collision));
                shapes.add(this.applyCollision(POST_MIDDLE[Direction.SOUTH.getHorizontal()], collision));
                shapes.add(FRAME_BOTTOM[Direction.SOUTH.getHorizontal()]);
                shapes.add(FRAME_TOP[Direction.SOUTH.getHorizontal()]);
            }
            if (west) {
                shapes.add(this.applyCollision(POST_SIDE[Direction.WEST.getHorizontal()], collision));
                shapes.add(this.applyCollision(POST_MIDDLE[Direction.WEST.getHorizontal()], collision));
                shapes.add(FRAME_BOTTOM[Direction.WEST.getHorizontal()]);
                shapes.add(FRAME_TOP[Direction.WEST.getHorizontal()]);
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
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getFenceState(super.getPlacementState(ctx), ctx.getWorld(), ctx.getBlockPos());
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        return this.getFenceState(state, world, pos);
    }

    private BlockState getFenceState(BlockState state, WorldAccess world, BlockPos pos) {
        boolean north = canConnectToBlock(world, pos, Direction.NORTH);
        boolean east = canConnectToBlock(world, pos, Direction.EAST);
        boolean south = canConnectToBlock(world, pos, Direction.SOUTH);
        boolean west = canConnectToBlock(world, pos, Direction.WEST);
        return state.with(NORTH, north).with(EAST, east).with(SOUTH, south).with(WEST, west);
    }

    private boolean canConnectToBlock(WorldAccess world, BlockPos pos, Direction direction) {
        BlockPos offsetPos = pos.offset(direction);
        BlockState offsetState = world.getBlockState(offsetPos);

        boolean flag1 = false;
        if (offsetState.getBlock() instanceof UpgradedGateBlock) {
            Direction gateDirection = offsetState.get(UpgradedGateBlock.DIRECTION);
            DoorHinge hingeSide = offsetState.get(UpgradedGateBlock.HINGE);
            Direction hingeFace = hingeSide == DoorHinge.LEFT ? gateDirection.rotateYCounterclockwise() : gateDirection.rotateYClockwise();
            flag1 = direction == hingeFace.getOpposite() || (!offsetState.get(UpgradedGateBlock.DOUBLE) && direction.getAxis() != gateDirection.getAxis());
        }

        return !cannotConnect(offsetState) && offsetState.isSideSolidFullSquare(world, offsetPos, direction.getOpposite()) || offsetState.isIn(UpgradedFenceModule.tag) || flag1;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(NORTH, EAST, SOUTH, WEST);
    }

    @Override
    public PathNodeType getPathNodeType(BlockState state, BlockView blockView, BlockPos pos) {
        return PathNodeType.FENCE;
    }
}
