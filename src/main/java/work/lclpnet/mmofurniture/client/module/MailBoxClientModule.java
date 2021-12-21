package work.lclpnet.mmofurniture.client.module;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import work.lclpnet.mmofurniture.client.gui.screen.MailBoxScreen;
import work.lclpnet.mmofurniture.module.MailBoxModule;

public class MailBoxClientModule implements IClientModule {

    @Override
    public void registerClient() {
        ScreenRegistry.register(MailBoxModule.screenHandlerType, MailBoxScreen::new);
    }
}
