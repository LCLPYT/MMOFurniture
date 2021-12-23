package work.lclpnet.mmofurniture.client.module;

import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import work.lclpnet.mmofurniture.client.render.blockentity.DoorMatBlockEntityRenderer;
import work.lclpnet.mmofurniture.module.DoorMatModule;

public class DoorMatClientModule implements IClientModule {

    @Override
    public void registerClient() {
        BlockEntityRendererRegistry.INSTANCE.register(DoorMatModule.blockEntityType, DoorMatBlockEntityRenderer::new);
    }
}
