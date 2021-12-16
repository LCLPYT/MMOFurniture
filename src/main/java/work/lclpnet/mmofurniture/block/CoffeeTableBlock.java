package work.lclpnet.mmofurniture.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.mmofurniture.util.VoxelShapeHelper;

import java.util.ArrayList;
import java.util.List;

public class CoffeeTableBlock extends FurnitureWaterloggedBlock {

    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty WEST = BooleanProperty.of("west");
    public static final BooleanProperty TALL = BooleanProperty.of("tall");

    public final ImmutableMap<BlockState, VoxelShape> SHAPES;

    public CoffeeTableBlock(Block parent) {
        super(Settings.copy(parent));
        setDefaultState(getDefaultState().with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false).with(TALL, false));
        SHAPES = this.generateShapes(this.getStateManager().getStates());
    }

    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states) {
        final VoxelShape TABLE_TOP_SHORT = Block.createCuboidShape(0.0, 6.0, 0.0, 16.0, 8.0, 16.0);
        final VoxelShape TABLE_TOP_TALL = Block.createCuboidShape(0.0, 14.0, 0.0, 16.0, 16.0, 16.0);
        final VoxelShape LEG_SOUTH_EAST_TALL = Block.createCuboidShape(13.5, 0, 13.5, 15.5, 14, 15.5);
        final VoxelShape LEG_SOUTH_WEST_TALL = Block.createCuboidShape(0.5, 0, 13.5, 2.5, 14, 15.5);
        final VoxelShape LEG_NORTH_WEST_TALL = Block.createCuboidShape(0.5, 0, 0.5, 2.5, 14, 2.5);
        final VoxelShape LEG_NORTH_EAST_TALL = Block.createCuboidShape(13.5, 0, 0.5, 15.5, 14, 2.5);
        final VoxelShape LEG_SOUTH_EAST_SHORT = Block.createCuboidShape(13.5, 0, 13.5, 15.5, 6, 15.5);
        final VoxelShape LEG_SOUTH_WEST_SHORT = Block.createCuboidShape(0.5, 0, 13.5, 2.5, 6, 15.5);
        final VoxelShape LEG_NORTH_WEST_SHORT = Block.createCuboidShape(0.5, 0, 0.5, 2.5, 6, 2.5);
        final VoxelShape LEG_NORTH_EAST_SHORT = Block.createCuboidShape(13.5, 0, 0.5, 15.5, 6, 2.5);

        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for (BlockState state : states) {
            boolean tall = state.get(TALL);
            boolean north = state.get(NORTH);
            boolean east = state.get(EAST);
            boolean south = state.get(SOUTH);
            boolean west = state.get(WEST);

            List<VoxelShape> shapes = new ArrayList<>();
            shapes.add(tall ? TABLE_TOP_TALL : TABLE_TOP_SHORT);
            if (!north && !west) {
                shapes.add(tall ? LEG_NORTH_WEST_TALL : LEG_NORTH_WEST_SHORT);
            }
            if (!north && !east) {
                shapes.add(tall ? LEG_NORTH_EAST_TALL : LEG_NORTH_EAST_SHORT);
            }
            if (!south && !west) {
                shapes.add(tall ? LEG_SOUTH_WEST_TALL : LEG_SOUTH_WEST_SHORT);
            }
            if (!south && !east) {
                shapes.add(tall ? LEG_SOUTH_EAST_TALL : LEG_SOUTH_EAST_SHORT);
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
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES.get(state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        boolean tall = state.get(TALL);
        boolean north = isCoffeeTable(world, pos, Direction.NORTH, tall);
        boolean east = isCoffeeTable(world, pos, Direction.EAST, tall);
        boolean south = isCoffeeTable(world, pos, Direction.SOUTH, tall);
        boolean west = isCoffeeTable(world, pos, Direction.WEST, tall);

        return state.with(NORTH, north).with(EAST, east).with(SOUTH, south).with(WEST, west);
    }

    private boolean isCoffeeTable(WorldAccess world, BlockPos source, Direction direction, boolean tall) {
        BlockState state = world.getBlockState(source.offset(direction));
        return state.getBlock() == this && state.get(TALL) == tall;
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos pos = ctx.getBlockPos();
        BlockState state = ctx.getWorld().getBlockState(pos);
        if (state.getBlock() == this) {
            return state.with(TALL, true);
        }
        return super.getPlacementState(ctx);
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        ItemStack stack = context.getStack();
        return !state.get(TALL) && stack.getItem() == this.asItem();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(NORTH, EAST, SOUTH, WEST, TALL);
    }
}
