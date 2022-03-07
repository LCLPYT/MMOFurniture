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
import work.lclpnet.mmofurniture.blockentity.CabinetBlockEntity;
import work.lclpnet.mmofurniture.util.VoxelShapeHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CabinetBlock extends FurnitureHorizontalWaterloggedBlock implements BlockEntityProvider, InventoryProvider {

    public static final BooleanProperty OPEN = BooleanProperty.of("open");

    public final ImmutableMap<BlockState, VoxelShape> SHAPES;

    public CabinetBlock(Block parent) {
        super(Settings.copy(parent));
        setDefaultState(getDefaultState().with(DIRECTION, Direction.NORTH).with(OPEN, false));
        SHAPES = this.generateShapes(this.getStateManager().getStates());
    }

    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states) {
        final VoxelShape[] BASE_CLOSED = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 0, 0, 16, 16, 15), Direction.SOUTH));
        final VoxelShape[] HANDLE_CLOSED = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(14, 5, 15, 15, 11, 16), Direction.SOUTH));
        final VoxelShape[] BASE_OPEN = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 0, 0, 16, 16, 13), Direction.SOUTH));
        final VoxelShape[] HANDLE_OPEN = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(-1, 5, 27, 0, 11, 28), Direction.SOUTH));
        final VoxelShape[] DOOR_OPEN = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 0, 13, 2, 16, 29), Direction.SOUTH));

        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for (BlockState state : states) {
            Direction direction = state.get(DIRECTION);
            List<VoxelShape> shapes = new ArrayList<>();
            if (state.get(OPEN)) {
                shapes.add(BASE_OPEN[direction.getHorizontal()]);
                shapes.add(HANDLE_OPEN[direction.getHorizontal()]);
                shapes.add(DOOR_OPEN[direction.getHorizontal()]);
            } else {
                shapes.add(BASE_CLOSED[direction.getHorizontal()]);
                shapes.add(HANDLE_CLOSED[direction.getHorizontal()]);
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
                if (tileEntity instanceof CabinetBlockEntity) {
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
        if (blockEntity instanceof CabinetBlockEntity)
            ((CabinetBlockEntity) blockEntity).scheduledTick();
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CabinetBlockEntity(pos, state);
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
