package work.lclpnet.mmofurniture.module;

import net.minecraft.block.Block;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmofurniture.MMOFurniture;
import work.lclpnet.mmofurniture.block.CoffeeTableBlock;

public class CoffeeTableModule extends AbstractVariantModule {

    @Override
    protected void registerVariant(String variant, Block parent) {
        new MMOBlockRegistrar(new CoffeeTableBlock(parent))
                .register(MMOFurniture.identifier("%s_coffee_table", variant), MMOFurniture.ITEM_GROUP);
    }
}
