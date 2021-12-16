package work.lclpnet.mmofurniture.client.module;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import work.lclpnet.mmocontent.client.entity.MMOClientEntities;
import work.lclpnet.mmofurniture.client.render.entity.SeatRenderer;
import work.lclpnet.mmofurniture.entity.SeatEntity;
import work.lclpnet.mmofurniture.module.ChairModule;

public class ChairClientModule implements IClientModule {

    @Override
    public void registerClient() {
        EntityRendererRegistry.INSTANCE.register(ChairModule.seatType, (manager, context) -> new SeatRenderer(manager));
        MMOClientEntities.registerNonLiving(ChairModule.seatType, SeatEntity::new);
    }
}
