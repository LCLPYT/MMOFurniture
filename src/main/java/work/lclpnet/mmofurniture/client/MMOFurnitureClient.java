package work.lclpnet.mmofurniture.client;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.api.ClientModInitializer;
import work.lclpnet.mmofurniture.client.module.ChairClientModule;
import work.lclpnet.mmofurniture.client.module.IClientModule;

import java.util.Set;

public class MMOFurnitureClient implements ClientModInitializer {

    public static final Set<IClientModule> MODULES = ImmutableSet.of(
            new ChairClientModule()
    );

    @Override
    public void onInitializeClient() {
        MODULES.forEach(IClientModule::registerClient);
    }
}
