package work.lclpnet.mmofurniture.module;

import net.minecraft.block.Block;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmofurniture.MMOFurniture;
import work.lclpnet.mmofurniture.block.SofaBlock;

public class SofaModule extends AbstractColoredModule {

    @Override
    protected void registerVariant(String color, Block parent) {
        new MMOBlockRegistrar(new SofaBlock(parent))
                .register(MMOFurniture.identifier("%s_sofa", color), MMOFurniture.ITEM_GROUP);
    }
}
