package work.lclpnet.mmofurniture.blockentity;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;

public interface IUpdatePacketReceiver {

    void onDataPacket(ClientConnection connection, BlockEntityUpdateS2CPacket packet);
}
