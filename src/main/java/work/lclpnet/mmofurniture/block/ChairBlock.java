package work.lclpnet.mmofurniture.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import work.lclpnet.mmofurniture.entity.SeatEntity;
import work.lclpnet.mmofurniture.util.VoxelShapeHelper;

import java.util.ArrayList;
import java.util.List;

public class ChairBlock extends FurnitureHorizontalWaterloggedBlock {

    public final ImmutableMap<BlockState, VoxelShape> SHAPES;

    public ChairBlock(Block parent) {
        super(Settings.copy(parent));
        setDefaultState(getDefaultState().with(DIRECTION, Direction.NORTH));
        SHAPES = this.generateShapes(this.getStateManager().getStates());
    }

    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states) {
        final VoxelShape[] FRONT_LEFT_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(2, 0, 12, 4, 8, 14), Direction.SOUTH));
        final VoxelShape[] BACKREST = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1.5, 10, 1.5, 14.5, 20, 3.5), Direction.SOUTH));
        final VoxelShape[] BASE = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(1.5, 8, 1.5, 14.5, 10, 14.5), Direction.SOUTH));
        final VoxelShape[] FRONT_RIGHT_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(12, 0, 12, 14, 8, 14), Direction.SOUTH));
        final VoxelShape[] BACK_RIGHT_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(12, 0, 2, 14, 8, 4), Direction.SOUTH));
        final VoxelShape[] BACK_LEFT_LEG = VoxelShapeHelper.getRotatedShapes(VoxelShapeHelper.rotate(Block.createCuboidShape(2, 0, 2, 4, 8, 4), Direction.SOUTH));

        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for (BlockState state : states) {
            Direction direction = state.get(DIRECTION);
            List<VoxelShape> shapes = new ArrayList<>();
            shapes.add(FRONT_LEFT_LEG[direction.getHorizontal()]);
            shapes.add(BACKREST[direction.getHorizontal()]);
            shapes.add(BASE[direction.getHorizontal()]);
            shapes.add(FRONT_RIGHT_LEG[direction.getHorizontal()]);
            shapes.add(BACK_RIGHT_LEG[direction.getHorizontal()]);
            shapes.add(BACK_LEFT_LEG[direction.getHorizontal()]);
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
        return SeatEntity.create(world, pos, 0.5, player, -0.1F);
    }
}
