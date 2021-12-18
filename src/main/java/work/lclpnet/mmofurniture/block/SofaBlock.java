package work.lclpnet.mmofurniture.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
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
import org.jetbrains.annotations.Nullable;
import work.lclpnet.mmofurniture.entity.SeatEntity;
import work.lclpnet.mmofurniture.util.VoxelShapeHelper;

import java.util.ArrayList;
import java.util.List;

public class SofaBlock extends FurnitureHorizontalWaterloggedBlock {

    public static final EnumProperty<Type> TYPE = EnumProperty.of("type", Type.class);

    public final ImmutableMap<BlockState, VoxelShape> SHAPES;

    public SofaBlock(Block parent) {
        super(Settings.copy(parent).sounds(BlockSoundGroup.WOOD));
        setDefaultState(getDefaultState().with(DIRECTION, Direction.NORTH).with(TYPE, Type.SINGLE));
        SHAPES = this.generateShapes(this.getStateManager().getStates());
    }

    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states) {
        final VoxelShape BASE = Block.createCuboidShape(0, 3, 0, 16, 10, 16);
        final VoxelShape[] LEG_BACK_LEFT = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 0, 0, 3, 3, 3), Direction.SOUTH));
        final VoxelShape[] LEG_FRONT_LEFT = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 0, 13, 3, 3, 16), Direction.SOUTH));
        final VoxelShape[] LEG_FRONT_RIGHT = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(13, 0, 13, 16, 3, 16), Direction.SOUTH));
        final VoxelShape[] LEG_BACK_RIGHT = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(13, 0, 0, 16, 3, 3), Direction.SOUTH));
        final VoxelShape[] BACK_REST = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 10, 0, 16, 20, 4), Direction.SOUTH));
        final VoxelShape[] BACK_REST_LEFT = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 10, 4, 4, 20, 16), Direction.SOUTH));
        final VoxelShape[] BACK_REST_RIGHT = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(12, 10, 4, 16, 20, 16), Direction.SOUTH));
        final VoxelShape[] LEFT_ARM_REST = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(-2, 9, 1, 2, 14, 16), Direction.SOUTH));
        final VoxelShape[] RIGHT_ARM_REST = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(14, 9, 1, 18, 14, 16), Direction.SOUTH));

        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for (BlockState state : states) {
            Direction direction = state.get(DIRECTION);
            Type type = state.get(TYPE);
            List<VoxelShape> shapes = new ArrayList<>();
            shapes.add(BASE);
            shapes.add(BACK_REST[direction.getHorizontal()]);
            switch (type) {
                case SINGLE:
                    shapes.add(LEG_BACK_LEFT[direction.getHorizontal()]);
                    shapes.add(LEG_FRONT_LEFT[direction.getHorizontal()]);
                    shapes.add(LEG_FRONT_RIGHT[direction.getHorizontal()]);
                    shapes.add(LEG_BACK_RIGHT[direction.getHorizontal()]);
                    shapes.add(LEFT_ARM_REST[direction.getHorizontal()]);
                    shapes.add(RIGHT_ARM_REST[direction.getHorizontal()]);
                    break;
                case LEFT:
                    shapes.add(LEG_BACK_LEFT[direction.getHorizontal()]);
                    shapes.add(LEG_FRONT_LEFT[direction.getHorizontal()]);
                    shapes.add(LEFT_ARM_REST[direction.getHorizontal()]);
                    break;
                case RIGHT:
                    shapes.add(LEG_FRONT_RIGHT[direction.getHorizontal()]);
                    shapes.add(LEG_BACK_RIGHT[direction.getHorizontal()]);
                    shapes.add(RIGHT_ARM_REST[direction.getHorizontal()]);
                    break;
                case CORNER_LEFT:
                    shapes.add(LEG_BACK_LEFT[direction.getHorizontal()]);
                    shapes.add(BACK_REST_LEFT[direction.getHorizontal()]);
                    break;
                case CORNER_RIGHT:
                    shapes.add(LEG_BACK_RIGHT[direction.getHorizontal()]);
                    shapes.add(BACK_REST_RIGHT[direction.getHorizontal()]);
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
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        if (state == null) return null;
        else return this.getSofaState(state, ctx.getWorld(), ctx.getBlockPos(), state.get(DIRECTION));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) return SeatEntity.create(world, pos, 0.7, player, -0.3F);
        return ActionResult.SUCCESS;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        return this.getSofaState(state, world, pos, state.get(DIRECTION));
    }

    private BlockState getSofaState(BlockState state, WorldAccess world, BlockPos pos, Direction dir) {
        boolean left = isSofa(world, pos, dir.rotateYCounterclockwise(), dir) || isSofa(world, pos, dir.rotateYCounterclockwise(), dir.rotateYCounterclockwise());
        boolean right = isSofa(world, pos, dir.rotateYClockwise(), dir) || isSofa(world, pos, dir.rotateYClockwise(), dir.rotateYClockwise());
        boolean cornerLeft = isSofa(world, pos, dir.getOpposite(), dir.rotateYCounterclockwise());
        boolean cornerRight = isSofa(world, pos, dir.getOpposite(), dir.rotateYClockwise());

        if (cornerLeft) {
            return state.with(TYPE, Type.CORNER_LEFT);
        } else if (cornerRight) {
            return state.with(TYPE, Type.CORNER_RIGHT);
        } else if (left && right) {
            return state.with(TYPE, Type.MIDDLE);
        } else if (left) {
            return state.with(TYPE, Type.RIGHT);
        } else if (right) {
            return state.with(TYPE, Type.LEFT);
        }
        return state.with(TYPE, Type.SINGLE);
    }

    private boolean isSofa(WorldAccess world, BlockPos source, Direction direction, Direction targetDirection) {
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
        MIDDLE("middle"),
        CORNER_LEFT("corner_left"),
        CORNER_RIGHT("corner_right");

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
