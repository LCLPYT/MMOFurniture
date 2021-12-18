package work.lclpnet.mmofurniture.module;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import net.minecraft.tag.Tag;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmofurniture.MMOFurniture;
import work.lclpnet.mmofurniture.block.UpgradedFenceBlock;

public class UpgradedFenceModule extends AbstractVariantModule {

    public static Tag<Block> tag;

    public UpgradedFenceModule() {
        super(false);
    }

    @Override
    public void register() {
        super.register();
        tag = TagRegistry.block(MMOFurniture.identifier("fences/upgraded"));
    }

    @Override
    protected void registerVariant(String variant, Block parent) {
        new MMOBlockRegistrar(new UpgradedFenceBlock(parent))
                .register(MMOFurniture.identifier("%s_upgraded_fence", variant), ItemGroup.DECORATIONS);
    }
}
