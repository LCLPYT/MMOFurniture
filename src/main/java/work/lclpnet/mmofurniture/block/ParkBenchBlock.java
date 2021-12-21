package work.lclpnet.mmofurniture.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import work.lclpnet.mmofurniture.entity.SeatEntity;
import work.lclpnet.mmofurniture.util.VoxelShapeHelper;

import java.util.ArrayList;
import java.util.List;

public class ParkBenchBlock extends FurnitureHorizontalWaterloggedBlock {

    public static final EnumProperty<Type> TYPE = EnumProperty.of("type", Type.class);

    public final ImmutableMap<BlockState, VoxelShape> SHAPES;

    public ParkBenchBlock(Block parent) {
        super(Settings.copy(parent));
        setDefaultState(getDefaultState().with(DIRECTION, Direction.NORTH).with(TYPE, Type.SINGLE).with(WATERLOGGED, false));
        SHAPES = this.generateShapes(this.getStateManager().getStates());
    }

    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states) {
        final VoxelShape[] SEAT = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 8, 3, 16, 9, 16), Direction.SOUTH));
        final VoxelShape[] BACKREST = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 9, 1, 16, 20, 5), Direction.SOUTH));
        final VoxelShape[] BACK_LEFT_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 0, 3, 4, 8, 6), Direction.SOUTH));
        final VoxelShape[] FRONT_LEFT_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 0, 12, 4, 8, 15), Direction.SOUTH));
        final VoxelShape[] FRONT_RIGHT_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(12, 0, 12, 15, 8, 15), Direction.SOUTH));
        final VoxelShape[] BACK_RIGHT_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(12, 0, 3, 15, 8, 6), Direction.SOUTH));
        final VoxelShape[] LEFT_SUPPORT = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 6, 6, 4, 8, 12), Direction.SOUTH));
        final VoxelShape[] RIGHT_SUPPORT = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(12, 6, 6, 15, 8, 12), Direction.SOUTH));
        final VoxelShape[] FRONT_SUPPORT = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 6, 12, 15, 8, 15), Direction.SOUTH));
        final VoxelShape[] BACK_SUPPORT = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 6, 3, 15, 8, 6), Direction.SOUTH));

        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for (BlockState state : states) {
            Direction direction = state.get(DIRECTION);
            Type type = state.get(TYPE);

            List<VoxelShape> shapes = new ArrayList<>();
            shapes.add(SEAT[direction.getHorizontal()]);
            shapes.add(BACKREST[direction.getHorizontal()]);
            shapes.add(FRONT_SUPPORT[direction.getHorizontal()]);
            shapes.add(BACK_SUPPORT[direction.getHorizontal()]);

            switch (type) {
                case SINGLE:
                    shapes.add(BACK_LEFT_LEG[direction.getHorizontal()]);
                    shapes.add(FRONT_LEFT_LEG[direction.getHorizontal()]);
                    shapes.add(FRONT_RIGHT_LEG[direction.getHorizontal()]);
                    shapes.add(BACK_RIGHT_LEG[direction.getHorizontal()]);
                    shapes.add(LEFT_SUPPORT[direction.getHorizontal()]);
                    shapes.add(RIGHT_SUPPORT[direction.getHorizontal()]);
                    break;
                case LEFT:
                    shapes.add(BACK_LEFT_LEG[direction.getHorizontal()]);
                    shapes.add(FRONT_LEFT_LEG[direction.getHorizontal()]);
                    shapes.add(LEFT_SUPPORT[direction.getHorizontal()]);
                    break;
                case RIGHT:
                    shapes.add(FRONT_RIGHT_LEG[direction.getHorizontal()]);
                    shapes.add(BACK_RIGHT_LEG[direction.getHorizontal()]);
                    shapes.add(RIGHT_SUPPORT[direction.getHorizontal()]);
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
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return SeatEntity.create(world, pos, 0.5, player, -0.1625F);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        return state == null ? null : this.getBenchState(state, ctx.getWorld(), ctx.getBlockPos(), state.get(DIRECTION));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        return this.getBenchState(state, world, pos, state.get(DIRECTION));
    }

    private BlockState getBenchState(BlockState state, WorldAccess world, BlockPos pos, Direction dir) {
        boolean left = isBench(world, pos, dir.rotateYCounterclockwise(), dir);
        boolean right = isBench(world, pos, dir.rotateYClockwise(), dir);
        if (left && right) {
            return state.with(TYPE, Type.MIDDLE);
        } else if (left) {
            return state.with(TYPE, Type.RIGHT);
        } else if (right) {
            return state.with(TYPE, Type.LEFT);
        }
        return state.with(TYPE, Type.SINGLE);
    }

    private boolean isBench(WorldAccess world, BlockPos source, Direction direction, Direction targetDirection) {
        BlockState state = world.getBlockState(source.offset(direction));
        if (state.getBlock() == this) {
            Direction sofaDirection = state.get(DIRECTION);
            return sofaDirection.equals(targetDirection);
        }
        return false;
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
