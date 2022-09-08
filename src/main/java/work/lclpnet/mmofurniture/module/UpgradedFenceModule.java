package work.lclpnet.mmofurniture.module;

import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmofurniture.MMOFurniture;
import work.lclpnet.mmofurniture.block.UpgradedFenceBlock;

public class UpgradedFenceModule extends AbstractVariantModule {

    public static TagKey<Block> tag;

    public UpgradedFenceModule() {
        super(false);
    }

    @Override
    public void register() {
        super.register();
        tag = TagKey.of(Registry.BLOCK_KEY, MMOFurniture.identifier("fences/upgraded"));
    }

    @Override
    protected void registerVariant(String variant, Block parent) {
        new MMOBlockRegistrar(new UpgradedFenceBlock(parent))
                .register(MMOFurniture.identifier("%s_upgraded_fence", variant), MMOFurniture.ITEM_GROUP);
    }
}
