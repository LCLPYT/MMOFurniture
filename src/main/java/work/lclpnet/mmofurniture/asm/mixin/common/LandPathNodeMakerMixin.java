package work.lclpnet.mmofurniture.asm.mixin.common;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.pathing.LandPathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.mmofurniture.block.IPathNodeOverride;

@Mixin(LandPathNodeMaker.class)
public class LandPathNodeMakerMixin {

    @Inject(
            method = "getCommonNodeType(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/entity/ai/pathing/PathNodeType;",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void onOverrideNodeType(BlockView blockView, BlockPos blockPos, CallbackInfoReturnable<PathNodeType> cir) {
        BlockState state = blockView.getBlockState(blockPos);
        Block block = state.getBlock();
        if (!(block instanceof IPathNodeOverride)) return;

        PathNodeType type = ((IPathNodeOverride) block).getPathNodeType(state, blockView, blockPos);
        if (type != null) cir.setReturnValue(type);
    }
}
