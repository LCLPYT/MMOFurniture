package work.lclpnet.mmofurniture.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import work.lclpnet.mmocontent.block.ext.MMOBlock;
import work.lclpnet.mmofurniture.util.VoxelShapeHelper;

import java.util.ArrayList;
import java.util.List;

public class TableBlock extends FurnitureWaterloggedBlock implements Waterloggable {

    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty WEST = BooleanProperty.of("west");

    public final ImmutableMap<BlockState, VoxelShape> SHAPES;

    public TableBlock(Block parent) {
        super(Settings.copy(parent));
        setDefaultState(getDefaultState()
                .with(NORTH, false)
                .with(EAST, false)
                .with(SOUTH, false)
                .with(WEST, false));

        SHAPES = this.generateShapes(this.getStateManager().getStates());
    }

    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states) {
        final VoxelShape TABLE_TOP = Block.createCuboidShape(0.0, 14.0, 0.0, 16.0, 16.0, 16.0);
        final VoxelShape MIDDLE_POST = Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 14.0, 10.0);
        final VoxelShape END_POST = Block.createCuboidShape(3.0, 0.0, 6.0, 7.0, 14.0, 10.0);
        final VoxelShape CORNER_POST = Block.createCuboidShape(3.0, 0.0, 9.0, 7.0, 14.0, 13.0);

        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for (BlockState state : states) {
            boolean north = state.get(NORTH);
            boolean east = state.get(EAST);
            boolean south = state.get(SOUTH);
            boolean west = state.get(WEST);

            List<VoxelShape> shapes = new ArrayList<>();
            shapes.add(TABLE_TOP);

            if (!north & !east && !south && !west) {
                shapes.add(MIDDLE_POST);
            } else if (north & !east && !south && !west) {
                shapes.add(VoxelShapeHelper.rotate(END_POST, Direction.NORTH));
            } else if (!north & east && !south && !west) {
                shapes.add(VoxelShapeHelper.rotate(END_POST, Direction.EAST));
            } else if (!north & !east && south && !west) {
                shapes.add(VoxelShapeHelper.rotate(END_POST, Direction.SOUTH));
            } else if (!north & !east && !south) {
                shapes.add(VoxelShapeHelper.rotate(END_POST, Direction.WEST));
            } else if (north && east && !south && !west) {
                shapes.add(VoxelShapeHelper.rotate(CORNER_POST, Direction.EAST));
            } else if (!north && east && south && !west) {
                shapes.add(VoxelShapeHelper.rotate(CORNER_POST, Direction.SOUTH));
            } else if (!north && !east) {
                shapes.add(VoxelShapeHelper.rotate(CORNER_POST, Direction.WEST));
            } else if (north && !east && !south) {
                shapes.add(VoxelShapeHelper.rotate(CORNER_POST, Direction.NORTH));
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
        boolean north = world.getBlockState(pos.north()).getBlock() == this;
        boolean east = world.getBlockState(pos.east()).getBlock() == this;
        boolean south = world.getBlockState(pos.south()).getBlock() == this;
        boolean west = world.getBlockState(pos.west()).getBlock() == this;

        return state.with(NORTH, north).with(EAST, east).with(SOUTH, south).with(WEST, west);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(NORTH, EAST, SOUTH, WEST);
    }
}
