package work.lclpnet.mmofurniture.module;

import net.minecraft.block.Block;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmofurniture.MMOFurniture;
import work.lclpnet.mmofurniture.block.BlindsBlock;

public class BlindsModule extends AbstractVariantModule {

    public BlindsModule() {
        super(false);
    }

    @Override
    protected void registerVariant(String variant, Block parent) {
        new MMOBlockRegistrar(new BlindsBlock(parent))
                .register(MMOFurniture.identifier("%s_blinds", variant), MMOFurniture.ITEM_GROUP);
    }
}
