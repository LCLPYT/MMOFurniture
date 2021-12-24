package work.lclpnet.mmofurniture.module;

import net.minecraft.block.Block;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmofurniture.MMOFurniture;
import work.lclpnet.mmofurniture.block.DeskBlock;

public class DeskModule extends AbstractVariantModule {

    @Override
    protected void registerVariant(String variant, Block parent) {
        try {
            int variantIdx = VARIANTS.indexOf(variant);
            if (variantIdx == -1) return;

            new MMOBlockRegistrar(new DeskBlock(parent, (short) variantIdx)) // only supports up to 2^16 - 2 variants
                    .register(MMOFurniture.identifier("%s_desk", variant), MMOFurniture.ITEM_GROUP);
        } catch (IllegalArgumentException ignored) {}
    }
}
