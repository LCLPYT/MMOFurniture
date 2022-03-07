package work.lclpnet.mmofurniture.client.module;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import work.lclpnet.mmocontent.client.entity.MMOClientEntities;
import work.lclpnet.mmofurniture.client.render.entity.SeatRenderer;
import work.lclpnet.mmofurniture.entity.SeatEntity;
import work.lclpnet.mmofurniture.module.ChairModule;

public class ChairClientModule implements IClientModule {

    @Override
    public void registerClient() {
        EntityRendererRegistry.register(ChairModule.seatType, SeatRenderer::new);
        MMOClientEntities.registerNonLiving(ChairModule.seatType, SeatEntity::new);
    }
}
