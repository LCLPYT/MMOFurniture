package work.lclpnet.mmofurniture.asm.mixin.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import work.lclpnet.mmofurniture.block.BlindsBlock;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(
            method = "shouldDrawSide(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/util/shape/VoxelShapes;matchesAnywhere(Lnet/minecraft/util/shape/VoxelShape;Lnet/minecraft/util/shape/VoxelShape;Lnet/minecraft/util/function/BooleanBiFunction;)Z"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    private static void shouldDrawSideWithBlinds(BlockState state, BlockView world, BlockPos pos, Direction facing, CallbackInfoReturnable<Boolean> cir, BlockPos offsetPos, BlockState offsetState) {
        if (offsetState.getBlock() instanceof BlindsBlock) cir.setReturnValue(true);
    }
}
