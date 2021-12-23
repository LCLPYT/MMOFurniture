package work.lclpnet.mmofurniture.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.logging.log4j.LogManager;
import work.lclpnet.mmocontent.networking.MMOPacketRegistrar;
import work.lclpnet.mmocontent.util.Env;
import work.lclpnet.mmofurniture.network.packet.DoorMatMessagePacket;
import work.lclpnet.mmofurniture.network.packet.LockCratePacket;

public class FurnitureNetworking {

    private static MMOPacketRegistrar registrar = null;

    public static void registerPackets() {
        if (registrar != null) return; // already registered

        registrar = new MMOPacketRegistrar(LogManager.getLogger());
        registrar.register(LockCratePacket.ID, new LockCratePacket.Decoder());
        registrar.register(DoorMatMessagePacket.ID, new DoorMatMessagePacket.Decoder());
    }

    @Environment(EnvType.CLIENT)
    public static void registerClientPacketHandlers() {
        registrar.registerClientPacketHandlers();
        registrar = null; // should be called last on client
    }

    public static void registerServerPacketHandlers() {
        registrar.registerServerPacketHandlers();
        if (!Env.isClient()) registrar = null; // not needed any further on a dedicated server
    }
}
