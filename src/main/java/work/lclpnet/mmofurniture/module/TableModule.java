package work.lclpnet.mmofurniture.module;

import net.minecraft.block.Block;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmofurniture.MMOFurniture;
import work.lclpnet.mmofurniture.block.TableBlock;

public class TableModule extends AbstractVariantModule {

    @Override
    protected void registerVariant(String variant, Block parent) {
        new MMOBlockRegistrar(new TableBlock(parent))
                .register(MMOFurniture.identifier("%s_table", variant), MMOFurniture.ITEM_GROUP);
    }
}
