package work.lclpnet.mmofurniture.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import work.lclpnet.mmocontent.networking.IPacketDecoder;
import work.lclpnet.mmocontent.networking.IServerPacketHandler;
import work.lclpnet.mmocontent.networking.MCPacket;
import work.lclpnet.mmofurniture.MMOFurniture;
import work.lclpnet.mmofurniture.blockentity.CrateBlockEntity;
import work.lclpnet.mmofurniture.inventory.CrateScreenHandler;

public class LockCratePacket extends MCPacket implements IServerPacketHandler {

    public static final Identifier ID = MMOFurniture.identifier("lock_crate");

    public LockCratePacket() {
        super(ID);
    }

    @Override
    public void handleServer(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender) {
        server.execute(() -> {
            if (player != null && player.currentScreenHandler instanceof CrateScreenHandler) {
                CrateBlockEntity inventory = ((CrateScreenHandler) player.currentScreenHandler).getCrateBlockEntity();
                if (inventory != null) {
                    if (inventory.getOwnerUuid() == null) {
                        inventory.setOwnerUuid(player.getUuid());
                    }
                    if (player.getUuid().equals(inventory.getOwnerUuid())) {
                        inventory.setLocked(!inventory.isLocked());
                        inventory.removeUnauthorisedPlayers();
                    }
                }
            }
        });
    }

    @Override
    public void encodeTo(PacketByteBuf buffer) {
    }

    public static class Decoder implements IPacketDecoder<LockCratePacket> {

        @Override
        public LockCratePacket decode(PacketByteBuf buffer) {
            return new LockCratePacket();
        }
    }
}
