package work.lclpnet.mmofurniture.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.SoundCategory;
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
import work.lclpnet.mmofurniture.sound.FurnitureSounds;
import work.lclpnet.mmofurniture.util.VoxelShapeHelper;

import java.util.ArrayList;
import java.util.List;

public class BlindsBlock extends FurnitureHorizontalWaterloggedBlock {

    public static final BooleanProperty OPEN = BooleanProperty.of("open");
    public static final BooleanProperty EXTENSION = BooleanProperty.of("extension");

    public final ImmutableMap<BlockState, VoxelShape> SHAPES;

    public BlindsBlock(Block parent) {
        super(Settings.copy(parent));
        setDefaultState(getDefaultState()
                .with(DIRECTION, Direction.NORTH)
                .with(OPEN, true)
                .with(EXTENSION, false)
                .with(WATERLOGGED, false));
        SHAPES = this.generateShapes(this.getStateManager().getStates());
    }

    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states) {
        final VoxelShape[] BOX = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 0, 0, 16, 16, 3), Direction.SOUTH));
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for (BlockState state : states) {
            Direction direction = state.get(DIRECTION);
            List<VoxelShape> shapes = new ArrayList<>();
            shapes.add(BOX[direction.getHorizontal()]);
            builder.put(state, VoxelShapeHelper.combineAll(shapes));
        }
        return builder.build();
    }

    @Override
    public boolean hasDynamicBounds() {
        return true;
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
        return VoxelShapeHelper.rotate(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 1, 0, 16, 16, 3), Direction.SOUTH), state.get(DIRECTION));
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        return state == null ? null : this.getBlindState(state, ctx.getWorld(), ctx.getBlockPos());
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        return this.getBlindState(state, world, pos);
    }

    private BlockState getBlindState(BlockState state, WorldAccess world, BlockPos pos) {
        BlockState aboveState = world.getBlockState(pos.up());
        boolean isExtension = aboveState.getBlock() == this && aboveState.get(DIRECTION) == state.get(DIRECTION);
        return state.with(EXTENSION, isExtension);
    }

    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return !state.get(OPEN);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        this.toggleBlinds(world, pos, !state.get(OPEN), state.get(DIRECTION), 5);
        if (!world.isClient) {
            if (state.get(OPEN)) {
                world.playSound(null, pos, FurnitureSounds.BLOCK_BLINDS_CLOSE, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.8F);
            } else {
                world.playSound(null, pos, FurnitureSounds.BLOCK_BLINDS_OPEN, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
            }
        }
        return ActionResult.SUCCESS;
    }

    private void toggleBlinds(World world, BlockPos pos, boolean targetOpen, Direction targetDirection, int depth) {
        if (depth <= 0)
            return;

        BlockState state = world.getBlockState(pos);
        if (state.getBlock() == this) {
            boolean open = state.get(OPEN);
            Direction direction = state.get(DIRECTION);
            if (open != targetOpen && direction.equals(targetDirection)) {
                world.setBlockState(pos, state.with(OPEN, targetOpen), 3);
                this.toggleBlinds(world, pos.offset(targetDirection.rotateYClockwise()), targetOpen, targetDirection, depth - 1);
                this.toggleBlinds(world, pos.offset(targetDirection.rotateYCounterclockwise()), targetOpen, targetDirection, depth - 1);
                this.toggleBlinds(world, pos.offset(Direction.UP), targetOpen, targetDirection, depth - 1);
                this.toggleBlinds(world, pos.offset(Direction.DOWN), targetOpen, targetDirection, depth - 1);
            }
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(OPEN, EXTENSION);
    }
}
