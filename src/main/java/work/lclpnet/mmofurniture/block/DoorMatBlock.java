package work.lclpnet.mmofurniture.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.mmofurniture.blockentity.DoorMatBlockEntity;
import work.lclpnet.mmofurniture.item.DoorMatItem;
import work.lclpnet.mmofurniture.util.VoxelShapeHelper;

import java.util.HashMap;
import java.util.Map;

public class DoorMatBlock extends FurnitureHorizontalWaterloggedBlock implements BlockEntityProvider {

    public final Map<BlockState, VoxelShape> SHAPES = new HashMap<>();

    public DoorMatBlock() {
        super(Settings.copy(Blocks.HAY_BLOCK));
    }

    private VoxelShape getShape(BlockState state) {
        return SHAPES.computeIfAbsent(state, key -> {
            final VoxelShape[] BOXES = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(0, 0, 2, 16, 1, 14), Direction.SOUTH));
            return BOXES[state.get(DIRECTION).getHorizontal()];
        });
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.getShape(state);
    }

    @Override
    public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return this.getShape(state);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new DoorMatBlockEntity();
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        return !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return !world.isAir(pos.down());
    }

    @Override
    public BlockItem provideBlockItem(Item.Settings settings) {
        return new DoorMatItem(this, settings);
    }
}
