package work.lclpnet.mmofurniture.client.module;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import work.lclpnet.mmofurniture.client.gui.screen.CrateScreen;
import work.lclpnet.mmofurniture.module.CratesModule;

public class CratesClientModule implements IClientModule {

    @Override
    public void registerClient() {
        ScreenRegistry.register(CratesModule.screenHandlerType, CrateScreen::new);
    }
}
