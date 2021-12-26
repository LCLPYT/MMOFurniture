package work.lclpnet.mmofurniture.module;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmofurniture.MMOFurniture;
import work.lclpnet.mmofurniture.block.UpgradedGateBlock;

public class UpgradedGateModule extends AbstractVariantModule {

    public static Tag<Item> vanillaFenceGates;
    public static Tag<Block> tag;

    public UpgradedGateModule() {
        super(false);
    }

    @Override
    public void register() {
        super.register();
        vanillaFenceGates = TagRegistry.item(MMOFurniture.identifier("fence_gates"));
        tag = TagRegistry.block(MMOFurniture.identifier("fence_gates/upgraded"));
    }

    @Override
    protected void registerVariant(String variant, Block parent) {
        new MMOBlockRegistrar(new UpgradedGateBlock(parent))
                .register(MMOFurniture.identifier("%s_upgraded_gate", variant), MMOFurniture.ITEM_GROUP);
    }
}
