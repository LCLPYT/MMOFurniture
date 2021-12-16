package work.lclpnet.mmofurniture.module;

import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmofurniture.MMOFurniture;
import work.lclpnet.mmofurniture.block.TableBlock;

public class TableModule extends AbstractVariantModule {

    @Override
    protected void registerVariant(String variant, Block parent) {
        new MMOBlockRegistrar(new TableBlock(parent))
                .register(MMOFurniture.identifier(String.format("%s_table", variant)), ItemGroup.DECORATIONS);
    }
}
