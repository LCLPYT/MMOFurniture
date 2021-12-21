package work.lclpnet.mmofurniture.module;

import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmofurniture.MMOFurniture;
import work.lclpnet.mmofurniture.block.ParkBenchBlock;

public class ParkBenchModule extends AbstractVariantModule {

    public ParkBenchModule() {
        super(false);
    }

    @Override
    protected void registerVariant(String variant, Block parent) {
        new MMOBlockRegistrar(new ParkBenchBlock(parent))
                .register(MMOFurniture.identifier("%s_park_bench", variant), ItemGroup.DECORATIONS);
    }
}
