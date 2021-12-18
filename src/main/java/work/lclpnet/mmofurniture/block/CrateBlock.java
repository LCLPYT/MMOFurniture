package work.lclpnet.mmofurniture.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.mmofurniture.blockentity.CrateBlockEntity;
import work.lclpnet.mmofurniture.inventory.IPortableInventory;
import work.lclpnet.mmofurniture.util.VoxelShapeHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CrateBlock extends FurnitureHorizontalBlock implements IPortableInventory, InventoryProvider, BlockEntityProvider {

    public static final BooleanProperty OPEN = Properties.OPEN;

    public final ImmutableMap<BlockState, VoxelShape> SHAPES;

    public CrateBlock() {
        super(Settings.copy(Blocks.CHEST));
        setDefaultState(getDefaultState().with(OPEN, false).with(DIRECTION, Direction.NORTH));
        SHAPES = this.generateShapes(this.getStateManager().getStates());
    }

    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states) {
        final VoxelShape[] OPEN_LID = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 13, -2, 16, 29, 1), Direction.SOUTH));

        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for (BlockState state : states) {
            Direction direction = state.get(DIRECTION);
            boolean open = state.get(OPEN);

            List<VoxelShape> shapes = new ArrayList<>();
            if (open) {
                shapes.add(Block.createCuboidShape(0, 0, 0, 16, 13, 16));
                shapes.add(OPEN_LID[direction.getHorizontal()]);
            } else {
                shapes.add(VoxelShapes.fullCube());
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
    public float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof CrateBlockEntity) {
            CrateBlockEntity crateTileEntity = (CrateBlockEntity) tileEntity;
            if (crateTileEntity.isLocked() && !player.getUuid().equals(crateTileEntity.getOwnerUuid()))
                return 0.0005F;
        }
        return super.calcBlockBreakingDelta(state, player, world, pos);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof CrateBlockEntity)
                player.openHandledScreen((NamedScreenHandlerFactory) tileEntity);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof CrateBlockEntity)
            ((CrateBlockEntity) tileEntity).onScheduledTick();
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (placer != null && tileEntity instanceof CrateBlockEntity)
            ((CrateBlockEntity) tileEntity).setOwnerUuid(placer.getUuid());
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new CrateBlockEntity();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(OPEN);
    }

    @Override
    public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof CrateBlockEntity && !((CrateBlockEntity) tileEntity).isLocked()) {
            return (CrateBlockEntity) tileEntity;
        }
        return null;
    }
}
