package work.lclpnet.mmofurniture.client;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.api.ClientModInitializer;
import work.lclpnet.mmofurniture.client.module.ChairClientModule;
import work.lclpnet.mmofurniture.client.module.CratesClientModule;
import work.lclpnet.mmofurniture.client.module.IClientModule;
import work.lclpnet.mmofurniture.network.FurnitureNetworking;

import java.util.Set;

public class MMOFurnitureClient implements ClientModInitializer {

    public static final Set<IClientModule> MODULES = ImmutableSet.of(
            new ChairClientModule(),
            new CratesClientModule()
    );

    @Override
    public void onInitializeClient() {
        FurnitureNetworking.registerPackets();
        FurnitureNetworking.registerClientPacketHandlers();

        MODULES.forEach(IClientModule::registerClient);
    }
}
