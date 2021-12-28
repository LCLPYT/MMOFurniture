package work.lclpnet.mmofurniture.client.module;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface IClientModule {

    void registerClient();
}
