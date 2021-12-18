package work.lclpnet.mmofurniture.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public interface IPathNodeOverride {

    PathNodeType getPathNodeType(BlockState state, BlockView blockView, BlockPos pos);
}
