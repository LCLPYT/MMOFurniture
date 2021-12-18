package work.lclpnet.mmofurniture.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.mmofurniture.util.VoxelShapeHelper;

import java.util.ArrayList;
import java.util.List;

public class UpgradedGateBlock extends FurnitureHorizontalWaterloggedBlock implements IPathNodeOverride {

    public static final EnumProperty<DoorHinge> HINGE = EnumProperty.of("hinge", DoorHinge.class);
    public static final BooleanProperty OPEN = Properties.OPEN;
    public static final BooleanProperty DOUBLE = BooleanProperty.of("double");
    public static final BooleanProperty POWERED = Properties.POWERED;

    public final ImmutableMap<BlockState, VoxelShape> SHAPES;
    public final ImmutableMap<BlockState, VoxelShape> COLLISION_SHAPES;

    public UpgradedGateBlock(Block parent) {
        super(Settings.copy(parent));
        setDefaultState(getDefaultState().with(HINGE, DoorHinge.LEFT).with(OPEN, false).with(DOUBLE, false));
        SHAPES = this.generateShapes(this.getStateManager().getStates(), false);
        COLLISION_SHAPES = this.generateShapes(this.getStateManager().getStates(), true);
    }

    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states, boolean collision) {
        final VoxelShape[] RIGHT_GATE_CLOSED = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1.5, 1, 7, 14.5, 17, 9), Direction.SOUTH));
        final VoxelShape[] RIGHT_DOUBLE_GATE_CLOSED = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 1, 7, 14, 17, 9), Direction.SOUTH));
        final VoxelShape[] RIGHT_GATE_OPEN = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(13, 1, -5, 15, 17, 8), Direction.SOUTH));
        final VoxelShape[] RIGHT_DOUBLE_GATE_OPEN = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(13, 1, -6, 15, 17, 8), Direction.SOUTH));
        final VoxelShape[] RIGHT_HINGE_BOTTOM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(14, 3, 7, 15, 6, 9), Direction.SOUTH));
        final VoxelShape[] RIGHT_HINGE_TOP = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(14, 12, 7, 15, 15, 9), Direction.SOUTH));
        final VoxelShape[] RIGHT_POST = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(15, 0, 6, 18, 17, 10), Direction.SOUTH));
        final VoxelShape[] LEFT_GATE_CLOSED = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1.5, 1, 7, 14.5, 17, 9), Direction.SOUTH));
        final VoxelShape[] LEFT_DOUBLE_GATE_CLOSED = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(2, 1, 7, 16, 17, 9), Direction.SOUTH));
        final VoxelShape[] LEFT_GATE_OPEN = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 1, -5, 3, 17, 8), Direction.SOUTH));
        final VoxelShape[] LEFT_DOUBLE_GATE_OPEN = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 1, -6, 3, 17, 8), Direction.SOUTH));
        final VoxelShape[] LEFT_HINGE_BOTTOM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 3, 7, 2, 6, 9), Direction.SOUTH));
        final VoxelShape[] LEFT_HINGE_TOP = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 12, 7, 2, 15, 9), Direction.SOUTH));
        final VoxelShape[] LEFT_POST = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(-2, 0, 6, 1, 17, 10), Direction.SOUTH));

        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for (BlockState state : states) {
            Direction direction = state.get(DIRECTION);
            DoorHinge hingeSide = state.get(HINGE);
            boolean open = state.get(OPEN);
            boolean double_ = state.get(DOUBLE);

            List<VoxelShape> shapes = new ArrayList<>();
            switch (hingeSide) {
                case LEFT:
                    VoxelShape post = LEFT_POST[direction.getHorizontal()];
                    if (collision) {
                        post = VoxelShapeHelper.setMaxHeight(post, 1.5);
                        post = VoxelShapeHelper.limitHorizontal(post);
                    }
                    shapes.add(post);
                    shapes.add(LEFT_HINGE_BOTTOM[direction.getHorizontal()]);
                    shapes.add(LEFT_HINGE_TOP[direction.getHorizontal()]);
                    if (open) {
                        VoxelShape doubleGate = double_ ? LEFT_DOUBLE_GATE_OPEN[direction.getHorizontal()] : LEFT_GATE_OPEN[direction.getHorizontal()];
                        if (collision) {
                            doubleGate = VoxelShapeHelper.setMaxHeight(doubleGate, 1.5);
                            doubleGate = VoxelShapeHelper.limitHorizontal(doubleGate);
                        }
                        shapes.add(doubleGate);
                    } else {
                        VoxelShape doubleGate = double_ ? LEFT_DOUBLE_GATE_CLOSED[direction.getHorizontal()] : LEFT_GATE_CLOSED[direction.getHorizontal()];
                        if (collision) {
                            doubleGate = VoxelShapeHelper.setMaxHeight(doubleGate, 1.5);
                            doubleGate = VoxelShapeHelper.limitHorizontal(doubleGate);
                        }
                        shapes.add(doubleGate);
                    }
                    if (!double_) {
                        post = RIGHT_POST[direction.getHorizontal()];
                        if (collision) {
                            post = VoxelShapeHelper.setMaxHeight(post, 1.5);
                            post = VoxelShapeHelper.limitHorizontal(post);
                        }
                        shapes.add(post);
                    }
                    break;
                case RIGHT:
                    post = RIGHT_POST[direction.getHorizontal()];
                    if (collision) {
                        post = VoxelShapeHelper.setMaxHeight(post, 1.5);
                        post = VoxelShapeHelper.limitHorizontal(post);
                    }
                    shapes.add(post);
                    shapes.add(RIGHT_HINGE_BOTTOM[direction.getHorizontal()]);
                    shapes.add(RIGHT_HINGE_TOP[direction.getHorizontal()]);
                    if (open) {
                        VoxelShape doubleGate = double_ ? RIGHT_DOUBLE_GATE_OPEN[direction.getHorizontal()] : RIGHT_GATE_OPEN[direction.getHorizontal()];
                        if (collision) {
                            doubleGate = VoxelShapeHelper.setMaxHeight(doubleGate, 1.5);
                            doubleGate = VoxelShapeHelper.limitHorizontal(doubleGate);
                        }
                        shapes.add(doubleGate);
                    } else {
                        VoxelShape doubleGate = double_ ? RIGHT_DOUBLE_GATE_CLOSED[direction.getHorizontal()] : RIGHT_GATE_CLOSED[direction.getHorizontal()];
                        if (collision) {
                            doubleGate = VoxelShapeHelper.setMaxHeight(doubleGate, 1.5);
                            doubleGate = VoxelShapeHelper.limitHorizontal(doubleGate);
                        }
                        shapes.add(doubleGate);
                    }
                    if (!double_) {
                        post = LEFT_POST[direction.getHorizontal()];
                        if (collision) {
                            post = VoxelShapeHelper.setMaxHeight(post, 1.5);
                            post = VoxelShapeHelper.limitHorizontal(post);
                        }
                        shapes.add(post);
                    }
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
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return COLLISION_SHAPES.get(state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        Direction hitFace = hit.getSide();
        Direction direction = state.get(DIRECTION);
        boolean open = state.get(OPEN);

        if (hitFace.getAxis() != direction.getAxis()) {
            if (!open || hitFace.getAxis().isVertical()) {
                return ActionResult.PASS;
            }
        }

        DoorHinge hingeSide = state.get(HINGE);
        this.openGate(state, world, pos, direction, hitFace, !open);
        this.openDoubleGate(world, pos, direction, hitFace, hingeSide, !open);
        this.openAdjacentGate(world, pos, direction, Direction.UP, hitFace, hingeSide, !open, 5);
        this.openAdjacentGate(world, pos, direction, Direction.DOWN, hitFace, hingeSide, !open, 5);

        world.syncWorldEvent(player, !open ? 1008 : 1014, pos, 0);

        return ActionResult.SUCCESS;
    }

    private void openGate(BlockState state, WorldAccess world, BlockPos pos, Direction direction, Direction hitFace, boolean open) {
        if (open) {
            if (hitFace.getOpposite() == direction) {
                world.setBlockState(pos, state.with(OPEN, open), 3);
            } else if (hitFace == direction) {
                world.setBlockState(pos, state.with(OPEN, open).with(HINGE, this.getOppositeHinge(state.get(HINGE))).with(DIRECTION, hitFace.getOpposite()), 3);
            }
        } else {
            world.setBlockState(pos, state.with(OPEN, open), 3);
        }
    }

    private void openAdjacentGate(WorldAccess world, BlockPos pos, Direction direction, Direction offset, Direction hitFace, DoorHinge hingeSide, boolean open, int limit) {
        if (limit <= 0) {
            return;
        }

        BlockPos offsetPos = pos.offset(offset);
        BlockState state = world.getBlockState(offsetPos);
        if (state.getBlock() == this) {
            if (state.get(DIRECTION) != direction || state.get(HINGE) != hingeSide || state.get(OPEN) == open) {
                return;
            }
            this.openGate(state, world, offsetPos, direction, hitFace, open);
            this.openDoubleGate(world, offsetPos, direction, hitFace, hingeSide, open);
            this.openAdjacentGate(world, offsetPos, direction, offset, hitFace, hingeSide, open, limit - 1);
        }
    }

    private void openDoubleGate(WorldAccess world, BlockPos pos, Direction direction, Direction hitFace, DoorHinge hingeSide, boolean open) {
        BlockPos adjacentPos = pos.offset(hingeSide == DoorHinge.LEFT ? direction.rotateYClockwise() : direction.rotateYCounterclockwise());
        BlockState adjacentState = world.getBlockState(adjacentPos);
        if (adjacentState.getBlock() == this && adjacentState.get(DIRECTION).getAxis() == direction.getAxis()) {
            if (adjacentState.get(HINGE) != hingeSide) {
                this.openGate(adjacentState, world, adjacentPos, direction, hitFace, open);
            } else {
                this.openGate(adjacentState.with(DIRECTION, adjacentState.get(DIRECTION).getOpposite()).with(HINGE, this.getOppositeHinge(adjacentState.get(HINGE))), world, adjacentPos, direction, hitFace, open);
            }
        }
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        return state == null ? null : state.with(HINGE, this.getHingeSide(ctx));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        Direction facing = state.get(DIRECTION);
        Direction offset = state.get(HINGE) == DoorHinge.LEFT ? facing.rotateYClockwise() : facing.rotateYCounterclockwise();
        BlockState adjacentBlock = world.getBlockState(pos.offset(offset));
        return state.with(DOUBLE, adjacentBlock.getBlock() == this);
    }

    private DoorHinge getHingeSide(ItemPlacementContext ctx) {
        Direction playerFacing = ctx.getPlayerFacing();
        int offsetX = playerFacing.getOffsetX();
        int offsetZ = playerFacing.getOffsetZ();
        BlockPos pos = ctx.getBlockPos();
        Vec3d hitVec = ctx.getHitPos().subtract(pos.getX(), pos.getY(), pos.getZ());
        boolean side = offsetX < 0 && hitVec.z < 0.5 || offsetX > 0 && hitVec.z > 0.5 || offsetZ < 0 && hitVec.x > 0.5 || offsetZ > 0 && hitVec.x < 0.5;
        return side ? DoorHinge.RIGHT : DoorHinge.LEFT;
    }

    private DoorHinge getOppositeHinge(DoorHinge hingeSide) {
        return hingeSide == DoorHinge.LEFT ? DoorHinge.RIGHT : DoorHinge.LEFT;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(HINGE, OPEN, DOUBLE, POWERED);
    }

    @Override
    public PathNodeType getPathNodeType(BlockState state, BlockView blockView, BlockPos pos) {
        return !state.get(OPEN) ? PathNodeType.FENCE : PathNodeType.OPEN;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (!world.isClient()) {
            boolean powered = world.isReceivingRedstonePower(pos);
            if (state.get(POWERED) != powered) {
                world.setBlockState(pos, state.with(POWERED, powered).with(OPEN, powered), 2);
                if (state.get(OPEN) != powered) {
                    world.syncWorldEvent(null, powered ? 1008 : 1014, pos, 0);
                }
            }
        }
    }
}
