package work.lclpnet.mmofurniture.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import work.lclpnet.mmocontent.networking.IPacketDecoder;
import work.lclpnet.mmocontent.networking.IServerPacketHandler;
import work.lclpnet.mmocontent.networking.MCPacket;
import work.lclpnet.mmofurniture.MMOFurniture;
import work.lclpnet.mmofurniture.blockentity.DoorMatBlockEntity;

public class DoorMatMessagePacket extends MCPacket implements IServerPacketHandler {

    public static final Identifier ID = MMOFurniture.identifier("door_mat_message");

    private final BlockPos pos;
    private final String message;

    public DoorMatMessagePacket(BlockPos pos, String message) {
        super(ID);
        this.pos = pos;
        this.message = message;
    }

    @Override
    public void handleServer(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender) {
        if (player == null) return;

        server.execute(() -> {
            World world = player.getWorld();
            if (!world.isRegionLoaded(pos, pos)) return;

            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof DoorMatBlockEntity)
                ((DoorMatBlockEntity) blockEntity).setMessage(message);
        });
    }

    @Override
    public void encodeTo(PacketByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeString(message, 64);
    }

    public static class Decoder implements IPacketDecoder<DoorMatMessagePacket> {

        @Override
        public DoorMatMessagePacket decode(PacketByteBuf buffer) {
            BlockPos pos = buffer.readBlockPos();
            String message = buffer.readString(64);
            return new DoorMatMessagePacket(pos, message);
        }
    }
}
