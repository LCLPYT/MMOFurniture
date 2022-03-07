package work.lclpnet.mmofurniture.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.mmofurniture.blockentity.BedsideCabinetBlockEntity;
import work.lclpnet.mmofurniture.util.VoxelShapeHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BedsideCabinetBlock extends FurnitureHorizontalWaterloggedBlock implements BlockEntityProvider, InventoryProvider {

    public static final BooleanProperty OPEN = BooleanProperty.of("open");

    public final ImmutableMap<BlockState, VoxelShape> SHAPES;

    public BedsideCabinetBlock(Block parent) {
        super(Settings.copy(parent));
        setDefaultState(getDefaultState().with(DIRECTION, Direction.NORTH).with(OPEN, false));
        SHAPES = this.generateShapes(this.getStateManager().getStates());
    }

    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states) {
        final VoxelShape[] LEG_BACK_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 0, 1, 3, 2, 3), Direction.SOUTH));
        final VoxelShape[] LEG_FRONT_LEFT = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 0, 11, 3, 2, 13), Direction.SOUTH));
        final VoxelShape[] LEG_FRONT_RIGHT = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(13, 0, 11, 15, 2, 13), Direction.SOUTH));
        final VoxelShape[] LEG_BACK_RIGHT = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(13, 0, 1, 15, 2, 3), Direction.SOUTH));
        final VoxelShape[] TOP = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 14, 0, 16, 16, 16), Direction.SOUTH));
        final VoxelShape[] HANDLE_BOTTOM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(5, 5, 15, 11, 6, 16), Direction.SOUTH));
        final VoxelShape[] BASE_CLOSED = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 2, 0, 16, 14, 15), Direction.SOUTH));
        final VoxelShape[] HANDLE_TOP_CLOSED = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(5, 11, 15, 11, 12, 16), Direction.SOUTH));
        final VoxelShape[] BASE_OPEN = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 2, 0, 16, 14, 13), Direction.SOUTH));
        final VoxelShape[] DRAW_TOP_OPEN = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 8, 20, 16, 14, 22), Direction.SOUTH));
        final VoxelShape[] DRAW_BOTTOM_OPEN = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 2, 13, 16, 8, 15), Direction.SOUTH));
        final VoxelShape[] HANDLE_TOP_OPEN = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(5, 11, 22, 11, 12, 23), Direction.SOUTH));
        final VoxelShape[] DRAW_INSIDE_BOTTOM = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 8, 13, 15, 10, 20), Direction.SOUTH));
        final VoxelShape[] DRAW_INSIDE_LEFT = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1, 10, 13, 3, 14, 20), Direction.SOUTH));
        final VoxelShape[] DRAW_INSIDE_RIGHT = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(13, 10, 13, 15, 14, 20), Direction.SOUTH));

        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for (BlockState state : states) {
            Direction direction = state.get(DIRECTION);
            List<VoxelShape> shapes = new ArrayList<>();
            shapes.add(LEG_BACK_LEG[direction.getHorizontal()]);
            shapes.add(LEG_FRONT_LEFT[direction.getHorizontal()]);
            shapes.add(LEG_FRONT_RIGHT[direction.getHorizontal()]);
            shapes.add(LEG_BACK_RIGHT[direction.getHorizontal()]);
            shapes.add(TOP[direction.getHorizontal()]);
            shapes.add(HANDLE_BOTTOM[direction.getHorizontal()]);
            if (state.get(OPEN)) {
                shapes.add(BASE_OPEN[direction.getHorizontal()]);
                shapes.add(DRAW_TOP_OPEN[direction.getHorizontal()]);
                shapes.add(DRAW_BOTTOM_OPEN[direction.getHorizontal()]);
                shapes.add(HANDLE_TOP_OPEN[direction.getHorizontal()]);
                shapes.add(DRAW_INSIDE_BOTTOM[direction.getHorizontal()]);
                shapes.add(DRAW_INSIDE_LEFT[direction.getHorizontal()]);
                shapes.add(DRAW_INSIDE_RIGHT[direction.getHorizontal()]);
            } else {
                shapes.add(BASE_CLOSED[direction.getHorizontal()]);
                shapes.add(HANDLE_TOP_CLOSED[direction.getHorizontal()]);
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
        if (state.get(DIRECTION).getOpposite() == hit.getSide()) {
            if (!world.isClient()) {
                BlockEntity tileEntity = world.getBlockEntity(pos);
                if (tileEntity instanceof BedsideCabinetBlockEntity) {
                    player.openHandledScreen((NamedScreenHandlerFactory) tileEntity);
                }
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof BedsideCabinetBlockEntity)
            ((BedsideCabinetBlockEntity) blockEntity).scheduledTick();
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BedsideCabinetBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(OPEN);
    }

    @Override
    public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        return tileEntity instanceof SidedInventory ? (SidedInventory) tileEntity : null;
    }
}
