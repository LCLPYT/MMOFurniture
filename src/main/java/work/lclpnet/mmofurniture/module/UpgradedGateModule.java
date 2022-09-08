package work.lclpnet.mmofurniture.module;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmofurniture.MMOFurniture;
import work.lclpnet.mmofurniture.block.UpgradedGateBlock;

public class UpgradedGateModule extends AbstractVariantModule {

    public static TagKey<Item> vanillaFenceGates;
    public static TagKey<Block> tag;

    public UpgradedGateModule() {
        super(false);
    }

    @Override
    public void register() {
        super.register();
        vanillaFenceGates = TagKey.of(Registry.ITEM_KEY, MMOFurniture.identifier("fence_gates"));
        tag = TagKey.of(Registry.BLOCK_KEY, MMOFurniture.identifier("fence_gates/upgraded"));
    }

    @Override
    protected void registerVariant(String variant, Block parent) {
        new MMOBlockRegistrar(new UpgradedGateBlock(parent))
                .register(MMOFurniture.identifier("%s_upgraded_gate", variant), MMOFurniture.ITEM_GROUP);
    }
}
