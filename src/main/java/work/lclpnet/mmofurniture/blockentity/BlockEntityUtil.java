package work.lclpnet.mmofurniture.blockentity;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public class BlockEntityUtil {

    public static void sendUpdatePacket(BlockEntity blockEntity) {
        BlockEntityUpdateS2CPacket packet = blockEntity.toUpdatePacket();
        if (packet != null) sendUpdatePacket(blockEntity.getWorld(), blockEntity.getPos(), packet);
    }

    private static void sendUpdatePacket(World world, BlockPos pos, final BlockEntityUpdateS2CPacket packet) {
        if (world instanceof ServerWorld)
            PlayerLookup.tracking((ServerWorld) world, new ChunkPos(pos))
                    .forEach(player -> player.networkHandler.sendPacket(packet));
    }
}
